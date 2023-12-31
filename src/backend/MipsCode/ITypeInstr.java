/**
 * Created by 曾宪域 on 2023/12/13
 */
package backend.MipsCode;

import backend.Reg;
import midend.MidCode.Imm;

public class ITypeInstr implements MipsCode {
    public enum IOpCode {
        addiu
    }

    public static class IR2I1 extends ITypeInstr {
        private final IOpCode opCode;
        private final Reg rs;
        private final Reg rt;
        private final Imm imm;

        public IR2I1(IOpCode opCode, Reg rs, Reg rt, Imm imm) {
            this.opCode = opCode;
            this.rs = rs;
            this.rt = rt;
            this.imm = imm;
        }

        @Override
        public String toString() {
            return opCode.toString() + " " + rs + ", " + rt + ", " + imm;
        }
    }

    public static class beq extends ITypeInstr {
        private final Reg rs;
        private final Reg rt;
        private final String label;

        public beq(Reg rs, Reg rt, String label) {
            this.rs = rs;
            this.rt = rt;
            this.label = label;
        }

        @Override
        public String toString() {
            return "beq " + rs + ", " + rt + ", " + label;
        }
    }

    public static class bne extends ITypeInstr {
        private final Reg rs;
        private final Reg rt;
        private final String label;

        public bne(Reg rs, Reg rt, String label) {
            this.rs = rs;
            this.rt = rt;
            this.label = label;
        }

        @Override
        public String toString() {
            return "bne " + rs + ", " + rt + ", " + label;
        }
    }

    public static class la extends ITypeInstr {
        private final Reg rt;
        private final Address address;

        public la(Reg rt, Address address) {
            this.rt = rt;
            this.address = address;
        }

        @Override
        public String toString() {
            return "la " + rt + ", " + address;
        }
    }

    public static class li extends ITypeInstr {
        private final Reg rt;
        private final Imm imm;

        public li(Reg rt, Imm imm) {
            this.rt = rt;
            this.imm = imm;
        }

        @Override
        public String toString() {
            return "li " + rt + ", " + imm;
        }
    }

    public static class lw extends ITypeInstr {
        private final Reg rt;
        private final Address address;

        public lw(Reg rt, Address address) {
            this.rt = rt;
            this.address = address;
        }

        @Override
        public String toString() {
            return "lw " + rt + ", " + address;
        }
    }

    public static class sw extends ITypeInstr {
        private final Reg rt;
        private final Address address;

        public sw(Reg rt, Address address) {
            this.rt = rt;
            this.address = address;
        }

        @Override
        public String toString() {
            return "sw " + rt + ", " + address;
        }
    }
}
