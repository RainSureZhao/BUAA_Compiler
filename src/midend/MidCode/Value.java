package midend.MidCode;

public abstract class Value {
    public static int tempCount = 0; // 静态变量，用于计数临时变量的数量

    public abstract String getIdent(); // 抽象方法，子类需要实现该方法来获取标识符

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Value) { // 判断对象是否为Value类型的实例
            return toString().equals(obj.toString()); // 调用toString方法比较两个对象的字符串表示是否相等
        } else {
            return false;
        }
    }
    @Override public int hashCode()
    {
        return toString().hashCode(); // 返回对象的字符串表示的哈希码
    }
}
