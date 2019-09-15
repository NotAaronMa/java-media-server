package mpack;

import net.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class Util {
    public static byte[] rf(String dir) throws IOException {
        byte[] data = Files.readAllBytes(Path.of(dir));
        return data;
    }

    public static byte[] rf(Path dir) throws IOException {
        byte[] data = Files.readAllBytes(dir);
        return data;
    }
    public static String bts(byte[]bytes){
        char[] chars= new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) bytes[i];
        }
        return new String(chars);

    }



    public static void log(String s, int priority) {
        boolean veb = Main.verbose;
        if (veb || priority == 1) {
            System.out.println(s);

        }
    }

    public static void parseConfig(String cfgfile) { ;
        try {
            byte[] inb = (rf(cfgfile));
            char[] chars = new char[inb.length];
            for (int i = 0; i < inb.length; i++) {
                chars[i] = (char) inb[i];
            }
            StringTokenizer t = new StringTokenizer(new String(chars));
            while (t.hasMoreElements()) {
                String s = t.nextToken("\n");
                System.out.print(s + "\n");

            }
        } catch (IOException ex) {
            Util.log(ex.getMessage(), 1);

        }
    }
}
