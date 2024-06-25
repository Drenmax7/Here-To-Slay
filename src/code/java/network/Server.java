package code.java.network;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    //Attributes
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
    private final ReentrantLock lock;

    //Constructor
    public Server(int port) {
        this.port = port;
        connectedClient = new ArrayList<>();
        clientOutput = new ArrayList<>();
        lock = new ReentrantLock();
    }

    //Methods
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

                    new Thread(() -> listen(socket,input)).start();

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
            sendData(new Packet("closing"));
            for (Socket socket : connectedClient){
                socket.close();
            }
            System.out.println("Server is stopped");
        } catch (IOException exception) {
            System.err.println("Error while stopping the server: " + exception.getMessage());
        }
    }

    public void listen(Socket socket, ObjectInputStream input) {
        while (!socket.isClosed()){
            try {
                Packet receivedObject = (Packet) input.readObject();

                if (receivedObject.name.equals("closing")) {
                    System.out.println("Closing connexion on client demand");
                    socket.close();
                }
                else {
                    sendData(new Packet("new message : " + receivedObject.name));
                    System.out.println(receivedObject.name);
                }
            }
            catch (ClassNotFoundException exception) {
                System.out.println("Error while receiving data: " + exception.getMessage());

            }
            catch (IOException exception){
                try {
                    System.out.println("IO error, socket is connexion is closing" + exception.getMessage());
                    socket.close();
                }
                catch (IOException exception2){
                    System.out.println("Error while closing client connexion" + exception2.getMessage());
                }
            }
        }

        lock.lock();
        try {
            int index = connectedClient.indexOf(socket);
            connectedClient.remove(index);
            clientOutput.remove(index);
        } finally {
            lock.unlock();
        }
    }

    public void sendData(Packet packet){
        //clientOutput.removeIf(Objects::isNull);
        for (ObjectOutputStream output : clientOutput){
            try {
                output.writeObject(packet);
                output.flush();
            } catch (IOException exception) {
                System.out.println("I/O error while sending data: " + exception.getMessage());
            }
        }

    }
}