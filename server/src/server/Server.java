package server;

import database.HistoryDatabase;
import database.TempOpenDatabase;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hyice on 4/26/14.
 */
public class Server extends Thread{

    private final int PORT = 4321;

    public void run() {

        clearDatabase();

        try {

            ServerSocket server = new ServerSocket(PORT);

            System.out.println("服务器启动，在" + PORT + "监听");
            while (true) {
                Socket socket = server.accept();
                Client client = new Client(socket);
                client.start();
            }

        }catch (IOException e) {

            System.out.println(e.getMessage());
        }


    }

    private void clearDatabase() {

        TempOpenDatabase.clearTempOpenRecordHasDued();
        HistoryDatabase.fixErrorHistory24HoursBefore();
    }
}
