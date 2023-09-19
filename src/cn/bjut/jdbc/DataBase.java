package cn.bjut.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {

        //private String dbpath="jdbc:sqlserver://PC-20140610WGYP:1433;DatabaseName=XuanKeMIS";
        //private String dbpath="jdbc:sqlserver://localhost:1433;integratedSecurity=true; DatabaseName=XuanKeMIS";
        //private String dbUserName="sa";
        //private String dbPassword="sa";//s输入你的数据库密码
        //private String jdbcName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
        private String dbpath="jdbc:mysql://localhost:3306/bookstore?allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8&useSSL=false";
        private String dbUserName="root";
        private String dbPassword="zcq.020731";//s输入你的数据库密码
        private String jdbcName="com.mysql.cj.jdbc.Driver";

        public DataBase(){

        }
        public String GetDBPath()
        {
            return dbpath;
        }
        public void SetDBPath(String dbpath)
        {
            this.dbpath=dbpath;
        }
        public Connection getCon() throws Exception{
            Class.forName(jdbcName);
            //Connection con=DriverManager.getConnection(dbpath,dbUserName,dbPassword);
            Connection con=DriverManager.getConnection(dbpath,dbUserName,dbPassword);
            return con;
        }

        public void closeCon(Connection con) throws Exception{
            if(con!=null){

                con.close();
            }
        }


        public static void OpenDB()
        {DataBase db=new DataBase();
            try {
                db.getCon();
                System.out.println("数据库连接成功!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        public  static void main(String[] args) {
            OpenDB();
        }
    }