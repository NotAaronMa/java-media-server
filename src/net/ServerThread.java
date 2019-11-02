//TODO make thread reuasable
//TODO fuckin implement the POST request

package net;

import mpack.Main;
import mpack.Util;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;


public class ServerThread implements Runnable {

    //Socket
    private Socket sock;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dout;


    public ServerThread(Socket s) {
        sock = s;
    }
    @Override
    public void run() {
        try {
            //init IO
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());
            dout = new BufferedOutputStream(sock.getOutputStream());
            do{
                //init io
                StringBuffer header = new StringBuffer();
                //parse request to buffer
                String next = in.readLine();
                int bl = 0;
                //parse until empty line for request header
                do {
                    if(next.contains("Content-Length:"))
                        bl = Integer.parseInt(next.split(" ")[1]);
                    header.append(next + "\n");
                    next = in.readLine();
                }while(!next.isEmpty());
                //parse request body
                char[]body = new char[bl];
                if(bl != 0) {
                    in.read(body);
                }
                handlereq(header.toString(),String.valueOf(bl));
            }while(!sock.isClosed());
            in.close();
            out.close();
            dout.close();
            sock.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    private void handlereq(String header, String body) throws IOException {
        StringTokenizer t = new StringTokenizer(header); //tokenizer for the whole request
        String rt = new StringTokenizer(t.nextToken("\n")).nextToken();
        Util.log("Handling request: "  + rt, 0);
        System.out.println(header);
        if (rt.equals("GET")) {
            get(header);
        } else if (rt.equals("HEAD")) {
            head(header);
        } else if (rt.equals("POST")) {
            post(header);
        }
        out.flush();
        dout.flush();
        Util.log("Done handling request: "  + rt, 0);
    }

    //handle POST request
    private void post(String req) {
    }

    //handle HEAD request
    private void head(String req) throws IOException {
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        Path p = Server.getFile(req);
        String mime = Files.probeContentType(p);
        HashMap<String,String>options = new HashMap<>();
        options.put("Content-Type",mime);
        options.put("Content-Length", Files.size(p) + "");
        out.print(HeaderUtils.genHeaders(options));
    }

    //handle GET file request
    private void get(String req) throws IOException {
        Util.log(req, 1);
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        //find file requested
        //init file io
        byte[] data;
        String mime;
        Path p = Server.getFile(fr);
        mime = Files.probeContentType(p);
        //check if file exists
        data = Util.rf(p);
        HashMap<String,String>options = new HashMap<>();
        options.put("Content-Type",mime);
        options.put("Content-Length", data.length + "");
        System.out.println("Sending file"  + p.toString());

        out.print(HeaderUtils.genHeaders(options));
        //send file
        dout.write(data, 0, data.length);
    }



}
