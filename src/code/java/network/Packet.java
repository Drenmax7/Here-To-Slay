package code.java.network;

import java.io.Serializable;

/**
 * The data structure that is sent between the server and clients
 *
 * @see Server
 * @see Client
 */
public class Packet implements Serializable {
    public String name;

    public Packet(String name){
        this.name = name;
    }
}
