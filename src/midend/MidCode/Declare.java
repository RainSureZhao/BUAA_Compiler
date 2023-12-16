package midend.MidCode;

import java.util.LinkedList;

public class Declare implements MidCode {
    private final boolean isGlobal; // 是否为全局变量
    private final boolean isFinal; // 是否为常量
    private final Value value; // 变量名
    private final int size; // 变量大小
    private final LinkedList<Value> initValues; // 初始化值列表

    public Declare(boolean isGlobal, boolean isFinal, Value value, int size, LinkedList<Value> initValues) {
        this.isGlobal = isGlobal;
        this.isFinal = isFinal;
        this.value = value;
        this.size = size;
        this.initValues = initValues;
        MidCodeTable.getInstance().addMidCode(this);
        MidCodeTable.getInstance().addVarInfo(value, size);
    }

    public Value getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }

    public LinkedList<Value> getInitValues() {
        return initValues;
    }

    @Override
    public String toString() {
        return (isGlobal ? "GLOBAL" : "LOCAL") + " " + (isFinal ? "CONST" : "VAR") + " " + value + " " +
                initValues.stream().map(Value::toString).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
    }
}
