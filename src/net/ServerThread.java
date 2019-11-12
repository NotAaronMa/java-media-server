//TODO make thread reuasable
//TODO fuckin implement the POST request

package net;

import mpack.Util;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
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
                handleReq(header.toString(),String.valueOf(bl));
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

    private void handleReq(String header, String body) throws IOException {
        HashMap<String,String> headerops = HeaderUtils.parseHeader(header);
        String requestType = headerops.get("RequestType");
        switch(requestType.toLowerCase().charAt(0)){
            case 'g':
                get(headerops);
            case 'h':

            case 'p':

        }
        out.flush();
        dout.flush();
        Util.log("Done handling request: "  + requestType, 0);
    }

    //handle POST request
    private void post(String req) {
    }

    //handle HEAD request
    private void head(HashMap<String,String> ops) throws IOException {
        String fr = ops.get("File");
        Path p = Server.getFile(fr);
        String mime = Files.probeContentType(p);
        HashMap<String, String> options = new HashMap<>();
        options.put("Content-Type", mime);
        options.put("Content-Length", Files.size(p) + "");
        out.print(HeaderUtils.genHeaders(options));
        out.flush();
    }

    //handle GET file request
    private void get(HashMap<String,String> ops) throws IOException {
        String fr = ops.get("File");
        byte[] data;
        String mime;
        Path p = Server.getFile(fr);
        mime = Files.probeContentType(p);
        //check if file exists
        data = Util.rf(p);
        HashMap<String,String>options = new HashMap<>();
        options.put("Content-Type",mime);
        options.put("Content-Length", data.length + "");
        out.print(HeaderUtils.genHeaders(options));
        out.flush();
        dout.write(data,0,data.length);
        System.out.println("sent file: " + fr + "mimetype: " + mime);

    }



}
