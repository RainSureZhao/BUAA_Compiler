package midend.MidCode;

public class Return implements MidCode {
    private Value value = null;

    public Return() {
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到MidCodeTable中
    }

    public Return(Value value) {
        this.value = value;
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到MidCodeTable中
    }

    public Value getValue() {
        return value; // 返回value属性
    }

    @Override
    public String toString() {
        return value == null ? "RETURN" : "RETURN " + value; // 返回表示RETURN语句的字符串
    }
}
