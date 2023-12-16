package midend.LabelTable;

import midend.MidCode.MidCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class LabelTable {
    // 单例模式，创建一个LabelTable实例
    private static final LabelTable LABEL_TABLE = new LabelTable();
    // 存储中间代码与标签的映射关系
    private static final HashMap<MidCode, LinkedList<Label>> code2label = new HashMap<>();

    // 获取LabelTable实例
    public static LabelTable getInstance() {
        return LABEL_TABLE;
    }

    // 设置中间代码对应的标签
    public void setMidCode(MidCode midCode, Label label) {
        // 如果code2label中不包含该中间代码，则添加一个新的LinkedList
        if (!code2label.containsKey(midCode)) {
            code2label.put(midCode, new LinkedList<>());
        }
        // 将标签添加到对应的中间代码的LinkedList中
        code2label.get(midCode).add(label);
    }

    // 获取中间代码对应的标签列表
    public LinkedList<Label> getLabelList(MidCode midCode) {
        // 如果code2label中不包含该中间代码，则返回一个空的LinkedList
        return code2label.getOrDefault(midCode, new LinkedList<>());
    }

    // 移除未使用的标签
    public void removeUnusedLabels(HashSet<Label> usedLabels) {
        // 遍历code2label中的每个LinkedList
        for (LinkedList<Label> labelList : code2label.values()) {
            // 移除未使用的标签，且标签ID不为0
            labelList.removeIf(label -> !usedLabels.contains(label) && label.getLabelId() != 0);
        }
    }
}
