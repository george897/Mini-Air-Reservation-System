/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pp_project_final_version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GeorgeAlaa
 */
public class RequestHandler implements Runnable{
    private final Socket clientSocket;
     private static Statement state;
    private static Statement state2;
     private static Statement state3;
    private static dbconnection db;
    private static String query;
    private static String query2;
     private static String query3;
    private static ResultSet rs;
     private static ReentrantLock reentrantlock=new ReentrantLock(); 
  public RequestHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
   public void Resev(int seatno,int id) throws SQLException, IOException{
        query="UPDATE seats set availabltey = 'unavailable' where seatno="+seatno+"";
        query2="UPDATE client SET seatNo="+seatno+" WHERE id = "+id+"";
        db=new dbconnection();
        Connection c=db.connect();
        state=c.createStatement();
        state2=c.createStatement();
       state.execute(query);
       state2.execute(query2);
    }
       public boolean checkValid(int seatno) throws SQLException{
    query="Select * from seats where seatno="+seatno+"";
      db=new dbconnection();
    Connection c=db.connect();
    state=c.createStatement();
    rs=state.executeQuery(query);
    rs.next();        
     String available =rs.getString("availabltey");
      System.out.println(available);
     if(available.equalsIgnoreCase("available")){
       // System.out.println("succ");
         return true;
     }
     else{
        //    System.out.println("error");
         return false;
     }
    }
       public boolean noseat(int seatno) throws SQLException{
       query="Select * from seats where seatno="+seatno+"";
      db=new dbconnection();
    Connection c=db.connect();
    state=c.createStatement();
    rs=state.executeQuery(query);
    rs.next();        
     String available =rs.getString("availabltey");
     if(available.equalsIgnoreCase("noSeat")){
       // System.out.println("succ");
         return true;
     }
     else{
        //    System.out.println("error");
         return false;
       }
       }
    @Override
    public void run() {
       PrintWriter out = null;
            BufferedReader in = null;
            String line="";
            int seatno=0;
                int id=0;
            try {
                    
                  // get the outputstream of client
                out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
  
                  // get the inputstream of client
                in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream()));
  
                
                while (true){
                    // writing the received message from
                    // client
                    line=in.readLine();
                    if(line.equalsIgnoreCase("cancel")){
                        System.out.println(line);    
                    id=in.read();
                     query="SELECT * FROM client WHERE id="+id+"";
                    db=new dbconnection();
                 Connection c;
                 try {
                  c = db.connect();
             state=c.createStatement();
             rs=state.executeQuery(query);
            rs.next();
             seatno=rs.getInt("seatNo");
             if(this.noseat(seatno)){
                 System.out.println("there is no seat");
             }else{
            query2="UPDATE seats set availabltey = 'available' where seatno="+seatno+"";
            query3="UPDATE client SET seatNo=0 WHERE id = "+id+"";
            state2=c.createStatement();
            state3=c.createStatement();
            state2.execute(query2);
            state3.execute(query3);
             }
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
                    }else{
                        System.out.println(line);
                   seatno=in.read();
                    System.out.println(seatno);
                    id=in.read();
                    System.out.println(id);
                     reentrantlock.lock();
                    if(this.checkValid(seatno)){
                    this.Resev(seatno, id);
                    System.out.println("setReserv");
                   // out.write(id);
                    //out.flush();
                    }else{
                        System.out.println("error");
                    }
                    reentrantlock.unlock();
                    
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

