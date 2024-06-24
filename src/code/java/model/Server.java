package code.java.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    /**
     * Run until close is set to true
     */
    private boolean close;
    private final int port;
    /**
     * The stream on wich data sent by clients are put
     */
    private ServerSocket serverSocket;

    private final ArrayList<Socket> connectedClient;
    private final ArrayList<ObjectOutputStream> clientOutput;

    public Server(String hostname, int port) {
        this.port = port;
        connectedClient = new ArrayList<>();
        clientOutput = new ArrayList<>();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            close = false;
            System.out.println("Server is running on port " + port);


            // As long as the server is open, it accepts connections
            while (!close) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("New connection from " + socket.getInetAddress().getHostAddress());

                    connectedClient.add(socket);
                    clientOutput.add(output);

                    new Thread(() -> listen(input)).start();

                } catch (IOException exception) {
                    if (!close) System.err.println("Error while accepting connexion: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while starting the server: " + exception.getMessage(), exception);
        }
    }

    public void stop() {
        close = true;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server is stopped");
        } catch (IOException exception) {
            System.err.println("Error while stopping the server: " + exception.getMessage());
        }
    }

    public void listen(ObjectInputStream input){
        while (!close){
            try {
                Object receivedObject = input.readObject();
                System.out.println(receivedObject);
            }
            catch (ClassNotFoundException | IOException exception) {
                if (!close) System.err.println("Error while receiving data: " + exception.getMessage());
            }
        }
    }
}