package net;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import mpack.Main;
import mpack.Util;
//TODO fuckin implement the POST request


public class Response implements Runnable {


    private Socket sock;

    public Response(Socket s) {
        sock = s;
    }

    public static void handlereq(String req, PrintWriter out, BufferedOutputStream bout) throws IOException {

        StringTokenizer t = new StringTokenizer(req); //tokenizer for the whole request
        String rt = new StringTokenizer(t.nextToken("\n")).nextToken();
        Util.log("Handling request: " + req, 0);
        if (rt.equals("GET")) {
            get(req, out, bout);
        } else if (rt.equals("HEAD")) {
            head(req, out);
        } else if (rt.equals("POST")) {

        }
        Util.log("finished handling request: " + rt, 0);
    }

    //handle POST request
    private static void post(String req) {
       Util.log(req, 0);
    }

    //handle GET HEAD request
    private static void head(String req, PrintWriter out) {
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");

        if (fr.equals("/")) {
            fr = Server.DEFAULT;
        }
        File f = new File(Server.ROOT, fr);
        String mime = "text/html";
        out.println("HTTP/1.1 200 OK");
        out.println("net.Server:" + Server.name + " : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + mime);
        out.println("Content-length: " + f.length());
        out.flush();

    }

    //handle GET file request
    private static void get(String req, PrintWriter out, BufferedOutputStream dout) throws IOException {
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        //find file requested
        if (fr.equals("/")) {
            fr = Server.DEFAULT;
        }
        //init file io
        File f = new File(Server.ROOT, fr);
        String mime = "";
        byte[] data;
        //check if file exists
        if (f.exists()) {
            mime = Files.probeContentType(Paths.get(f.getPath()));
            data = Util.rf(f, (int) f.length());
                Util.log("sending: " + f + " mime=" + mime, 0);
        } else {
            data = Util.rf(new File(Server.ROOT, "404.html"), (int) f.length());
            mime = "text/html";

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
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream());
            BufferedOutputStream bout = new BufferedOutputStream(sock.getOutputStream());
            ArrayList<Character> buffer = new ArrayList<>(64);

            //parse request to buffer
            String next;
            while (!(next = in.readLine()).equals("")) {
                for (int i = 0; i < next.length(); i++) {
                    buffer.add(next.charAt(i));
                }
            }
            //copy buffer to a String
            char[] b = new char[buffer.size()];
            for (int i = 0; i < buffer.size(); i++) {
                b[i] = buffer.get(i);
            }
            String request = new String(b);
            //handle the request
            handlereq(request, out, bout);
            //clean up



            sock.close();
            in.close();
            out.close();
            bout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
