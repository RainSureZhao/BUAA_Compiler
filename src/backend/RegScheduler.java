package backend;

import backend.MipsCode.AbsoluteAddress;
import backend.MipsCode.Address;
import backend.MipsCode.ITypeInstr;
import backend.MipsCode.RelativeAddress;
import midend.MidCode.Addr;
import midend.MidCode.Imm;
import midend.MidCode.Value;
import midend.MidCode.Word;

import java.util.*;

public class RegScheduler {
    // 定义寄存器列表
    static List<Reg> regs = Arrays.asList(
            Reg.T0, Reg.T1, Reg.T2, Reg.T3, Reg.T4,
            Reg.T5, Reg.T6, Reg.T7, Reg.T8, Reg.T9,
            Reg.S0, Reg.S1, Reg.S2, Reg.S3, Reg.S4,
            Reg.S5, Reg.S6, Reg.S7, Reg.S8, Reg.S9,
            Reg.S10, Reg.S11, Reg.S12, Reg.S13, Reg.S14
    );
    private final HashMap<Reg, Value> reg2value = new HashMap<>(); // 寄存器到值的映射
    private final LinkedList<Reg> busyRegs = new LinkedList<>(); // 忙碌的寄存器列表
    private final LinkedList<Reg> freeRegs = new LinkedList<>(); // 空闲的寄存器列表

    public RegScheduler() {
        freeRegs.addAll(regs); // 初始化空闲寄存器列表
    }

    public HashMap<Reg, Value> getReg2value() {
        return reg2value; // 获取寄存器到值的映射
    }

    public void clear() {
        reg2value.clear(); // 清空寄存器到值的映射
        busyRegs.clear(); // 清空忙碌的寄存器列表
        freeRegs.clear(); // 清空空闲的寄存器列表
        freeRegs.addAll(regs); // 重新添加所有寄存器到空闲寄存器列表
    }

    public Reg find(Value value) {
        for (Reg reg : busyRegs) {
            if (reg2value.get(reg).equals(value)) { // 查找值对应的寄存器
                return reg;
            }
        }
        return null;
    }

    public Reg alloc(Value value) {
        if (!freeRegs.isEmpty()) {
            Reg reg = freeRegs.getFirst(); // 分配一个空闲寄存器
            freeRegs.removeFirst(); // 从空闲寄存器列表中移除已分配的寄存器
            busyRegs.addLast(reg); // 将寄存器添加到忙碌寄存器列表
            reg2value.put(reg, value); // 将寄存器与值建立映射关系
            return reg;
        }
        return null;
    }

    public Reg preempt(Value value) {
        for (Reg reg : busyRegs) {
            if (!Translator.getInstance().getSynchronizedReg().contains(reg)) {
                Value oldValue = reg2value.get(reg);
                if (oldValue instanceof Word || (value instanceof Addr && ((Addr) oldValue).isTemp())) {
                    Translator.getInstance().getMipsCodeList().add(
                            new ITypeInstr.sw(reg, Translator.getInstance().getValue2address().get(oldValue)));
                }
                reg2value.put(reg, value); // 替换寄存器的值
                busyRegs.remove(reg); // 从忙碌寄存器列表中移除寄存器
                busyRegs.addLast(reg); // 将寄存器添加到忙碌寄存器列表的末尾
                return reg;
            }
        }
        return null;
    }

    public void flush() {
        for (Map.Entry<Reg, Value> entry : reg2value.entrySet()) {
            Address address = Translator.getInstance().getValue2address().get(entry.getValue());
            if (address instanceof AbsoluteAddress && entry.getValue() instanceof Word ||
                    address instanceof RelativeAddress && !(entry.getValue() instanceof Addr)) {
                Translator.getInstance().getMipsCodeList().add(new ITypeInstr.sw(entry.getKey(), address));
            }
        }
        clear(); // 清空寄存器状态
    }
}
