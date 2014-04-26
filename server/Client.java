package server;

import model.database.CardDatabase;
import model.database.MysqlDatabase;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hyice on 4/26/14.
 */
public class Client extends Thread{

    private Socket socket;

    public Client(Socket aSocket) {

        socket = aSocket;
    }

    public void run() {

        try {
            System.out.println("连接来自"+socket.getInetAddress());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),"GBK")));
            String line;
            Message msgrcvd = new Message();
            Message msgsend;
            while ( (line = in.readLine()) != null ) {
                System.out.println("-->"+line);
                if ( msgrcvd.parse(line) ) {

                    String sid = CardDatabase.studentIdByCardId(msgrcvd.getText());

                    if(sid.equals("")) {

                        msgsend = new Message(msgrcvd.getSrc(), "SRV", 'D', "卡号信息不存在，请先去登记卡号！");
                    }else {

                        msgsend = new Message(msgrcvd.getSrc(),"SRV",
                                'D', "测试");
                    }

                    out.print(msgsend.generate());
                    out.flush();
                    System.out.println("<--"+msgsend.generate());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(socket.getInetAddress()+"已断开");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
