import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

//     Uncomment this block to pass the first stage

     try {
       ServerSocket serverSocket = new ServerSocket(4221);

       // Since the tester restarts your program quite often, setting SO_REUSEADDR
       // ensures that we don't run into 'Address already in use' errors
         serverSocket.setReuseAddress(true);
        Socket socket = serverSocket.accept();
         BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String requestLine = br.readLine();
         String str="/";
         int len=0;
         Map<String, String> headers = new HashMap<>();
         String path ="/";
         System.out.println(requestLine);
         if(requestLine!=null){
             String [] parts = requestLine.split(" ");
             if (parts.length >= 2) {
                 path = parts[1];
             }
         }
         System.out.println(path);
         if("/".equals(path) || path.contains("/echo/")) {
             if(path.contains("/echo/")) {
                 str = path.substring(6);
                 len = str.length();
             }
             System.out.println(str+" "+len);
             String responseBody= "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "+len+"\r\n\r\n"+str;
             System.out.println(responseBody);
             socket.getOutputStream().write(responseBody.getBytes());
         }
         else if("/user-agent".equals(path)){
             String line;
             while ((line=br.readLine())!=null && !line.isEmpty()){
                 String []headerParts = line.split(":",2);
                 // split at first :
                 if(headerParts.length==2){
                     // as we have to store in map so checking if it has been split into 2 parts
                     headers.put(headerParts[0].trim(),headerParts[1].trim());
                 }
             }
             str=headers.get("User-Agent");
             len=str.length();
             String responseBody= "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "+len+"\r\n\r\n"+str;
             System.out.println(responseBody);
             socket.getOutputStream().write(responseBody.getBytes());
         }
         else{
             socket.getOutputStream().write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
         }
       System.out.println("           new connection");
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
