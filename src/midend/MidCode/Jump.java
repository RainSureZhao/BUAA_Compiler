package midend.MidCode;

import midend.LabelTable.Label;

public class Jump implements MidCode {
    private Label label; // 定义私有成员变量label，用于存储跳转的目标标签

    public Jump(Label label) { // 构造函数，接收一个Label对象作为参数
        this.label = label; // 将参数赋值给成员变量label
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到MidCodeTable中
    }

    public Label getLabel() { // 获取label的方法
        return label; // 返回label
    }

    public void setLabel(Label label) { // 设置label的方法
        this.label = label; // 将参数赋值给成员变量label
    }

    @Override
    public String toString() { // 重写toString方法
        if(label == null) { // 如果label为空
            return ""; // 返回跳转指令的字符串表示，格式为 "JUMP"
        }
        return "JUMP " + label.getLabelName(); // 返回跳转指令的字符串表示，格式为 "JUMP 标签名"
    }
}
