package mpack;

import file.Loader;
import net.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class Main {
    public static String ROOT;
    public static String WEB_ROOT;
    public static boolean verbose;
    public static String configfile;

    public static void main(String[] args) throws IOException {
        ROOT = new File("").getAbsolutePath();
        WEB_ROOT = ROOT + "/web";
        parseArgs(args);
        Util.log("reading from configfile: " + configfile, 0);
        parseConfig(Util.readLines(Path.of(configfile)));
        Loader.update();
        Server.start();
    }

    public static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                verbose = true;
            } else if (args[i].equals("-c")) {
                configfile = args[++i];
            } else {
                System.out.println("ur bad");
            }
        }
    }

    public static void parseConfig(String[] cfg) {
        for(String s: cfg){
            String[]tk = s.split(" ");
            if(tk[0].equals("root_dir")){
                ROOT = tk[1];
            }else if(tk[0].equals("web_dir")){

            }
        }
    }


}
