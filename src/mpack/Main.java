package mpack;

import file.Loader;
import net.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {
    public static String ROOT;
    public static String WEB_ROOT;
    public static boolean verbose;
    public static Path configfile;

    public static void main(String[] args) throws IOException {
        ROOT = new File("/srv/ojms").getAbsolutePath();
        WEB_ROOT = ROOT + "/web";
        configfile = Util.UNIXfindCfg();

        Loader.update();
        Server.start();
    }

    public static HashMap<String,String> parseConfig(String[] cfg) {
        HashMap<String,String> options = new HashMap<>();
        for(String s: cfg){
            String[]tk = s.split(" ");
            options.put(tk[0], tk[1]);
        }
        return options;
    }


}
