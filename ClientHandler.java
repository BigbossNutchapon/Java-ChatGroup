import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        try {
            // Create reader and writer to communicate with the client
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            // Read the username sent by the client
            username = reader.readLine();
            System.out.println("New client connected: " + username);
            // Broadcast a message to all clients to notify that a new client has joined the chat
            server.broadcastMessage(username + " has joined the chat.");

            String message;
            // Read messages from the client and broadcast them to all clients
            while ((message = reader.readLine()) != null) {
                server.broadcastMessage(username + ": " + message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            try {
                // Close the reader, writer, and client socket
                reader.close();
                writer.close();
                clientSocket.close();
                // Remove the client handler from the server's list of client handlers
                server.removeClient(this);
                // Broadcast a message to all clients to notify that the client has left the chat
                server.broadcastMessage(username + " has left the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send a message to the client
    public void sendMessage(String message) {
        try {
            writer.write(message + "\n"); //write message in buffer
            writer.flush();  // ส่งข้อความไปทันทีไม่ต้องรอ buffer เต็ม
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}