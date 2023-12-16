package frontend.ErrorHandler;

public class Error {
    private final String errorCode; // 错误代码
    private final int line; // 行号

    public Error(String errorCode, int line) {
        this.errorCode = errorCode;
        this.line = line;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getLine() {
        return line;
    }
}
