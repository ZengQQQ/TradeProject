package cn.bjut.jdbc;

import java.sql.SQLException;
import java.util.List;

public class Test {


    public static void main(String[] args) throws SQLException {
        DataControl data = new DataControl();


        List<User> list = data.selectUserTable();
        for(User i : list){
            System.out.println(i.getU_sex());
        }
    }

}
