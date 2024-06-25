package code.java.network;

import java.io.Serializable;

public class Packet implements Serializable {
    public String name;

    public Packet(String name){
        this.name = name;
    }
}
