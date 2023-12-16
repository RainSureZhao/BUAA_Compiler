package frontend.Lexer;

import frontend.Parser.ParsedUnit;

import java.util.Collections;
import java.util.HashMap;

import static frontend.Lexer.TypeCode.*;
import static frontend.Lexer.TypeCode.BITAND;

public class Token extends ParsedUnit {
    private final TypeCode typeCode; // Token的类型码
    private String stringValue; // 字符串类型的值
    private int intValue; // 整数类型的值
    private final int line; // Token所在的行号
    private static final HashMap<TypeCode, String> code2str = new HashMap<>(); // 类型码到字符串的映射

    static {
        code2str.put(BITAND, "bitand"); // 将类型码BITAND映射为字符串"bitand"
        code2str.put(MAINTK, "main"); // 将类型码MAINTK映射为字符串"main"
        code2str.put(CONSTTK, "const"); // 将类型码CONSTTK映射为字符串"const"
        code2str.put(INTTK, "int"); // 将类型码INTTK映射为字符串"int"
        code2str.put(BREAKTK, "break"); // 将类型码BREAKTK映射为字符串"break"
        code2str.put(CONTINUETK, "continue"); // 将类型码CONTINUETK映射为字符串"continue"
        code2str.put(IFTK, "if"); // 将类型码IFTK映射为字符串"if"
        code2str.put(ELSETK, "else"); // 将类型码ELSETK映射为字符串"else"
        code2str.put(FORTK, "for"); // 将类型码FORTK映射为字符串"for"
        code2str.put(WHILETK, "while"); // 将类型码WHILETK映射为字符串"while"
        code2str.put(GETINTTK, "getint"); // 将类型码GETINTTK映射为字符串"getint"
        code2str.put(PRINTFTK, "printf"); // 将类型码PRINTFTK映射为字符串"printf"
        code2str.put(RETURNTK, "return"); // 将类型码RETURNTK映射为字符串"return"
        code2str.put(VOIDTK, "void"); // 将类型码VOIDTK映射为字符串"void"
        code2str.put(AND, "&&"); // 将类型码AND映射为字符串"&&"
        code2str.put(OR, "||"); // 将类型码OR映射为字符串"||"
        code2str.put(LEQ, "<="); // 将类型码LEQ映射为字符串"<="
        code2str.put(GEQ, ">="); // 将类型码GEQ映射为字符串">="
        code2str.put(EQL, "=="); // 将类型码EQL映射为字符串"=="
        code2str.put(NEQ, "!="); // 将类型码NEQ映射为字符串"!="
        code2str.put(PLUS, "+"); // 将类型码PLUS映射为字符串"+"
        code2str.put(MINU, "-"); // 将类型码MINU映射为字符串"-"
        code2str.put(MULT, "*"); // 将类型码MULT映射为字符串"*"
        code2str.put(DIV, "/"); // 将类型码DIV映射为字符串"/"
        code2str.put(MOD, "%"); // 将类型码MOD映射为字符串"%"
        code2str.put(SEMICN, ";"); // 将类型码SEMICN映射为字符串";"
        code2str.put(COMMA, ","); // 将类型码COMMA映射为字符串","
        code2str.put(LPARENT, "("); // 将类型码LPARENT映射为字符串"("
        code2str.put(RPARENT, ")"); // 将类型码RPARENT映射为字符串")"
        code2str.put(LBRACK, "["); // 将类型码LBRACK映射为字符串"["
        code2str.put(RBRACK, "]"); // 将类型码RBRACK映射为字符串"]"
        code2str.put(LBRACE, "{"); // 将类型码LBRACE映射为字符串"{"
        code2str.put(RBRACE, "}"); // 将类型码RBRACE映射为字符串"}"
        code2str.put(NOT, "!"); // 将类型码NOT映射为字符串"!"
        code2str.put(LSS, "<"); // 将类型码LSS映射为字符串"<"
        code2str.put(GRE, ">"); // 将类型码GRE映射为字符串">"
        code2str.put(ASSIGN, "="); // 将类型码ASSIGN映射为字符串"="
    }

    public Token(TypeCode typeCode, int line) {
        super(String.valueOf(typeCode), Collections.emptyList());
        this.typeCode = typeCode;
        this.line = line;
    }

    public Token(TypeCode typeCode, String stringValue, int line) {
        super(String.valueOf(typeCode), Collections.emptyList());
        this.typeCode = typeCode;
        this.stringValue = stringValue;
        this.line = line;
    }

    public Token(TypeCode typeCode, int intValue, int line) {
        super(String.valueOf(typeCode), Collections.emptyList());
        this.typeCode = typeCode;
        this.intValue = intValue;
        this.line = line;
    }

    public TypeCode getTypeCode() {
        return typeCode;
    }

    public boolean isType(TypeCode typeCode) {
        return this.typeCode == typeCode;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        if (typeCode == IDENFR || typeCode == STRCON) {
            return typeCode + " " + stringValue;
        } else if (typeCode == INTCON) {
            return typeCode + " " + intValue;
        } else {
            return typeCode + " " + code2str.get(typeCode);
        }
    }
}
