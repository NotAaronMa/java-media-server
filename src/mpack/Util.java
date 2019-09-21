package mpack;

import net.Server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Util {


    public static byte[] rf(String dir) throws IOException {
        byte[] data = Files.readAllBytes(Path.of(dir));
        return data;
    }

    public static byte[] rf(Path dir) throws IOException {
        byte[] data = Files.readAllBytes(dir);
        return data;
    }

    public static String[]readLines(Path p) throws IOException {
        List<String> in = Files.readAllLines(p);
        String[]s = new String[in.size()];
        for(int i = 0; i < in.size(); i++){
            s[i] = in.get(i);
        }
        return s;
    }

    public static Path[] getChildren(Path dir) throws IOException {
        Stream<Path> p = Files.list(dir);
        Object[]a = p.toArray();
        Path[]s = new Path[a.length];
        for(int i = 0; i < a.length; i++){
            s[i] = (Path)a[i];
        }
       return s;
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
