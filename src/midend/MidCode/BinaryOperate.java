package midend.MidCode;

public class BinaryOperate implements Operate {
    // 定义枚举类型BinaryOp，表示二元操作符
    public enum BinaryOp {
        ADD, SUB, MUL, DIV, MOD, AND, OR, SLL, GT, GE, LT, LE, EQ, NE, BITAND
    }

    private final BinaryOp binaryOp; // 二元操作符
    private final Value leftValue; // 左操作数
    private final Value rightValue; // 右操作数

    public BinaryOperate(BinaryOp binaryOp, Value leftValue, Value rightValue) {
        this.binaryOp = binaryOp;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public BinaryOp getBinaryOp() {
        return binaryOp;
    }

    public Value getLeftValue() {
        return leftValue;
    }

    public Value getRightValue() {
        return rightValue;
    }

    @Override
    public String toString() {
        return leftValue.toString() + " " + binaryOp.toString() + " " + rightValue.toString();
    }
}
