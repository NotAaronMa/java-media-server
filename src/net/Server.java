package net;

import mpack.Util;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;


//TODO ability to generify web root and default
public class Server {
    final static int port = 8080;
    public static String name;
    static String ROOT = "./web";
    static String DEFAULT = "index.html";
    public static void start() {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //handle fileNotFound and permissions
    public static boolean cansend(File s){
        if(s.exists()) {
            return s.getAbsolutePath().contains(new File(ROOT).getAbsolutePath());
        }else{
            return false;
        }
    }
}
