package frontend.SyntaxTree;

import frontend.ErrorHandler.Error;
import frontend.ErrorHandler.ErrorHandler;
import frontend.Lexer.Token;
import frontend.SymbolTable.SymbolTable;
import midend.MidCode.*;
import midend.LabelTable.Label;

import static frontend.Lexer.TypeCode.AND;
import static frontend.Lexer.TypeCode.OR;

public class BranchNode implements StmtNode {
    private final SymbolTable symbolTable; // 符号表
    private final ExpNode cond; // 条件表达式
    private final StmtNode thenStmt; // 条件为真时执行的语句
    private final StmtNode elseStmt; // 条件为假时执行的语句

    public BranchNode(SymbolTable symbolTable, ExpNode cond, StmtNode thenStmt, StmtNode elseStmt) {
        this.symbolTable = symbolTable;
        this.cond = cond;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public void check(Token funcDefType) {
        if (thenStmt instanceof BlockNode) { // 如果条件为真时执行的语句是一个代码块
            ((BlockNode) thenStmt).check(funcDefType); // 对代码块进行语义检查
        } else if (thenStmt instanceof ReturnNode && ((ReturnNode) thenStmt).hasReturnValue()) { // 如果条件为真时执行的语句是一个返回语句且有返回值
            ErrorHandler.getInstance().addError(new Error("f", ((ReturnNode) thenStmt).getLine())); // 报错：返回语句不能在条件语句中有返回值
        }
        if (elseStmt instanceof BlockNode) { // 如果条件为假时执行的语句是一个代码块
            ((BlockNode) elseStmt).check(funcDefType); // 对代码块进行语义检查
        } else if (elseStmt instanceof ReturnNode && ((ReturnNode) elseStmt).hasReturnValue()) { // 如果条件为假时执行的语句是一个返回语句且有返回值
            ErrorHandler.getInstance().addError(new Error("f", ((ReturnNode) elseStmt).getLine())); // 报错：返回语句不能在条件语句中有返回值
        }
    }

    @Override
    public StmtNode simplify() {
        ExpNode simCond = cond.simplify(); // 简化条件表达式
        StmtNode simThen = thenStmt.simplify(); // 简化条件为真时执行的语句
        StmtNode simElse = elseStmt == null ? null : elseStmt.simplify(); // 简化条件为假时执行的语句（如果存在）
        if (simCond instanceof NumberNode) { // 如果条件表达式是一个数字节点
            return ((NumberNode) simCond).getValue() == 0 ? simElse == null ? new NopNode() : simElse : simThen; // 如果条件为0，则返回条件为假时执行的语句（如果存在），否则返回条件为真时执行的语句
        } else if (simCond instanceof BinaryExpNode) { // 如果条件表达式是一个二元表达式节点
            if (((BinaryExpNode) simCond).getBinaryOp().isType(AND)) { // 如果二元操作符是AND
                StmtNode newThen = new BranchNode(symbolTable, ((BinaryExpNode) simCond).getRightExp(), simThen, simElse).simplify(); // 递归简化右子表达式为条件为真时执行的语句，得到新的条件为真时执行的语句
                return new BranchNode(symbolTable, ((BinaryExpNode) simCond).getLeftExp(), newThen, simElse).simplify(); // 递归简化左子表达式为条件表达式，得到新的条件为真时执行的语句
            } else if (((BinaryExpNode) simCond).getBinaryOp().isType(OR)) { // 如果二元操作符是OR
                StmtNode newElse = new BranchNode(symbolTable, ((BinaryExpNode) simCond).getRightExp(), simThen, simElse).simplify(); // 递归简化右子表达式为条件为假时执行的语句，得到新的条件为假时执行的语句
                return new BranchNode(symbolTable, ((BinaryExpNode) simCond).getLeftExp(), simThen, newElse).simplify(); // 递归简化左子表达式为条件表达式，得到新的条件为假时执行的语句
            }
        }
        return new BranchNode(symbolTable, simCond, simThen, simElse); // 返回简化后的条件语句节点
    }

    @Override
    public Value generateMidCode() {
        Label thenEndLabel = new Label(); // 创建一个标签，用于条件为真时执行的语句结束后跳转到条件为假时执行的语句
        Value condValue = cond.generateMidCode(); // 生成条件表达式的中间代码，得到条件的值
        new Branch(Branch.BranchOp.EQ, condValue, new Imm(0), thenEndLabel); // 生成条件为真时跳转到thenEndLabel的中间代码
        thenStmt.generateMidCode(); // 生成条件为真时执行的语句的中间代码
        if (elseStmt == null) { // 如果条件为假时执行的语句不存在
            Nop thenEnd = new Nop(); // 创建一个空操作
            thenEndLabel.setMidCode(thenEnd); // 将thenEnd设置为thenEndLabel的中间代码
        } else {
            Label elseEndLabel = new Label(); // 创建一个标签，用于条件为假时执行的语句结束后跳转到条件语句结束的位置
            new Jump(elseEndLabel); // 生成跳转到elseEndLabel的中间代码
            Nop thenEnd = new Nop(); // 创建一个空操作，用于标记条件为真时执行的语句结束的位置
            elseStmt.generateMidCode(); // 生成条件为假时执行的语句的中间代码
            Nop elseEnd = new Nop(); // 创建一个空操作，用于标记条件语句结束的位置
            thenEndLabel.setMidCode(thenEnd); // 将thenEnd设置为thenEndLabel的中间代码
            elseEndLabel.setMidCode(elseEnd); // 将elseEnd设置为elseEndLabel的中间代码
        }
        return null; // 返回空值
    }
}
