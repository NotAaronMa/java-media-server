package file;

import mpack.Util;

import java.io.File;
import java.util.ArrayList;

public class Playlists {
    private static ArrayList<String> playlists;
    private static String dbFile;


    public static void init(){
        playlists = new ArrayList<String>();
    }

    public void addLib(String dir){
        File d = new File("f");
        if(d.exists() && d.isDirectory()){
            playlists.add(dir);


        }else{
            Util.log("Directory " + dir + " not found or invalid", 1);


        }


    }








}
