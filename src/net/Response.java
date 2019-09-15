package net;

import mpack.Main;
import mpack.Util;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.StringTokenizer;
//TODO fuckin implement the POST request


public class Response implements Runnable {

    //Socket
    private Socket sock;
    //io
    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dout;


    public Response(Socket s) {
        sock = s;
    }

    public void handlereq(String req) throws IOException {

        StringTokenizer t = new StringTokenizer(req); //tokenizer for the whole request
        String rt = new StringTokenizer(t.nextToken("\n")).nextToken();
        Util.log("Handling request: " + req, 0);
        boolean ka = req.contains("keep-aliveCookie");
        if (rt.equals("GET")) {
            get(req);
        } else if (rt.equals("HEAD")) {
            head(req);
        } else if (rt.equals("POST")) {
            post(req);
        }
        Util.log("finished handling request: " + rt, 0);
    }


    //handle POST request
    private void post(String req) {
        Util.log(req, 0);
    }

    //handle GET HEAD request
    private void head(String req) throws IOException{
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        if (fr.equals("/")) {
            fr = Server.DEFAULT;
        }else if(Files.notExists(Path.of(Main.ROOT, req))){
            fr = Server.NOTFOUND;
        }
        Path p = Path.of(Main.ROOT, req);
        String mime = Files.probeContentType(p);
        out.println("HTTP/1.1 200 OK");
        out.println("net.Server:" + Server.name + " : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + mime);
        out.println("Content-length: " + Files.size(p));
        out.flush();

    }

    //handle GET file request
    private void get(String req) throws IOException {
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        //find file requested
        if (fr.equals("/")) {
            fr = Server.DEFAULT;
        }
        //init file io
        byte[]data;
        String mime;
        Path p = Paths.get(Main.ROOT, fr);
        mime = Files.probeContentType(p);

        //check if file exists
        if (Server.cansend(p)) {
            data = Util.rf(p);
            Util.log("sending: " + fr + " mime=" + mime, 0);
        }else{
            data = Util.rf(Paths.get(Main.ROOT, Server.NOTFOUND));
            Util.log("404 " + fr + "not found. sending: " + Server.NOTFOUND + " mime=" + mime, 0);
        }


        //send headers
        out.println("HTTP/1.1 200 OK");
        out.println("net.Server:" + Server.name + " : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + mime);
        out.println("Content-length: " + data.length);
        out.println(); // blank line between headers and content, very important !
        out.flush();
        //send file
        dout.write(data, 0, data.length);
        dout.flush();
    }


    @Override
    public void run() {
        try {
            //init io
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());
            dout = new BufferedOutputStream(sock.getOutputStream());
            StringBuffer buffer = new StringBuffer();

            //parse request to buffer
            String next;
            while (!(next = in.readLine()).equals("")) {
                for (int i = 0; i < next.length(); i++) {
                    buffer.append(next.charAt(i));
                }
            }
            //copy buffer to a String

            String request = buffer.toString();

            //handle the request
            handlereq(request);
            //clean up

            System.out.println(buffer);
            //copy

            sock.close();
            in.close();
            out.close();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
