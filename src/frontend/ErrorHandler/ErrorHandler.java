package frontend.ErrorHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class ErrorHandler {
    private static final ErrorHandler STDERR = new ErrorHandler(); // 创建一个静态的ErrorHandler实例，用于标准错误处理
    private final HashMap<Integer, Error> errorMap = new HashMap<>(); // 创建一个HashMap用于存储错误信息

    public static ErrorHandler getInstance() { // 获取ErrorHandler实例的静态方法
        return STDERR;
    }

    public void addError(Error error) { // 添加错误信息到errorMap中
        errorMap.putIfAbsent(error.getLine(), error);
    }

    public void log(BufferedWriter stderr) throws IOException { // 将错误信息写入BufferedWriter并关闭
        errorMap.keySet().stream().sorted().forEach(line -> {
            try {
                stderr.write(line + " " + errorMap.get(line).getErrorCode() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stderr.close();
    }
}
