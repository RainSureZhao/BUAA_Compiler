package frontend.SyntaxTree;

import frontend.ErrorHandler.Error;
import frontend.ErrorHandler.ErrorHandler;
import frontend.Lexer.Token;
import frontend.SymbolTable.SymbolTable;
import midend.MidCode.*;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static frontend.SyntaxTree.ExpNode.Type.*;
import static midend.MidCode.BinaryOperate.BinaryOp.ADD;
import static midend.MidCode.BinaryOperate.BinaryOp.MUL;

public class LValNode implements ExpNode {
    private final SymbolTable symbolTable;  // 符号表
    private final Token ident;  // 标识符
    private LinkedList<ExpNode> dimensions;  // 维度列表

    // 构造函数，初始化符号表、标识符和维度列表
    public LValNode(SymbolTable symbolTable, Token ident, LinkedList<ExpNode> dimensions) {
        this.symbolTable = symbolTable;
        this.ident = ident;
        this.dimensions = dimensions;
    }

    // 获取标识符
    public Token getIdent() {
        return ident;
    }

    // 获取维度列表
    public LinkedList<ExpNode> getDimensions() {
        return dimensions;
    }

    // 检查标识符是否在符号表中
    public void checkC() {
        if (symbolTable.getVariable(ident) == null) {
            ErrorHandler.getInstance().addError(new Error("c", ident.getLine()));
        }
    }

    // 检查标识符是否为final
    public void checkH() {
        DefNode variable;
        if ((variable = symbolTable.getVariable(ident)) != null) {
            if (variable.isFinal()) {
                ErrorHandler.getInstance().addError(new Error("h", ident.getLine()));
            }
        }
    }

    // 检查标识符是否在符号表中
    @Override
    public void check() {
        checkC();
    }

    // 获取类型
    @Override
    public Type getType() {
        if (symbolTable.getVariable(ident) == null) {
            return null;
        }
        int dimension = symbolTable.getVariable(ident).getDimensions().size();
        if (dimensions.size() == 0) {
            return dimension == 0 ? INT : dimension == 1 ? ARRAY : MATRIX;
        } else if (dimensions.size() == 1) {
            return dimension == 1 ? INT : ARRAY;
        } else {
            return INT;
        }
    }

    // 计算维度
    public LValNode compute() {
        dimensions = dimensions.stream().map(ExpNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        return this;
    }

    // 简化维度
    @Override
    public ExpNode simplify() {
        dimensions = dimensions.stream().map(ExpNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        if (dimensions.stream().allMatch(item -> item instanceof NumberNode)) {
            DefNode defNode = symbolTable.getVariable(ident);
            if(defNode == null) {
                return this;
            }
            if (defNode.isFinal() && dimensions.size() == defNode.getDimensions().size()) {
                return defNode.getValue(dimensions);
            }
        }
        return this;
    }

    // 生成中间代码
    @Override
    public Value generateMidCode() {
        if(symbolTable.getVariable(ident) == null) {
            return null;
        }
        DefNode defNode = symbolTable.getVariable(ident).simplify();
        int id = defNode.getId();
        LinkedList<ExpNode> dimensions = defNode.getDimensions();
        if (dimensions.size() == 0) {
            Word value = new Word();
            new Move(true, value, new Word(ident.getStringValue() + "@" + id));
            return value;
        } else if (dimensions.size() == 1) {
            if (this.dimensions.size() == 0) {
                Addr value = new Addr();
                new Move(true, value, new Addr(ident.getStringValue() + "@" + id));
                return value;
            } else {
                Value offsetValue = this.dimensions.get(0).generateMidCode();
                Addr addr = new Addr();
                new Assign(true, addr, new BinaryOperate(ADD,
                        new Addr(ident.getStringValue() + "@" + id), offsetValue));
                Word value = new Word();
                new Load(true, value, addr);
                return value;
            }
        } else {
            if (this.dimensions.size() == 0) {
                Addr value = new Addr();
                new Move(true, value, new Addr(ident.getStringValue() + "@" + id));
                return value;
            } else if (this.dimensions.size() == 1) {
                Value offsetValue = this.dimensions.get(0).generateMidCode();
                Word offset = new Word();
                new Assign(true, offset, new BinaryOperate(MUL,
                        offsetValue, new Imm(((NumberNode) dimensions.get(1)).getValue())));
                Addr value = new Addr();
                new Assign(true, value, new BinaryOperate(ADD,
                        new Addr(ident.getStringValue() + "@" + id), offset));
                return value;
            } else {
                Value rowOffsetValue = this.dimensions.get(0).generateMidCode();
                Word rowOffset = new Word();
                new Assign(true, rowOffset, new BinaryOperate(MUL,
                        rowOffsetValue, new Imm(((NumberNode) dimensions.get(1)).getValue())));
                Value colOffsetValue = this.dimensions.get(1).generateMidCode();
                Word colIndex = new Word();
                new Assign(true, colIndex, new BinaryOperate(ADD, colOffsetValue, rowOffset));
                Addr addr = new Addr();
                new Assign(true, addr, new BinaryOperate(ADD,
                        new Addr(ident.getStringValue() + "@" + id), colIndex));
                Word value = new Word();
                new Load(true, value, addr);
                return value;
            }
        }
    }
}