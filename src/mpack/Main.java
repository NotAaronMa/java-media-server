package mpack;
import file.Loader;
import net.Server;

import java.io.File;

public class Main {
    public static String ROOT;
    public static String WEB_ROOT;

public static boolean verbose;
    public static String configfile;

    public static void main(String[] args) {
        ROOT = new File("").getAbsolutePath();
        WEB_ROOT = ROOT+"/web";

        parseArgs(args);
        Util.log("reading from configfile: " + configfile, 0);
        Util.parseConfig(configfile);
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
}
