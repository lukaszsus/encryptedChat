package server;

import db.UserContext;
import jsonParser.JsonMessage;
import jsonParser.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerLogic {

    private UserContext userContext;
    private ServerThread serverThread;
    private List<ClientThread> clientThreads;
    private Map<String, Socket> clientSockets;

    public ServerLogic(UserContext userContext){
        this.userContext = userContext;
        clientThreads = new ArrayList<>();
        serverThread = new ServerThread(this);
        clientSockets = new HashMap<>();
    }

    public void start(){
        serverThread.start();
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public Map<String, Socket> getClientSockets() {
        return clientSockets;
    }

    public int getPort() {
        return 15000;       // TODO
    }

    public JsonMessage RegResponse(String login, String pass1, String pass2){
        if(isLoginUnique(login)){
            if(pass1.equals(pass2)) {
                addNewUser(login, pass1);
                return new JsonMessage(MessageType.REG, new String("true"), new String("Registration succeeded."));
            } else{
                return new JsonMessage(MessageType.REG, new String("false"), new String("Passwords don't match."));
            }
        } else{
            return new JsonMessage(MessageType.REG, new String("false"), new String("Login is already in the database."));
        }
    }

    private boolean isLoginUnique(String login){
        return userContext.isLoginUnique(login);
    }

    private void addNewUser(String login, String pass1){
        userContext.addUser(login, pass1);
    }

    public JsonMessage LoginResponse(Socket socket, String login, String pass){
        if(isLoginAndPasswordCorrect(login, pass)){
            clientSockets.put(login, socket);
            System.out.println(String.format("Login: %s, Socket: %s", login, socket.toString()));
            return new JsonMessage(MessageType.LOGIN, new String("true"), new String("Login succeeded."));
        }else{
            return new JsonMessage(MessageType.LOGIN, new String("false"), new String("Login or password is incorrect."));
        }
    }

    private boolean isLoginAndPasswordCorrect(String login, String pass){
        return userContext.isPasswordCorrect(login, pass);
    }

    public JsonMessage TextResponse(Socket socket, JsonMessage message){
        if(isSocketAuthorized(socket, message.getP1())){
            if(isUserLoggedIn(message.getP2())){
                sendMessage(message);
                return new JsonMessage(MessageType.TEXT, new String("true"), new String("Succeeded."));
            }
            else{
                return new JsonMessage(MessageType.TEXT, new String("false"), new String("Recipient is not logged."));
            }
        } else{
            return new JsonMessage(MessageType.TEXT, new String("false"), new String("You are not correctly logged in."));
        }
    }

    private boolean isSocketAuthorized(Socket socket, String login){
        if(clientSockets.containsKey(login) && clientSockets.get(login).equals(socket)){
            return true;
        }
        return false;
    }

    private boolean isUserLoggedIn(String login){
        return clientSockets.containsKey(login);
    }

    private void sendMessage(JsonMessage message){
        Socket socket = clientSockets.get(message.getP2());
        try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())){
            output.writeObject(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize(){
        serverThread.setWork(false);
        for(ClientThread ct : clientThreads){
            ct.setWork(false);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
