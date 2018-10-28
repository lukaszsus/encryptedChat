package server;

import java.util.ArrayList;
import java.util.List;

public class ServerLogic {

    private ServerThread serverThread;
    private List<ClientThread> clientThreads;

    public ServerLogic(){
        clientThreads = new ArrayList<>();
        serverThread = new ServerThread(this);
    }

    public void start(){
        serverThread.start();
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public int getPort() {
        return 15000;       // TODO
    }
}
