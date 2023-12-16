package frontend.SyntaxTree;

import frontend.Lexer.Token;
import frontend.SymbolTable.SymbolTable;
import midend.MidCode.*;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class DefNode implements SyntaxNode {
    protected final SymbolTable symbolTable; // 符号表
    protected final boolean isFinal; // 是否为常量
    protected final Token ident; // 标识符
    protected LinkedList<ExpNode> dimensions; // 数组维度
    protected LinkedList<ExpNode> initValues; // 初始化值

    public DefNode(SymbolTable symbolTable, boolean isFinal, Token ident,
                   LinkedList<ExpNode> dimensions, LinkedList<ExpNode> initValues) {
        this.symbolTable = symbolTable;
        this.isFinal = isFinal;
        this.ident = ident;
        this.dimensions = dimensions;
        this.initValues = initValues;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public Token getIdent() {
        return ident;
    }

    public LinkedList<ExpNode> getDimensions() {
        return dimensions;
    }

    public NumberNode getValue(LinkedList<ExpNode> dimensions) {
        simplify(); // 简化表达式
        if (initValues.size() == 0) {
            return new NumberNode(0);
        }
        if (dimensions.size() == 0) {
            return (NumberNode) initValues.get(0);
        } else if (dimensions.size() == 1) {
            return (NumberNode) initValues.get(((NumberNode) dimensions.get(0)).getValue());
        } else {
            return (NumberNode) initValues.get(((NumberNode) dimensions.get(0)).getValue() *
                    ((NumberNode) this.dimensions.get(1)).getValue() + ((NumberNode) dimensions.get(1)).getValue());
        }
    }

    public int getId() {
        return symbolTable.getId();
    }

    @Override
    public DefNode simplify() {
        dimensions = dimensions.stream().map(ExpNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        initValues = initValues.stream().map(ExpNode::simplify).collect(Collectors.toCollection(LinkedList::new));
        return this;
    }

    @Override
    public Value generateMidCode() {
        boolean isGlobal = symbolTable.getParent() == null; // 是否为全局变量
        LinkedList<Value> values = new LinkedList<>(); // 初始化值的中间代码
        initValues.forEach(initValue -> values.add(initValue.generateMidCode()));
        int size = dimensions.size() == 0 ? 1 : dimensions.size() == 1 ?
                ((NumberNode) dimensions.get(0)).getValue() :
                ((NumberNode) dimensions.get(0)).getValue() *
                        ((NumberNode) dimensions.get(1)).getValue();
        if (dimensions.size() == 0) {
            Word value = new Word(ident.getStringValue() + "@" + symbolTable.getId()); // 变量名
            new Declare(isGlobal, isFinal, value, size, values); // 声明变量的中间代码
        } else {
            Addr value = new Addr(ident.getStringValue() + "@" + symbolTable.getId()); // 变量名
            new Declare(isGlobal, isFinal, value, size, values); // 声明变量的中间代码
        }
        return null;
    }
}
