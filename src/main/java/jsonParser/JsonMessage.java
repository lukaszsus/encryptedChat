package jsonParser;

import server.ServerLogic;
import org.json.*;

import java.net.Socket;

import static jsonParser.MessageType.*;

public class JsonMessage {

    MessageType msgType = UNKNOWN;
    String p0 = null;
    String p1 = null;
    String p2 = null;
    String p3 = null;

    public MessageType getMsgType() {
        return msgType;
    }

    public String getP0() {
        return p0;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getP3() {
        return p3;
    }

    public JsonMessage(String message){
        JSONObject obj = new JSONObject(message);
        this.p0 = obj.getString("P0");
        this.p1 = obj.getString("P1");
        this.p2 = obj.getString("P2");
        this.p3 = obj.getString("P3");
        setMessageType();
    }

    public JsonMessage(String p0, String p1, String p2, String p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        setMessageType();
    }

    public JsonMessage(String p0, String p1, String p2) {
        this(p0, p1, p2, null);
    }

    public JsonMessage(String p0, String p1) {
        this(p0, p1, null, null);
    }

    public JsonMessage(MessageType msgType, String p1, String p2, String p3) {
        this.msgType = msgType;
        this.p0 = msgType.toString();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public JsonMessage(MessageType msgType, String p1, String p2) {
        this(msgType, p1, p2, null);
    }

    public JsonMessage(MessageType msgType, String p1){
        this(msgType, p1, null, null);
    }

    public JsonMessage doAction(ServerLogic serverLogic, Socket socket) {
        switch(msgType){
            case REG:
                return serverLogic.RegResponse(p1,p2,p3);
            case LOGIN:
                return serverLogic.LoginResponse(socket, p1, p2);
            case TEXT:
                return serverLogic.TextResponse(socket, this);
            case LIST:
                break;
            case FIND:
                break;
            case LOGOUT:
                break;
            case PONG:
                break;
            case PING:
                break;
            default:
                System.out.println("Message type is unknown.");
                break;
        }
        return new JsonMessage(MessageType.UNKNOWN, null, null, null);
    }

    public String toString(){
        JSONObject obj = new JSONObject();

        obj.put("P0", p0);
        obj.put("P1", p1);
        obj.put("P2", p2);
        obj.put("P3", p3);

        return obj.toString();
    }

    private void setMessageType(){
        String messageType = this.p0;
        switch(messageType){
            case "REG":
                msgType = REG;
                break;
            case "LOGIN":
                msgType = LOGIN;
                break;
            case "TEXT":
                msgType = TEXT;
                break;
            case "LIST":
                msgType = LIST;
                break;
            case "FIND":
                msgType = FIND;
                break;
            case "LOGOUT":
                msgType = LOGIN;
                break;
            case "PONG":
                msgType = PONG;
                break;
            case "PING":
                msgType = PING;
                break;
            default:
                throw new IllegalArgumentException("Protocol doesn't define this type of message.");
        }
    }
}
