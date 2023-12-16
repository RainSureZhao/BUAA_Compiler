package midend.MidCode;

public class ArgPush implements MidCode {
    private final Value value; // 值

    public ArgPush(Value value) {
        this.value = value; // 设置值
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到中间代码表中
    }

    public Value getValue() {
        return value; // 返回值
    }

    @Override
    public String toString() {
        return "PUSH " + value; // 返回"PUSH"加上值的字符串表示形式
    }
}
