package net;

import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;

public class HeaderUtils {


    public static HashMap<String,String>parseHeader(String header){
        String[]headerLines = header.split("\n");
        HashMap<String,String> options = new HashMap<>();
        String[]h0 = headerLines[0].split(" ");
        options.put("RequestType", h0[0]);
        options.put("File",h0[1]);
        options.put("HTTP", h0[2]);

        for(int i = 1; i < headerLines.length; i++){
            String[]s = headerLines[i].split(" ");
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
