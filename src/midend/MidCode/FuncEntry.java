package midend.MidCode;

import midend.LabelTable.Label;

public class FuncEntry implements MidCode {
    private final Label entryLabel; // 函数入口标签

    public FuncEntry(Label entryLabel) {
        this.entryLabel = entryLabel;
        MidCodeTable.getInstance().addMidCode(this); // 将当前对象添加到中间代码表中
    }

    public Label getEntryLabel() {
        return entryLabel; // 返回函数入口标签
    }

    @Override
    public String toString() {
        return entryLabel.toString(); // 返回函数入口标签的字符串表示
    }
}
