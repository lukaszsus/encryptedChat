package app;

import db.UserContext;
import server.ServerLogic;

import java.util.Scanner;

public class CipherChatApp {

    public static void main(String[] args){
        String dbUrl = CipherChatApp.askDbUrl();
        int port = askPort();

        UserContext userContext = new UserContext(dbUrl);
        userContext.removeAllUsers();
        ServerLogic serverLogic = new ServerLogic(userContext, port);
        serverLogic.start();
    }

    private static String askDbUrl(){
        System.out.println("Please enter url to sqlite database.");
        Scanner reader = new Scanner(System.in);
        String url = reader.nextLine();
        reader.close();
        return url;
    }

    private static int askPort(){
        System.out.println("Please enter port.");
        Scanner reader = new Scanner(System.in);
        int port = reader.nextInt();
        reader.close();
        return port;
    }
}
