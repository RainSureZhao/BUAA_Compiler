package midend.MidCode;

public class Addr extends Value {
    private final String ident; // 标识符

    public Addr(String ident) { // 构造函数，接受一个标识符参数
        this.ident = ident; // 将参数赋值给成员变量
    }

    public Addr() { // 无参构造函数
        this.ident = String.valueOf(tempCount++); // 将 tempCount 的值转换为字符串并赋值给成员变量
    }

    public boolean isTemp() { // 判断是否为临时变量
        return Character.isDigit(ident.charAt(0)); // 判断标识符的第一个字符是否为数字
    }

    @Override
    public String getIdent() { // 获取标识符
        return ident; // 返回标识符
    }

    @Override
    public String toString() { // 转换为字符串表示
        return "&" + ident; // 返回以 "&" 开头的标识符字符串
    }
}
