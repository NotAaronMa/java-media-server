package net;

import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;

public class HeaderUtils {


    public static HashMap<String,String>parseHeader(String header){
        HashMap<String,String> options = new HashMap<>();
        for(int i = 0; i < header.length(); i++){
            String[]s = header.split(" ");
            options.put(s[0].replace(":",""), s[1]);
        }
        return options;
    }
    public static String genHeaders(HashMap<String,String>options){
        StringBuffer buffer = new StringBuffer();
        buffer.append("HTTP/1.1 200 OK \n");
        buffer.append("net.Server:" + Server.name + " : 1.0 \n");
        buffer.append("Date: " + new Date() + "\n");
        //io
        for(String s: options.keySet()){
            buffer.append(s + ": " + options.get(s) + "\n");
        }
        buffer.append("\n");
        return buffer.toString();
    }


}
