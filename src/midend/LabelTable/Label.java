package midend.LabelTable;

import backend.MipsCode.MipsCode;
import midend.MidCode.Jump;
import midend.MidCode.MidCode;

public class Label implements MipsCode {
    private static int labelCount = 1; // 静态变量，用于记录标签数量
    private int labelId = 0; // 标签的唯一标识符
    private String labelName; // 标签名称
    private MidCode midCode; // 关联的中间代码

    public Label() {
        this.labelId = labelCount++; // 构造函数，自增标签数量并分配给当前标签
    }

    public Label(String labelName) {
        this.labelName = labelName; // 构造函数，指定标签名称
    }

    public int getLabelId() {
        return labelId; // 获取标签的唯一标识符
    }

    public String getLabelName() {
        if (labelName != null) {
            return labelName; // 如果标签名称不为空，则返回标签名称
        } else {
            return "Label" + labelId; // 否则返回默认的标签名称
        }
    }

    public MidCode getMidCode() {
        return midCode; // 获取关联的中间代码
    }

    public void setMidCode(MidCode midCode) {
        this.midCode = midCode; // 设置关联的中间代码
        LabelTable.getInstance().setMidCode(midCode, this); // 将中间代码与当前标签关联
    }

    public Label getTarget() {
        if (midCode instanceof Jump) {
            return ((Jump) midCode).getLabel().getTarget(); // 如果关联的中间代码是跳转指令，则返回目标标签的目标标签
        } else {
            return this; // 否则返回当前标签
        }
    }

    @Override
    public String toString() {
        if (labelName != null) {
            return labelName + ":"; // 如果标签名称不为空，则返回标签名称加冒号
        } else {
            return "Label" + labelId + ":"; // 否则返回默认的标签名称加冒号
        }
    }
}
