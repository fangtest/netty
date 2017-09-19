package com.example.telnet.pac;

/**
 * @author fangtest on 2017/9/19.
 */
public class FXPkg {
    private int    opcode;
    private String desc;
    private String msg;

    public FXPkg() {
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FXPkg fxPkg = (FXPkg) o;

        if (opcode != fxPkg.opcode) {
            return false;
        }
        if (desc != null ? !desc.equals(fxPkg.desc) : fxPkg.desc != null) {
            return false;
        }
        return msg != null ? msg.equals(fxPkg.msg) : fxPkg.msg == null;

    }

    @Override
    public int hashCode() {
        int result = opcode;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FXPkg{" +
                "opcode=" + opcode +
                ", desc='" + desc + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
