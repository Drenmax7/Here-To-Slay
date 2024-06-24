package code.java.view;

import code.java.model.Server;

public class HereToSlay {
    public static void main(String[] args) {
        Server server = new Server("192.168.43.167",666);
        new Thread(server::run).start();

    }
}