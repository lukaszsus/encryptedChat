package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerLogicTest {

    private ServerLogic serverLogic;
    ClientSocket cs1;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        serverLogic = new ServerLogic();
        serverLogic.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cs1 = new ClientSocket(15000);
    }

    @Test
    void testCommunication(){
        cs1.sendMessage(new String("Ala ma kota."));

        assertTrue(true);
    }

}