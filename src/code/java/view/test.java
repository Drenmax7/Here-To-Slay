package code.java.view;

import code.java.model.Client;

public class test {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("192.168.43.167",666);
        client.connexion();

        while (true){
            client.sendData("client 2");
            System.out.println("message envoye");
            Thread.sleep(500);
        }
    }
}
