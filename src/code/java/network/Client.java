package code.java.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that permit connexion to the server.
 * Its purpose is to receive information about the game to update the game state on screen
 * It also sends the user inputs to the server for him to proceed the player turn
 *
 * @see Server
 * @see Packet
 */
public class Client {
    //Attributes

    /**
     * The state of the connexion
     */
    private boolean close;

    /**
     * Ip address of the server
     */
    private final String hostname;

    /**
     * Server's port
     */
    private final int port;

    /**
     * The socket being use on the connexion
     */
    private Socket socket;

    /**
     * The stream on which information can be put to be read by the server
     */
    private ObjectOutputStream output;

    //Constructor

    /**
     * Create a new client object
     *
     * @param hostname ip address of the server
     * @param port server's port
     */
    public Client(String hostname, int port){
        close = false;
        this.hostname = hostname;
        this.port = port;
    }

    //Methods

    /**
     * Create a new connexion with the server
     *
     * @return if the connexion has been successful or not
     */
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

    /**
     * Close the connexion with the server
     */
    public void closeConnexion(){
        try {
            sendData(new Packet("closing"));
            close = true;
            socket.close();
        } catch (IOException exception) {
            System.err.println("Error while closing the connexion: " + exception.getMessage());
        }
    }

    /**
     * Listen to the input stream and fetch data that are sent on it
     * Then process the data into the view
     * Should be run in a thread
     *
     * @param input the input stream to be listened
     */
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

    /**
     * Send the packet onto the output stream for the server to receive it
     *
     * @param packet the data to send
     */
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

    /**
     * Return the connexion state
     *
     * @return the connexion state
     */
    public boolean isConnected() {return !close;}
}