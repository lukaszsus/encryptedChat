package app;

import db.UserContext;
import server.ServerLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CipherChatApp {

    public static void main(String[] args){
        String dbUrl = CipherChatApp.askDbUrl();
        int port = askPort();

        UserContext userContext = new UserContext(dbUrl);
        userContext.removeAllUsers();
        ServerLogic serverLogic = new ServerLogic(userContext, port);
        serverLogic.start();

        askFinish();
        serverLogic.close();
        System.exit(0);
    }

    private static String askDbUrl(){
        System.out.println("Please enter url to sqlite database.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    private static int askPort(){
        System.out.println("Please enter port.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int port = Integer.parseInt(line);
        return port;
    }

    private static boolean askFinish(){
        System.out.println("Please enter \'q\' to finish.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while((line != null && !line.equals("q")) || line == null) {
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
