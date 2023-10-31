package cn.bjut.jdbc;

public class Comment{
    private String name;
    private String time;
    private String content;

    private String flag;

    public Comment(String userName, String time, String content,String flag) {
        this.name = userName;
        this.time = time;
        this.content = content;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUserName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}