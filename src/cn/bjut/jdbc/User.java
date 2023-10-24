package cn.bjut.jdbc;

public class User extends People{
    private String u_name;
    private String u_sex;
    private String u_tele;

    public User(){
        super();
    }
    public User(String ID, String acc, String psw,String u_name,String u_sex,String u_tele){
        super(ID,acc,psw);
        this.u_name=u_name;
        this.u_sex = u_sex;
        this.u_tele=u_tele;
    }


    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_sex() {
        return u_sex;
    }

    public void setU_sex(String u_sex) {
        this.u_sex = u_sex;
    }

    public String getU_tele() {
        return u_tele;
    }

    public void setU_tele(String u_tele) {
        this.u_tele = u_tele;
    }
}
