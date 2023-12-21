package main.java.Journal_Management_System.dao;

import main.java.Journal_Management_System.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {
    public boolean exists(String username){
        String sql="select * from users where username=?";
        //twr
        //获取连接 获取预编译执行器
        try(Connection con= DatabaseConnection.getCon();
            PreparedStatement pst=con.prepareStatement(sql);)
        {
            //传入参数
            pst.setString(1,username);
            //获取结果集
            ResultSet rs=pst.executeQuery();
            //判断用户存不存在
            return rs.next();
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public boolean checkUser(String username,String password){
        String sql="select * from f_user where username=? and password=?";
        //twr
        //获取连接 获取预编译执行器
        try(Connection con= DatabaseConnection.getCon();
            PreparedStatement pst=con.prepareStatement(sql);)
        {
            //传入参数
            pst.setString(1,username);
            pst.setString(2,password);
            //获取结果集
            ResultSet rs=pst.executeQuery();
            //判断用户存不存在
            return rs.next();
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public boolean addUser(String username,String password){
        String sql="insert into users(username,password,role) values (?,?,?)";
        try(Connection con= DatabaseConnection.getCon();
            PreparedStatement pst=con.prepareStatement(sql);)
        {
            pst.setString(1,username);
            pst.setString(2,password);
            pst.setString(3,"user");
            return pst.executeUpdate()==1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
