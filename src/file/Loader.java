package file;

import mpack.Main;
import mpack.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

//TODO create a class to generate html as string
public class Loader {

    //Files relative to MAIN.ROOT

    static String vidDir = "/vid/";
    static String genDir = "/a/";
    static String pltemp = "/res/templates/pltemp.html";
    static Path[] plists;


    public static void update() {
        try {
            Path genpath = Path.of(Main.WEB_ROOT, genDir);
            if (!Files.exists(genpath)) {
                Files.createDirectory(genpath);
            }
            plists = Util.getChildren(Paths.get(Main.WEB_ROOT, vidDir));

            for (int i = 0; i < plists.length; i++) {
                String filename = plists[i].getFileName().toString();
                Path outFile = Path.of(Main.WEB_ROOT, genDir, filename + ".html");

                Files.deleteIfExists(outFile);
                Files.createFile(outFile);
                Path vdir = Path.of(Main.WEB_ROOT, vidDir, filename);
                WritePlPage(vdir, outFile);
            }

        } catch (IOException e) {
            Util.log("Exception: File " + e.getMessage() + " not found", 0);

        }


    }

    private static void WritePlPage(Path vdir, Path outFile) throws IOException {
        //only make one level to avoid fucking up filesystem
        String flag = "<!-- FLAG PUT LINKS HERE -->";
        Files.deleteIfExists(outFile);
        Files.createFile(outFile);

        Path[] vidpaths = Util.getChildren(vdir);
        String[] input = Util.readLines(Path.of(Main.ROOT, pltemp));
        StringBuffer buffer = new StringBuffer();
        vidPath[]vp = new vidPath[vidpaths.length];

        for(int i = 0; i < vp.length; i++){
            vp[i] = new vidPath(vidpaths[i]);
        }
        Arrays.sort(vp);


        int i;
        for (i = 0; (!input[i].contains(flag)) && i < input.length; i++) {
            buffer.append(input[i] + "\n");
        }
        for (int j = 0; j < vidpaths.length; j++) {
            buffer.append(genVidEntry(vp[j]));

        }
        for (; i < input.length; i++) {
            buffer.append(input[i] + "\n");
        }
        Files.writeString(outFile, buffer.toString());

    }

    //note give this function absolute path
    private static String genVidEntry(vidPath p) {
        String path = p.p.toAbsolutePath().toString().replace(Main.WEB_ROOT, "");
        String filename = p.name;
        return "<li class = \"video-select\" id=\"" + path + "\">" + filename + "</li> \n";



    }

    private static class vidPath implements Comparable<vidPath>{
        final Path p;
        final String name;

        public vidPath(Path p){
            this.p = p;
            this.name = p.getFileName().toString();
        }
        @Override
        public int compareTo(vidPath vp) {
            return this.name.compareTo(vp.name);
        }
    }

}
