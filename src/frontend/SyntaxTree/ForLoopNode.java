package frontend.SyntaxTree;

import frontend.SymbolTable.SymbolTable;
import midend.LabelTable.Label;
import midend.MidCode.*;

public class ForLoopNode implements StmtNode {
    private StmtNode initStmt;
    private ExpNode loopCond;
    private StmtNode updateStmt;
    private StmtNode loopStmt;

    public ForLoopNode(SymbolTable symbolTable, StmtNode initStmt, ExpNode loopCond, StmtNode updateStmt, StmtNode loopStmt) {
        this.initStmt = initStmt;
        this.loopCond = loopCond;
        this.updateStmt = updateStmt;
        this.loopStmt = loopStmt;
    }

    public StmtNode getInitStmt() {
        return initStmt;
    }

    public ExpNode getLoopCond() {
        return loopCond;
    }

    public StmtNode getUpdateStmt() {
        return updateStmt;
    }

    public StmtNode getLoopStmt() {
        return loopStmt;
    }

    // TODO: Implement the method to generate mid code for this node
    @Override
    public Value generateMidCode() {
        // Generate mid code for initStmt
        if (initStmt != null) {
            initStmt.generateMidCode();
        }

        // Create labels for the loop
        Label loopBeginLabel = new Label();
        Label loopEndLabel = new Label();

        MidCodeTable.getInstance().setLoop(loopBeginLabel, loopEndLabel);

        // Generate mid code for loopCond
        Nop loopBegin = new Nop();
        loopBeginLabel.setMidCode(loopBegin);
        if (loopCond != null) {
            Value condValue = loopCond.generateMidCode();
            new Branch(Branch.BranchOp.EQ, condValue, new Imm(0), loopEndLabel);
        }

        // Generate mid code for loopStmt
        if (loopStmt != null) {
            loopStmt.generateMidCode();
        }

        // Generate mid code for updateStmt
        if (updateStmt != null) {
            updateStmt.generateMidCode();
        }

        // Jump back to the beginning of the loop
        new Jump(loopBeginLabel);

        // Mark the end of the loop
        Nop loopEnd = new Nop();
        loopEndLabel.setMidCode(loopEnd);

        MidCodeTable.getInstance().unsetLoop();
        return null;
    }

    @Override
    public StmtNode simplify() {
        return this;
    }
}