package cn.bjut.jdbc;

public class Merchant extends People{

    private String m_name;
    private String m_sex;
    private String m_tele;


    public Merchant(String ID, String acc, String psw) {
        super(ID, acc, psw);
    }

    public Merchant(String ID, String acc, String psw, String m_name, String m_sex, String m_tele) {
        super(ID, acc, psw);
        this.m_name = m_name;
        this.m_sex = m_sex;
        this.m_tele = m_tele;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_sex() {
        return m_sex;
    }

    public void setM_sex(String m_sex) {
        this.m_sex = m_sex;
    }

    public String getM_tele() {
        return m_tele;
    }

    public void setM_tele(String m_tele) {
        this.m_tele = m_tele;
    }
}
