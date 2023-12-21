package main.java.Journal_Management_System.util;

import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    static Properties pro=new Properties();
    static {
//        InputStream in=CF.class.getClassLoader().getResourceAsStream("config/db.properties");
        try{
            pro.load(new FileReader("config/db.properties"));
//            in.close();
            Class.forName(pro.getProperty("m.driver"));
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public static Connection getCon() throws SQLException {
        return DriverManager.getConnection(pro.getProperty("m.url"),
                pro.getProperty("m.username"),
                pro.getProperty("m.password"));
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getCon());
    }
    public static  void close(Connection con, PreparedStatement pst, ResultSet set){
        try {
            if(set!=null)
                set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(pst!=null)
                    pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(con!=null)
                        con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
