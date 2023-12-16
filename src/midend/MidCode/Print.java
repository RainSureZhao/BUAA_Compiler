package midend.MidCode;

public class Print implements MidCode {
    private final String formatString; // 格式化字符串

    public Print(String formatString) {
        this.formatString = formatString; // 初始化格式化字符串
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到中间代码表中
    }

    public String getFormatString() {
        return formatString; // 返回格式化字符串
    }

    @Override
    public String toString() {
        return "PRINT " + formatString; // 返回打印语句的字符串表示形式
    }
}
