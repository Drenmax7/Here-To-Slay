package code.java.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    //Attributes
    private boolean close;
    private final String hostname;
    private final int port;
    private Socket socket;
    private ObjectOutputStream output;

    //Constructor
    public Client(String hostname, int port){
        close = false;
        this.hostname = hostname;
        this.port = port;
    }

    //Methods
    public void connexion(){
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to server " + hostname);
            this.socket = socket;

            this.output = new ObjectOutputStream(socket.getOutputStream());;

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> listen(input));
        }
        catch (UnknownHostException exception) {
            System.out.println("Server not found: " + exception.getMessage());
        }
        catch (IOException exception) {
            System.out.println("I/O error: " + exception.getMessage());
        }
    }

    public void closeConnexion(){
        try {
            close = true;
            socket.close();
        } catch (IOException exception) {
            System.err.println("Error while closing the connexion: " + exception.getMessage());
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

    public void sendData(String message){
        if (output == null) {
            System.out.println("Cannot send data without properly connected to server first");
            return;
        }
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException exception) {
            System.out.println("I/O error while sending data: " + exception.getMessage());
        }
    }
}