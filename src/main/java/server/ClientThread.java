package server;

import jsonParser.JsonMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean work;

    private ServerLogic serverLogic;

    public ClientThread(Socket socket, ServerLogic serverLogic) {
        this.socket = socket;
        this.serverLogic = serverLogic;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    @Override
    public void run() {
        work = true;
        try {
            input = new ObjectInputStream(socket.getInputStream());

            while (work) {
                String message = (String) input.readObject();
                System.out.println(message);
                JsonMessage jsonMessage = new JsonMessage(message);
                jsonMessage = jsonMessage.doAction(serverLogic, socket);
                System.out.println(jsonMessage.toString());

                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(jsonMessage.toString());
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        serverLogic.finishConnection(socket);

        try {
            socket.close();
        } catch (IOException e) {
        }
        try {
            output.close();
        } catch (IOException e) {
        }
        try {
            input.close();
        } catch (IOException e) {
        }
    }
}
