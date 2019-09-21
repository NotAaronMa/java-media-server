//TODO multithread this bitch
package net;

import mpack.Main;
import mpack.Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    final static int port = 8080;
    public static String name;
    static String DEFAULT = "index.html";
    static String NOTFOUND = "404.html";
    public static void start() {
        try {
            ServerSocket sc = new ServerSocket(port);
            Util.log("Server started.\nListening for connections on port : " + port + " ...\n", 1);
            // we listen until user halts server execution
            while (true) {
                ServerThread response = new ServerThread(sc.accept());
                // create dedicated thread to manage the client connection
                new Thread(response).start();
                Util.log("Server ready for more connections", 0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //handle fileNotFound and permissions
    public static boolean cansend(Path p){
        if(Files.exists(p)) {
            return p.toString().contains(Main.WEB_ROOT);
        }else{
            return false;
        }
    }
}
