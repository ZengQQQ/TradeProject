package cn.bjut.jdbc;

public class People {
    private String ID;
    private String acc;
    private String psw;

    public People(String ID, String acc, String psw) {
        this.ID = ID;
        this.acc = acc;
        this.psw = psw;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
