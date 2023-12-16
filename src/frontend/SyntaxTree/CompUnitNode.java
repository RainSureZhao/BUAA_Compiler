package frontend.SyntaxTree;

import frontend.SymbolTable.SymbolTable;
import midend.MidCode.*;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class CompUnitNode implements SyntaxNode {
    private final SymbolTable symbolTable; // 符号表
    private LinkedList<DeclNode> declNodes; // 声明节点列表
    private LinkedList<FuncDefNode> funcDefNodes; // 函数定义节点列表
    private FuncDefNode mainFuncDefNode; // 主函数定义节点

    public CompUnitNode(SymbolTable symbolTable, LinkedList<DeclNode> declNodes,
                        LinkedList<FuncDefNode> funcDefNodes, FuncDefNode mainFuncDefNode) {
        this.symbolTable = symbolTable;
        this.declNodes = declNodes;
        this.funcDefNodes = funcDefNodes;
        this.mainFuncDefNode = mainFuncDefNode;
    }

    @Override
    public CompUnitNode simplify() {
        // 简化声明节点列表
        declNodes = declNodes.stream().map(DeclNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        // 简化函数定义节点列表
        funcDefNodes = funcDefNodes.stream().map(FuncDefNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        // 简化主函数定义节点
        mainFuncDefNode = mainFuncDefNode.simplify();
        return this;
    }

    @Override
    public Value generateMidCode() {
        // 生成声明节点的中间代码
        declNodes.forEach(DeclNode::generateMidCode);
        // 设置当前函数为"main"
        MidCodeTable.getInstance().setCurFunc("main");
        // 调用"main"函数
        new FuncCall("main");
        // 生成退出中间代码
        new Exit();
        // 生成主函数定义节点的中间代码
        mainFuncDefNode.generateMidCode();
        // 生成函数定义节点列表的中间代码
        funcDefNodes.forEach(FuncDefNode::generateMidCode);
        return null;
    }
}
