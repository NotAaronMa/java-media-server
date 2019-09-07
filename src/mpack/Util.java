package mpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class Util {
    public static byte[] rf(File f, int l) throws IOException {
        FileInputStream in = null;
        byte[] data = new byte[l];

        try {
            in = new FileInputStream(f);
            in.read(data);
        } finally {
            if (in != null)
                in.close();
        }

        return data;
    }

    public static void log(String s, int priority){
        boolean veb = Main.verbose;
        if(veb || priority == 1){
            System.out.println(s);

        }
    }

    public static void parseConfig(String cfgfile){
        File f = new File(cfgfile);
        try {
            byte[]inb = (rf(f, (int) f.length()));
            char[]chars = new char[inb.length];
            for(int i = 0; i < inb.length; i++){
                chars[i] =(char)inb[i];
            }
            StringTokenizer t = new StringTokenizer(new String(chars));
            while(t.hasMoreElements()){
                String s = t.nextToken("\n");
                System.out.print(s + "\n");

            }
        }catch(IOException ex){
            Util.log(ex.getMessage(), 1);

        }
    }
}
