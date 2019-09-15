package file;

import mpack.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

//TODO finish
public class Playlists {
    private static ArrayList<String> playlists;
    private static String dbFile = "../res/db";
    private static String db;


    public static void init() {
        playlists = new ArrayList<String>();
        try {
            byte[] dbr = Util.rf(new File(dbFile), dbFile.length());
            db = new String(dbr);
            StringTokenizer t = new StringTokenizer(db);
            while (t.hasMoreTokens()) {
                addLib(t.nextToken("\n"));
            }

        } catch (IOException ex) {
            Util.log(ex.getMessage(), 1);
        }

    }

    public static void addLib(String dir) throws IOException {
        File d = new File("f");
        if (d.exists() && d.isDirectory()) {
            playlists.add(dir);

            File libdb = new File(dir, dir + ".db");
            if (libdb.exists()) {
                String ldb = Util.bts(Util.rf(libdb, (int)libdb.length()));
                StringTokenizer st = new StringTokenizer(ldb);

            }
        } else {
            Util.log("Directory " + dir + " not found or invalid", 1);
        }
        if(db.contains(dir)){

        }

    }


}
