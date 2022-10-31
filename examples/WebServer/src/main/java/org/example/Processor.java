package org.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;


public class Processor {
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public int fib(int num) {
        if (num < 2) return num;

        return (fib(num - 1) + fib(num - 2));
        //        int a = 0, b = 1, c, i = 1;
        //        while(i++ <= num){
        //            c = a + b;
        //            a = b;
        //            b = c;
        //        }
        //        return a;

    }

    public String createFile() {
        String fileName = request.getRequestLine().replaceAll("GET /create/", "");
        fileName = fileName.replaceAll(" HTTP/1.1", "");

        // create a file object for the current location
        File file = new File("/Users/User/Rauana/dc-course"+ fileName + ".txt");


        try {
            boolean value = file.createNewFile();
            if (value) {
                return("New "+ fileName + ".txt file is created: /Users/User/Rauana/dc-course" + fileName);
            }
            else {
                return ("The file already exists.");
            }
        }
        catch(Exception e) {
            e.getStackTrace();
        }

        return fileName;
    }

    public String deleteFile() {
        String fileName = request.getRequestLine().replaceAll("GET /delete/", "");
        fileName = fileName.replaceAll(" HTTP/1.1", "");


        File myObj = new File("/Users/User/Rauana/dc-course"+ fileName + ".txt");

        if (myObj.delete()) {
            return "Successfully deleted the file: " + myObj.getName();
        } else{
            return "Failed to delete the file.";
        }
    }


    public void process() throws IOException {
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();

        PrintWriter output = new PrintWriter(socket.getOutputStream());


        if (request.getRequestLine().contains("GET /exec/")) {
            String num = request.getRequestLine().replaceAll("GET /exec/", "");
            num = num.replaceAll(" HTTP/1.1", "");

            int n = Integer.parseInt(num);

            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Fibonacci</title></head>");
            output.println("<body><p>Fibonacci of " + n + ": <b>" + fib(n) + "</b></p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }

        else if (request.getRequestLine().contains("GET /create/")) {
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Create txt file</title></head>");
            output.println("<body><p>File Created</p></body>");
            output.println("<body><p>" + createFile() +"</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }


        else if (request.getRequestLine().contains("GET /delete/")) {
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>DELETE</title></head>");
            output.println("<body><p>Delete File</p></body>");
            output.println("<body><p>" + deleteFile() +"</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }

        else {
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Hello</title></head>");
            output.println("<body><p>Hello, world!</p></body>");
            output.println("</html>");
            output.flush();

            socket.close();
        }

    }


}