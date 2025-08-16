import java.io.*;
import java.net.*;
import java.nio.file.*;

public class TodoListApp {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server running at http://localhost:" + port);

        while (true) {
            try (Socket socket = server.accept()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();

                String requestLine = in.readLine();
                if (requestLine == null) continue;

                String fileName = "index.html"; // default page
                if (requestLine.contains("style.css")) {
                    fileName = "style.css";
                }

                File file = new File("web", fileName);
                if (file.exists()) {
                    String contentType = fileName.endsWith(".css") ? "text/css" : "text/html";
                    byte[] content = Files.readAllBytes(file.toPath());

                    out.write(("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\n\r\n").getBytes());
                    out.write(content);
                } else {
                    out.write("HTTP/1.1 404 Not Found\r\n\r\nFile not found".getBytes());
                }

                out.flush();
            }
        }
    }
}
