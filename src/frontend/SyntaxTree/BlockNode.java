package frontend.SyntaxTree;

import frontend.ErrorHandler.Error;
import frontend.ErrorHandler.ErrorHandler;
import frontend.Lexer.Token;
import frontend.SymbolTable.SymbolTable;
import midend.MidCode.Value;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static frontend.Lexer.TypeCode.INTTK;

public class BlockNode implements StmtNode {
    private final SymbolTable symbolTable; // 符号表
    private LinkedList<BlockItemNode> blockItemNodes; // 块节点列表
    private final int endLine; // 块节点结束行号

    public BlockNode(SymbolTable symbolTable, LinkedList<BlockItemNode> blockItemNodes, int line) {
        this.symbolTable = symbolTable;
        this.blockItemNodes = blockItemNodes;
        this.endLine = line;
    }

    public void check(Token funcDefType) {
        if (funcDefType.isType(INTTK)) { // 如果函数定义类型是int
            if (blockItemNodes.size() == 0) { // 如果块节点列表为空
                ErrorHandler.getInstance().addError(new Error("g", endLine)); // 添加错误信息
            } else if (!(blockItemNodes.getLast() instanceof ReturnNode)) { // 如果块节点列表的最后一个节点不是返回节点
                ErrorHandler.getInstance().addError(new Error("g", endLine)); // 添加错误信息
            }
        } else { // 如果函数定义类型不是int
            for (BlockItemNode blockItemNode : blockItemNodes) {
                if (blockItemNode instanceof BlockNode) { // 如果块节点是块节点
                    ((BlockNode) blockItemNode).check(funcDefType); // 递归检查块节点
                } else if (blockItemNode instanceof BranchNode) { // 如果块节点是分支节点
                    ((BranchNode) blockItemNode).check(funcDefType); // 检查分支节点
                } else if (blockItemNode instanceof LoopNode) { // 如果块节点是循环节点
                    ((LoopNode) blockItemNode).check(funcDefType); // 检查循环节点
                } else if (blockItemNode instanceof ReturnNode && ((ReturnNode) blockItemNode).hasReturnValue()) { // 如果块节点是返回节点且有返回值
                    ErrorHandler.getInstance().addError(new Error("f", ((ReturnNode) blockItemNode).getLine())); // 添加错误信息
                }
            }
        }
    }

    public void complete() {
        if (blockItemNodes.size() == 0 || !(blockItemNodes.getLast() instanceof ReturnNode)) { // 如果块节点列表为空或者最后一个节点不是返回节点
            blockItemNodes.add(new ReturnNode(symbolTable, null, null)); // 添加一个返回节点
        }
    }

    @Override
    public BlockNode simplify() {
        blockItemNodes = blockItemNodes.stream().map(BlockItemNode::simplify).collect(Collectors.toCollection(LinkedList::new)); // 简化块节点列表中的每个节点
        return this;
    }

    @Override
    public Value generateMidCode() {
        blockItemNodes.forEach(BlockItemNode::generateMidCode); // 生成中间代码
        return null;
    }
}
