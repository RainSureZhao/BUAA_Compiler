package midend.MidCode;

public class Load implements MidCode {
    private final boolean isTemp; // 是否为临时变量
    private final Value leftValue; // 左值
    private final Addr rightValue; // 右值

    public Load(boolean isTemp, Value leftValue, Addr rightValue) {
        this.isTemp = isTemp;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        MidCodeTable.getInstance().addMidCode(this);
        if (isTemp) {
            MidCodeTable.getInstance().addVarInfo(leftValue, 1);
        }
    }

    public boolean isTemp() {
        return isTemp;
    }

    public Value getLeftValue() {
        return leftValue;
    }

    public Addr getRightValue() {
        return rightValue;
    }

    @Override
    public String toString() {
        return (isTemp ? "TEMP " : "SAVE ") + leftValue + " <- *" + rightValue;
    }
}
