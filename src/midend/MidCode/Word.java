package midend.MidCode;

public class Word extends Value {
    private final String ident; // 标识符

    public Word(String ident) { // 构造函数，接受一个标识符参数
        this.ident = ident; // 将参数赋值给成员变量
    }

    public Word() { // 无参构造函数
        this.ident = String.valueOf(tempCount++); // 将 tempCount 的值转换为字符串并赋值给成员变量
    }

    public String getIdent() { // 获取标识符的方法
        return ident; // 返回标识符
    }

    @Override
    public String toString() { // 重写 toString 方法
        return "$" + ident; // 返回以 "$" 开头的标识符字符串
    }
}
