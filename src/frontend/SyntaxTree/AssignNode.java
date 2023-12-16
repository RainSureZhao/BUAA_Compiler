package frontend.SyntaxTree;

import frontend.SymbolTable.SymbolTable;
import midend.MidCode.*;

import java.util.LinkedList;

import static midend.MidCode.BinaryOperate.BinaryOp.ADD;
import static midend.MidCode.BinaryOperate.BinaryOp.MUL;

public class AssignNode implements StmtNode {
    private final SymbolTable symbolTable;
    private LValNode lValNode;
    private ExpNode expNode;

    public AssignNode(SymbolTable symbolTable, LValNode lValNode, ExpNode toExpNode) {
        this.symbolTable = symbolTable;
        this.lValNode = lValNode;
        this.expNode = toExpNode;
    }

    @Override
    public AssignNode simplify() {
        // 对左值节点进行简化
        lValNode = lValNode.compute();
        // 对表达式节点进行简化
        expNode = expNode.simplify();
        return this;
    }

    @Override
    public Value generateMidCode() {
        // 获取左值节点对应的定义节点
        DefNode defNode = symbolTable.getVariable(lValNode.getIdent());
        if(defNode == null) {
            return null;
        }
        defNode.simplify();
        // 获取定义节点的id
        int id = defNode.getId();
        // 获取定义节点的维度列表
        LinkedList<ExpNode> dimensions = defNode.getDimensions();
        // 生成表达式节点的中间代码
        Value expValue = expNode.generateMidCode();
        if (dimensions.isEmpty()) {
            // 如果维度列表为空，则表示是单个变量
            // 创建一个表示变量的中间代码单词
            Word value = new Word(lValNode.getIdent().getStringValue() + "@" + id);
            // 创建一个赋值中间代码
            new Move(false, value, expValue);
            return null;
        } else if (dimensions.size() == 1) {
            // 如果维度列表长度为1，则表示是一维数组
            // 获取第一个维度的偏移量
            if(lValNode.getDimensions().isEmpty()) {
                return null;
            }
            Value offset = lValNode.getDimensions().get(0).generateMidCode();
            // 创建一个地址中间代码
            Addr addr = new Addr();
            // 创建一个赋值中间代码
            new Assign(true, addr, new BinaryOperate(ADD,
                    new Addr(lValNode.getIdent().getStringValue() + "@" + id), offset));
            // 创建一个存储中间代码
            new Store(addr, expValue);
            return null;
        } else {
            // 如果维度列表长度大于1，则表示是多维数组
            // 获取第一维度的值
            if(lValNode.getDimensions().isEmpty()) {
                return null;
            }
            Value rowValue = lValNode.getDimensions().get(0).generateMidCode();
            // 创建一个表示行索引的中间代码单词
            Word rowIndex = new Word();
            // 创建一个赋值中间代码
            // 判断类型是不是BinaryExpNode
            if(defNode.getDimensions().get(1) instanceof BinaryExpNode) {
                BinaryExpNode temp = (BinaryExpNode) defNode.getDimensions().get(1);
                new Assign(true, rowIndex, new BinaryOperate(MUL, rowValue,
                        new Imm(((NumberNode) temp.fullyCalculate(temp.getBinaryOp(), (NumberNode) temp.getLeftExp(), (NumberNode)temp.getRightExp())).getValue())));
            } else {
                new Assign(true, rowIndex, new BinaryOperate(MUL, rowValue,
                        new Imm(((NumberNode) defNode.getDimensions().get(1)).getValue())));
            }

            // 获取第二维度的值
            Value colValue = lValNode.getDimensions().get(1).generateMidCode();
            // 创建一个表示列偏移量的中间代码单词
            Word colOffset = new Word();
            // 创建一个赋值中间代码
            new Assign(true, colOffset, new BinaryOperate(ADD, rowIndex, colValue));
            // 创建一个地址中间代码
            Addr addr = new Addr();
            // 创建一个赋值中间代码
            new Assign(true, addr, new BinaryOperate(ADD,
                    new Addr(lValNode.getIdent().getStringValue() + "@" + id), colOffset));
            // 创建一个存储中间代码
            new Store(addr, expValue);
            return null;
        }
    }
}
