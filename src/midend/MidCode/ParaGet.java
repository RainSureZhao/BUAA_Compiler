package midend.MidCode;

public class ParaGet implements MidCode {
    private final Value value; // 值

    public ParaGet(Value value) {
        this.value = value; // 初始化值
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到中间代码表中
        MidCodeTable.getInstance().addVarInfo(value, 1); // 将值添加到变量信息表中，作为参数
    }

    public Value getValue() {
        return value; // 返回值
    }

    @Override
    public String toString() {
        return "PARA " + value; // 返回字符串表示形式
    }
}
