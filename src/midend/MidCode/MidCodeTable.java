package midend.MidCode;

import midend.LabelTable.Label;
import midend.LabelTable.LabelTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringJoiner;

public class MidCodeTable {
    private static final MidCodeTable MID_CODE_TABLE = new MidCodeTable(); // 单例模式，创建MidCodeTable的实例
    private static String curFunc = "@global"; // 当前函数名，默认为全局函数
    private static final LinkedList<Label> loopBegins = new LinkedList<>(); // 循环开始标签的链表
    private static final LinkedList<Label> loopEnds = new LinkedList<>(); // 循环结束标签的链表

    private static final LinkedList<MidCode> forUpdateStmts = new LinkedList<>(); // For循环代码更新段
    private static final LinkedList<MidCode> globalCodeList = new LinkedList<>(); // 全局代码列表
    private static final LinkedList<MidCode> midCodeList = new LinkedList<>(); // 函数内代码列表
    private static final LinkedList<MidCode> simMidCodeList = new LinkedList<>(); // 简化后的代码列表
    private static final HashMap<String, LinkedList<Value>> func2valList = new HashMap<>(); // 函数名到变量信息的映射
    private static final HashMap<Value, Integer> val2size = new HashMap<>(); // 变量到大小的映射

    public static MidCodeTable getInstance() {
        return MID_CODE_TABLE; // 返回MidCodeTable的实例
    }

    static {
        func2valList.put("@global", new LinkedList<>()); // 初始化全局函数的变量信息列表
    }

    public Label getLoopBegin() {
        if(loopBegins.isEmpty()) {
            return null;
        }
        return loopBegins.getLast(); // 返回最后一个循环开始标签
    }

    public Label getLoopEnd() {
        if(loopEnds.isEmpty()) {
            return null;
        }
        return loopEnds.getLast(); // 返回最后一个循环结束标签
    }

    public LinkedList<MidCode> getGlobalCodeList() {
        return globalCodeList; // 返回全局代码列表
    }

    public LinkedList<MidCode> getMidCodeList() {
        return midCodeList; // 返回函数内代码列表
    }

    public LinkedList<Value> getValInfos(String func) {
        return func2valList.get(func); // 返回指定函数的变量信息列表
    }

    public int getValSize(Value value) {
        return val2size.get(value); // 返回指定变量的大小
    }

    public MidCode getForUpdateStmt() {
        if(forUpdateStmts.isEmpty()) {
            return null;
        }
        return forUpdateStmts.getLast(); // 返回最后一个For循环代码更新段
    }

    public void setCurFunc(String func) {
        curFunc = func; // 设置当前函数名
        func2valList.putIfAbsent(func, new LinkedList<>()); // 如果函数名不存在，则添加对应的变量信息列表
    }

    public void setLoop(Label loopBegin, Label loopEnd) {
        loopBegins.add(loopBegin); // 添加循环开始标签
        loopEnds.add(loopEnd); // 添加循环结束标签
    }

    public void unsetLoop() {
        loopBegins.removeLast(); // 移除最后一个循环开始标签
        loopEnds.removeLast(); // 移除最后一个循环结束标签
    }

    public void addMidCode(MidCode midCode) {
        if (curFunc.equals("@global")) {
            globalCodeList.add(midCode); // 如果当前函数为全局函数，则将代码添加到全局代码列表中
        } else {
            midCodeList.add(midCode); // 否则将代码添加到函数内代码列表中
        }
    }

    public void addVarInfo(Value value, int size) {
        func2valList.get(curFunc).add(value); // 将变量信息添加到当前函数的变量信息列表中
        val2size.put(value, size); // 将变量和大小的映射关系添加到映射表中
    }

    public void simplify() {
        simplifyNop(); // 简化Nop指令
        simplifyLabel(); // 简化Label指令
        simplifyExp(); // 简化Exp指令
    }

    public void simplifyNop() {
        int index;
        for (MidCode midCode : midCodeList) {
            if (midCode instanceof Nop) {
                index = midCodeList.indexOf(midCode);
                for (Label label : LabelTable.getInstance().getLabelList(midCode)) {
                    label.setMidCode(midCodeList.get(index + 1)); // 将跳转到Nop指令的标签指向下一条指令
                }
                midCodeList.remove(index); // 移除Nop指令
            }
        }
    }

    public void simplifyLabel() {
        int index;
        HashSet<Label> usedLabels = new HashSet<>(); // 用于存储已使用的标签
        for (MidCode midCode : midCodeList) {
            if (midCode instanceof Jump) {
                Jump jump = (Jump) midCode;
                Label target = jump.getLabel().getTarget();
                index = midCodeList.indexOf(jump);
                if (midCodeList.indexOf(target.getMidCode()) - index == 1) {
                    midCodeList.remove(index); // 如果跳转目标紧接着当前指令，则移除跳转指令
                } else {
                    jump.setLabel(target); // 否则更新跳转指令的目标标签
                    usedLabels.add(target); // 将目标标签添加到已使用的标签集合中
                }
            } else if (midCode instanceof Branch) {
                Branch branch = (Branch) midCode;
                Label target = branch.getBranchLabel().getTarget();
                index = midCodeList.indexOf(branch);
                if (midCodeList.indexOf(target.getMidCode()) - index == 1) {
                    midCodeList.remove(index); // 如果跳转目标紧接着当前指令，则移除跳转指令
                } else {
                    branch.setLabel(target); // 否则更新跳转指令的目标标签
                    usedLabels.add(target); // 将目标标签添加到已使用的标签集合中
                }
            }
        }
        LabelTable.getInstance().removeUnusedLabels(usedLabels); // 移除未使用的标签
    }

    public void simplifyExp() {
        for (MidCode midCode : midCodeList) {
            if (midCode instanceof Assign) {
                ((Assign) midCode).simplify(); // 简化赋值指令
            } else {

            }
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (MidCode midCode : globalCodeList) {
            stringJoiner.add(midCode.toString()); // 添加全局代码的字符串表示到字符串拼接器中
        }
        for (MidCode midCode : midCodeList) {
            for (Label label : LabelTable.getInstance().getLabelList(midCode)) {
                stringJoiner.add(label.toString()); // 添加标签的字符串表示到字符串拼接器中
            }
            stringJoiner.add(midCode.toString()); // 添加代码的字符串表示到字符串拼接器中
        }
        return stringJoiner.toString(); // 返回拼接后的字符串
    }
}
