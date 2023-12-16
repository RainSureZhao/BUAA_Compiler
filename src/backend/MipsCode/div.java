/**
 * Created by 曾宪域 on 2023/12/13
 */
package backend.MipsCode;

import backend.Reg;

public class div implements MipsCode {
    private final Reg rs;
    private final Reg rt;

    public div(Reg rs, Reg rt) {
        this.rs = rs;
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "div " + rs + ", " + rt;
    }
}
