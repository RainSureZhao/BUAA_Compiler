package midend.MidCode;

public class FuncCall implements MidCode {
    public final String ident; // 函数标识符

    public FuncCall(String ident) {
        this.ident = ident; // 初始化函数标识符
        MidCodeTable.getInstance().addMidCode(this); // 将当前实例添加到中间代码表中
    }

    public String getIdent() {
        return ident; // 获取函数标识符
    }

    @Override
    public String toString() {
        return "CALL " + ident; // 返回调用函数的字符串表示
    }
}
