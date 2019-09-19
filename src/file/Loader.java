package file;

import mpack.Main;
import mpack.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//TODO create a class to generate html as string
//TODO change to non-blocking io
public class Loader {
    static String vidDir = "/web/vid";
    static String genDir = "/web/gen";

    static String vidRoot;
    static String pageRoot;

    static String pltemp = "/res/templates/pltemp.html";


    private static File[]c;

    public static void update(){
        vidRoot = new File(Main.ROOT, vidDir).getAbsolutePath();
        pageRoot = new File(Main.ROOT, genDir).getAbsolutePath();
        pltemp = new File(Main.ROOT, pltemp).getAbsolutePath();
        c = new File(vidRoot).listFiles();
        
        for(int i = 0;i < c.length;i++) {
            try {
                WritePlPage(c[i], new File(pageRoot, c[i].getName()));
            }catch(IOException ex){
                Util.log(ex.getMessage(), 1);
            }
        }
    }


    private static void WritePlPage(File pl, File out) throws IOException {
        out = new File(out.getAbsolutePath().concat(".html"));
        out.createNewFile();

        String flag = "<!-- FLAG PUT LINKS HERE -->";
        PrintWriter fout = new PrintWriter(new FileWriter(out));
        List<String> sl = Files.readAllLines(Paths.get(pltemp));

        String input;
        int c = 0;

        do{
            input = sl.get(c);
            fout.println(input);
            c++;
        }while(!input.contains(flag));

        File[]vid = pl.listFiles();
        System.out.println(vid.length);
        for(int i = 0; i < vid.length; i++){
            fout.println("<a href=\"" + vid[i].getAbsolutePath() + "\"> fjdasf </a>" );
        }
        for( ; c < sl.size(); c++){
           fout.println(sl.get(c));
        }
        fout.close();
    }
}
