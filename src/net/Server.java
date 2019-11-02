//TODO multithread this bitch
package net;

import mpack.Main;
import mpack.Util;

import java.net.Socket;
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
                Socket c = sc.accept();
                ServerThread Response = new ServerThread(c);
                // create dedicated thread to manage the client connection
                new Thread(Response).start();
                Util.log("Server ready for more connections", 0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Path getFile(String req){
        if (req.equals("/")) {
            req = DEFAULT;
        } else if (Files.notExists(Path.of(Main.WEB_ROOT, req))) {
            req = NOTFOUND;
        }
        Path rtn = Path.of(Main.WEB_ROOT,req);
        return canSend(rtn)? rtn:Path.of(Main.WEB_ROOT,NOTFOUND);
    }

    //handle fileNotFound and permissions
    private static boolean canSend(Path p) {
        if (Files.exists(p)) {
            return p.toString().contains(Main.WEB_ROOT);
        } else {
            return false;
        }
    }
}
