import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    //Port
    private static final int PORT = 20000;
    //HashSet เก็บข้อมูลที่ไม่ซ้ำกันไม่มีลำดับ
    private Set<ClientHandler> clientHandlers = new HashSet<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { //new port
            System.out.println("Server started on port " + PORT);

            while (true) { //waitting for connect
                // Accept client connection & return client socket
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create new client handler for the connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);

                // Add the client handler to the set of client handlers
                clientHandlers.add(clientHandler);

                // Start the client handler in a new thread
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Broadcast a message to all connected clients
    public void broadcastMessage(String message) {
        synchronized (clientHandlers) { //เพื่อป้องกันการเข้าถึงพร้อมกันและการแก้ไขเซต clientHandlers จากเธรดอื่น ๆ
            for (ClientHandler clientHandler : clientHandlers) { 
                clientHandler.sendMessage(message); //send message ไปเรื่อยๆจนครบตาม set
            }
        }
    }

    // Remove a client handler from the set of client handlers
    public void removeClient(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
    }

    public static void main(String[] args) {
        // Create and start the server
        Server server = new Server();
        server.start();
    }
}