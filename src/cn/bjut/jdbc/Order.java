package cn.bjut.jdbc;

public class Order {

    private  User user;
    private  Product product;

    private int p_id;
    private int u_id;
    private String buytime;
    private int quantity;
    private String totalprice;

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalptice) {
        this.totalprice = totalptice;
    }

    public  Order(User user, Product product) {
        this.product=product;
        this.user=user;
    }

    public Order() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getBuytime() {
        return buytime;
    }

    public void setBuytime(String buytime) {
        this.buytime = buytime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
