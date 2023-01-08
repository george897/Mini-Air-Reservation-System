/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pp_project_final_version;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author GeorgeAlaa
 */
public class dbconnection {
    private String user="root";
    private String pass="";
    private String add="jdbc:mysql://localhost/ppproject";
    private Connection connect;
    
    public Connection connect() throws SQLException{
    connect=DriverManager.getConnection(add,user,pass);
      //  System.out.println("connected");
        return connect;
    }
}