package midend.MidCode;

import midend.LabelTable.Label;

public class Branch implements MidCode {
    public enum BranchOp {
        GT, GE, LT, LE, EQ, NE
    }

    private final BranchOp branchOp; // 分支操作类型
    private final Value leftValue; // 左操作数
    private final Value rightValue; // 右操作数
    private Label branchLabel; // 分支目标标签

    public Branch(BranchOp branchOp, Value leftValue, Value rightValue, Label branchLabel) {
        this.branchOp = branchOp;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.branchLabel = branchLabel;
        MidCodeTable.getInstance().addMidCode(this);
    }

    public BranchOp getBranchOp() {
        return branchOp;
    }

    public Value getLeftValue() {
        return leftValue;
    }

    public Label getBranchLabel() {
        return branchLabel;
    }

    public void setLabel(Label target) {
        this.branchLabel = target;
    }

    @Override
    public String toString() {
        return "BRANCH " + branchLabel + " IF " + leftValue + " " + branchOp + " " + rightValue;
    }
}
