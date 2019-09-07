package mpack;

import net.Server;

public class Main {

    public static final String help =
                    "-c      chose configfile\n" +
                    "-h      print this thingy" +
                            "-v      verbose";


    public static boolean verbose;
    public static String configfile;

    public static void main(String[] args) {
        Server.start();
    }
    public static void parseargs(String[]args){
        for(int i = 0;i  < args.length; i++){
            if(args[i].equals("-v")){
                verbose = true;
            }else if(args[i].equals("-c")){
                configfile = args[++i];
            }else {
                System.out.println(help);
            }
        }
    }
}