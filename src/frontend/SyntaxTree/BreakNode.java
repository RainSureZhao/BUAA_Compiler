package frontend.SyntaxTree;

import frontend.SymbolTable.SymbolTable;
import midend.MidCode.Jump;
import midend.MidCode.MidCodeTable;
import midend.MidCode.Value;

public class BreakNode implements StmtNode {
    private final SymbolTable symbolTable; // 符号表

    public BreakNode(SymbolTable symbolTable) {
        this.symbolTable = symbolTable; // 初始化符号表
    }

    @Override
    public BreakNode simplify() {
        return this; // 简化当前节点
    }

    @Override
    public Value generateMidCode() {
        new Jump(MidCodeTable.getInstance().getLoopEnd()); // 生成中间代码，跳转到循环结束位置
        return null; // 返回空值
    }
}
