package midend.MidCode;

public class IntGet implements MidCode {
    public IntGet() {
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到MidCodeTable中
    }

    @Override
    public String toString() {
        return "CALL GETINT"; // 返回字符串 "CALL GETINT"
    }
}
