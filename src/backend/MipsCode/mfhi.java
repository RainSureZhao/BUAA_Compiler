/**
 * Created by 曾宪域 on 2023/12/13
 */
package backend.MipsCode;

import backend.Reg;

public class mfhi implements MipsCode {
    private final Reg reg;

    public mfhi(Reg reg) {
        this.reg = reg;
    }

    @Override
    public String toString() {
        return "mfhi " + reg;
    }
}
