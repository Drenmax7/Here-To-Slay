package code.java.network;

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
    public boolean connexion(){
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to server " + hostname);
            this.socket = socket;

            this.output = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            new Thread(() -> listen(input)).start();

            return true;
        }
        catch (UnknownHostException exception) {
            System.out.println("Server not found: " + exception.getMessage());
        }
        catch (IOException exception) {
            System.out.println("I/O error: " + exception.getMessage());
        }
        return false;
    }

    public void closeConnexion(){
        try {
            sendData(new Packet("closing"));
            close = true;
            socket.close();
        } catch (IOException exception) {
            System.err.println("Error while closing the connexion: " + exception.getMessage());
        }
    }

    public void listen(ObjectInputStream input){
        while (!close){
            try {
                Packet receivedObject = (Packet) input.readObject();

                if (receivedObject.name.equals("closing")) {
                    System.out.println("Closing connexion on server demand");
                    socket.close();
                    close = true;
                }
                else{
                    System.out.println(receivedObject.name);
                }
            }
            catch (ClassNotFoundException | IOException exception) {
                if (exception.getMessage().equals("Connection reset")) close = true;
                else if (!close) System.err.println("Error while receiving data: " + exception.getMessage());
            }
        }
    }

    public void sendData(Packet packet){
        if (output == null) {
            System.out.println("Cannot send data without properly connected to server first");
            return;
        }

        if (close){
            System.out.println("Cannot send data if connexion is closed");
            return;
        }

        try {
            output.writeObject(packet);
            output.flush();
        } catch (IOException exception) {
            System.out.println("I/O error while sending data: " + exception.getMessage());
        }
    }

    public boolean isConnected() {return !close;}
}