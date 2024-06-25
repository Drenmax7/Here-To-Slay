package code.java.view;

import code.java.network.Server;

public class HereToSlay {
    public static void main(String[] args) {
        Server server = new Server(666);
        new Thread(server::run).start();

    }
}