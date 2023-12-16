package midend.MidCode;

import static midend.MidCode.UnaryOperate.UnaryOp.POS;

public class Assign implements MidCode {
    private final boolean isTemp; // 是否为临时变量
    private final Value leftValue; // 赋值的左值
    private final Operate rightValue; // 赋值的右值

    public Assign(boolean isTemp, Value leftValue, Operate rightValue) {
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

    public Operate getRightValue() {
        return rightValue;
    }

    public void simplify() {
    }

    @Override
    public String toString() {
        return leftValue + " <- " + rightValue;
    }
}
