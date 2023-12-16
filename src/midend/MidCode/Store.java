package midend.MidCode;

public class Store implements MidCode {
    private final Addr leftValue; // 左值地址
    private final Value rightValue; // 右值

    public Store(Addr leftValue, Value rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        MidCodeTable.getInstance().addMidCode(this);
    }

    public Addr getLeftValue() {
        return leftValue;
    }

    public Value getRightValue() {
        return rightValue;
    }

    @Override
    public String toString() {
        return "*" + leftValue + " <- " + rightValue; // 返回左值地址和右值的字符串表示
    }
}
