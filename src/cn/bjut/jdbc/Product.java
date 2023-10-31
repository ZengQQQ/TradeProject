package cn.bjut.jdbc;

public class Product {
    private int p_id;
    private int m_id;
    private String p_name;
    private String p_desc;
    private String p_class;
    private String p_price;
    private String p_status;
    private int  p_quantity;
    private String p_img;

    public Product() {
    }

    public Product(int pid, String newName, String newdesc, String newclass, double newPrice, String newStatus,int newquantity, String newImgName) {
        this.p_id = pid;
        this.p_name = newName;
        this.p_desc = newdesc;
        this.p_class =newclass;
        this.p_price = String.valueOf(newPrice);
        this.p_status = newStatus;
        this.p_quantity=newquantity;
        this.p_img = newImgName;
    }

    public Product(String newName, String newdesc, String newclass, double newPrice, String newStatus,int newquantity, String newImgName,int m_id) {
        this.m_id=m_id;
        this.p_name = newName;
        this.p_desc = newdesc;
        this.p_class =newclass;
        this.p_price = String.valueOf(newPrice);
        this.p_status = newStatus;
        this.p_quantity=newquantity;
        this.p_img = newImgName;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_desc() {
        return p_desc;
    }

    public void setP_desc(String p_desc) {
        this.p_desc = p_desc;
    }

    public String getP_class() {
        return p_class;
    }

    public void setP_class(String p_class) {
        this.p_class = p_class;
    }

    public String getP_price() {
        return p_price;
    }

    public void setP_price(String p_price) {
        this.p_price = p_price;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }

    public String getP_quantity() {
        return String.valueOf(p_quantity);
    }

    public void setP_quantity(int p_quantity) {
        this.p_quantity = p_quantity;
    }

    public String getP_img() {
        return p_img;
    }

    public void setP_img(String p_img) {
        this.p_img = p_img;
    }
}
