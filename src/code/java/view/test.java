package code.java.view;

import code.java.network.Client;
import code.java.network.Packet;

public class test {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("192.168.43.167",666);

        boolean connecte = false;
        while (!connecte){
            connecte = client.connexion();
        };

        for (int i = 0; i < 10; i++) {
            client.sendData(new Packet("client 1"));
            Thread.sleep(500);
        }

        client.closeConnexion();
    }
}
