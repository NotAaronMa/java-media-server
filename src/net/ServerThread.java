
//TODO make thread reuasable
//TODO fuckin implement the POST request

package net;

import mpack.Main;
import mpack.Util;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.StringTokenizer;


public class ServerThread implements Runnable {

    //Socket
    private Socket sock;
    //io
    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream dout;


    public ServerThread(Socket s) {
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

    //handle HEAD request
    private void head(String req) throws IOException{
        StringTokenizer t1 = new StringTokenizer(req);
        t1.nextToken(" ");
        String fr = t1.nextToken(" ");
        if (fr.equals("/")) {
            fr = Server.DEFAULT;
        }else if(Files.notExists(Path.of(Main.WEB_ROOT, req))){
            fr = Server.NOTFOUND;
        }
        Path p = Path.of(Main.WEB_ROOT, req);
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
        if(!fr.contains(".")){
            fr = fr + ".html";
        }

        //init file io
        byte[]data;
        String mime;
        Path p = Paths.get(Main.WEB_ROOT, fr);
        mime = Files.probeContentType(p);

        //check if file exists
        if (Server.cansend(p)) {
            data = Util.rf(p);
            Util.log("sending: " + fr + " mime=" + mime, 0);
        }else{
            data = Util.rf(Paths.get(Main.WEB_ROOT, Server.NOTFOUND));
            Util.log("404 " + fr + "not found. sending: " + Server.NOTFOUND + " mime=" + mime, 0);
        }



        //send only requested bytes
        //int bs,bf;

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
        try{
        while(!sock.isClosed()){

            //init io
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());
            dout = new BufferedOutputStream(sock.getOutputStream());
            StringBuffer buffer = new StringBuffer();

            //parse request to buffer
            String next;
            while (!(next = in.readLine()).equals("")) {
                buffer.append(next + "\n");
            }
            //copy buffer to a String

            String request = buffer.toString();

            //handle the request
            handlereq(request);
            //clean up

            System.out.println(Thread.currentThread().getName() + " handling request: " + buffer);
            //copy
        }
        in.close();
        out.close();
        dout.close();
        sock.close();
        }catch(IOException ex){
           Util.log(ex.getMessage(), 1);
        }finally{
            Thread.currentThread().interrupt();
        }
    }
}
