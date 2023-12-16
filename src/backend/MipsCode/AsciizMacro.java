/**
 * Created by 曾宪域 on 2023/12/13
 */
package backend.MipsCode;

public class AsciizMacro implements MipsCode {
    private final String label;
    private final String stringValue;

    public AsciizMacro(String label, String stringValue) {
        this.label = label;
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return label + ": .asciiz \"" + stringValue + "\"";
    }
}
