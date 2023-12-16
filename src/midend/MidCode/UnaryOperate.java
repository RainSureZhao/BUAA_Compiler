package midend.MidCode;

public class UnaryOperate implements Operate {
    public enum UnaryOp {
        POS, NEG, NOT
    }

    private final UnaryOp unaryOp; // 一元操作符类型
    private final Value value; // 操作数

    public UnaryOperate(UnaryOp unaryOp, Value value) {
        this.unaryOp = unaryOp;
        this.value = value;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return unaryOp.toString() + " " + value.toString();
    }
}
