package net;
import mpack.*;

import java.io.*;
import java.net.ServerSocket;

public class Server{
    final static int port  = 8080;
    public static String name;

    public static void  start(){
        try {
            ServerSocket sc = new ServerSocket(port);
            Util.log("Server started.\nListening for connections on port : " + port + " ...\n", 1);
            // we listen until user halts server execution
            while (true) {
                Response response = new Response(sc.accept());
                // create dedicated thread to manage the client connection
                new Thread(response).start();
                Util.log("Server ready for more connections", 0);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
