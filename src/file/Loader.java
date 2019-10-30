package file;

import mpack.Main;
import mpack.Util;
import mpack.WrappedPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//TODO create a class to generate html as string
public class Loader {

    //Files relative to MAIN.ROOT

    static String vidDir = "/vid/";
    static String genDir = "/a/";
    static String indexTemplate = "/res/templates/indexTemp.html";
    static String playlistTemplate = "/res/templates/playlistTemp.html";

    public static void update() {
        try {
            Path genpath = Path.of(Main.WEB_ROOT, genDir);
            if (!Files.exists(genpath)) {
                Files.createDirectory(genpath);
            }
            Path[] playlistPaths = Util.getChildren(Paths.get(Main.WEB_ROOT, vidDir));
            ArrayList<WrappedPath> playlistUrls = new ArrayList<>();
            for (Path p : playlistPaths) {
                String filename = p.getFileName().toString();
                Path outFile = Path.of(Main.WEB_ROOT, genDir, filename + ".html");
                Path vdir = Path.of(Main.WEB_ROOT, vidDir, filename);
                playlistUrls.add(new WrappedPath(outFile));
                WritePlPage(vdir, outFile);
            }
            Collections.sort(playlistUrls);
            WrappedPath[] plpaths = new WrappedPath[playlistPaths.length];
            playlistUrls.toArray(plpaths);
            WriteIndexPage(Path.of(Main.WEB_ROOT,"index.html"), plpaths);

        } catch (IOException e) {
            Util.log("Exception: File " + e.getMessage() + " not found", 0);
        }
    }

    private static void WritePlPage(Path vidDir, Path outFile) throws IOException {
        //only make one level to avoid fucking up filesystem
        String flag = "<!-- FLAG PUT LINKS HERE -->";
        Path[] rawPaths = Util.getChildren(vidDir);

        String[] input = Util.readLines(Path.of(Main.ROOT, playlistTemplate));
        StringBuffer buffer = new StringBuffer();
        WrappedPath[] vp = new WrappedPath[rawPaths.length];
        for (int i = 0; i < vp.length; i++) {
            vp[i] = new WrappedPath(rawPaths[i]);
        }
        Arrays.sort(vp);
        for(WrappedPath p: vp){
            System.out.println(p.name);
        }
        for(String s  : input) {
            if(s.contains(flag)){
                for(WrappedPath wp : vp) {
                    buffer.append(genVidEntry(wp));
                }
            }else{
                buffer.append(s.strip() + "\n");
            }
        }
        Util.overWrite(outFile, buffer);
    }
    private static void WriteIndexPage(Path outFile, WrappedPath[]pl){
        StringBuffer buffer = new StringBuffer();


    }

    //note give this function absolute path
    private static String genVidEntry(WrappedPath p) {
        String path = p.p.toAbsolutePath().toString().replace(Main.WEB_ROOT, "");
        String filename = p.name;
        return "<li class = \"video-select\" id=\"" + path + "\">" + filename + "</li> \n";
    }
    private static String genIndexEntry(WrappedPath p) {
        String path = p.p.toAbsolutePath().toString().replace(Main.WEB_ROOT, "");
        String filename = p.name;
        return "<li class = \"selct-box\" id=\"" + path + "\">" + filename + "</li> \n";
    }

}
