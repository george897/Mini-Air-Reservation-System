/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pp_project_final_version;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author GeorgeAlaa
 */
public class Server {
     private static ThreadPoolExecutor executor;
     public static void main(String[] args)
     
    {
        ServerSocket server = null;
        
        try {
  
            // server is listening on port 1234
            executor=(ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            server = new ServerSocket(1234);
            server.setReuseAddress(true);
            System.out.println("server Start");
  
            // running infinite loop for getting
            // client request
            while (true) {
  
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
  
                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                                   + client.getInetAddress()
                                         .getHostAddress());
                
  
                // create a new thread object
                RequestHandler clientSock
                    = new RequestHandler(client);
  
                // This thread will handle the client
                // separately
               executor.execute(clientSock);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

