package mpack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Util {

    //file io stuff
    public static byte[] rf(Path dir) throws IOException {
        byte[] data = Files.readAllBytes(dir);
        return data;
    }

    public static String[] readLines(Path p) throws IOException {
        List<String> in = Files.readAllLines(p);
        String[] s = new String[in.size()];
        for (int i = 0; i < in.size(); i++) {
            s[i] = in.get(i);
        }
        return s;
    }
    public static Path[] getChildren(Path dir) throws IOException {
        Stream<Path> p = Files.list(dir);
        Object[] a = p.toArray();
        Path[] s = new Path[a.length];
        for (int i = 0; i < a.length; i++) {
            s[i] = (Path) a[i];
        }
        return s;
    }
    public static void overWrite(Path p, StringBuffer b) throws IOException {
        Files.deleteIfExists(p);
        Files.createFile(p);
        Files.writeString(p,b.toString());
    }

    public static Path UNIXfindCfg() throws FileNotFoundException {
        Path file = Path.of(Main.ROOT,"config");
        String userhome = System.getenv("HOME");
        System.out.println(userhome);
        if(Files.exists(Path.of(userhome,".config/ojms/config"))){

            file = Path.of(userhome,".config/ojms/config");
        }else if(Files.exists(Path.of("/etc/ojms/config"))){
            file = Path.of("/etc/ojms/config");
        }
        return file;
    }
    //logging
    public static void log(String s, int priority) {
        boolean veb = Main.verbose;
        if (veb || priority == 1) {
            System.out.println(Thread.currentThread().getName() + " " + s);

        }
    }


}
