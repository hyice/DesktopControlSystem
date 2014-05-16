package server;

import database.CardDatabase;
import database.ClassroomDatabase;
import database.ServerDatabase;
import main.view.BindCardWithSidWindow;
import utilities.Utilities;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created by hyice on 4/26/14.
 */
public class Client extends Thread{

    private Socket socket;

    private Message msgrcvd;
    private Message msgsend;

    private BufferedReader in;
    private PrintWriter out;

    private String sid;
    private int cid;
    private String ip;

    public Client(Socket aSocket) {

        socket = aSocket;
    }

    public void run() {

        try {
            System.out.println("连接来自"+socket.getInetAddress());
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),"GBK")));
            String line = in.readLine();
            msgrcvd = new Message();

            System.out.println("-->"+line);
            if ( msgrcvd.parse(line) ) {

                sid = CardDatabase.studentIdByCardId(msgrcvd.getText());
                ip = socket.getInetAddress().toString().substring(1);
                calculateCid();

                char type = msgrcvd.getType();

                switch (type) {

                    case 'A':

                        System.out.println("handle type A msg.");
                        dealWithTypeAMsg();
                        break;
                    case 'B':

                        System.out.println("handle type B msg.");
                        dealWithTypeBMsg();
                        break;
                    case 'E':

                        System.out.println("handle type E msg.");
                        dealWithTypeEMsg();
                        break;

                    default:

                        System.out.println("ERROR: Unknown type!");
                        break;
                }

                if(msgsend!=null) {

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

    private void calculateCid(){

        if(msgrcvd.getSrc().equals("MEN")) {

            cid = ClassroomDatabase.getCidByGuardIp(ip);
        }else {

            cid = ClassroomDatabase.getCidByForwardIp(ip);
        }

        if(cid==0) {

            System.out.println("ERROR: classroom ip has error!");
        }

    }

    private void dealWithTypeAMsg() {

        boolean canUse = false;
        String sendMsg = "";

        // first check whether card and student number has been associated
        if(sid.equals("")) {

            canUse = false;
            sendMsg = "卡号信息不存在，请先去登记卡号！";
        }else {

            // second, deal msg from guard or communication system in separate way
            if(msgrcvd.getSrc().equals("MEN")) {

                // from guard system
                // 1.check whether has lecture now
                if(ServerDatabase.hasLectureInClassroomNow(msgrcvd.getText(), cid)) {

                    canUse = true;
                    System.out.println("has lecture");

                }else {

                    // 2.check the temp seat
                    canUse = ServerDatabase.hasBeenTempOpenInClassroomNow(
                            msgrcvd.getText(), cid);

                }

                if(canUse) {

                    // 3.calculate the seat number

                    // check whether has already assigned
                    int seatNumber = ServerDatabase.seatNumberIfAlreadyAssigned(sid, cid);
                    if(seatNumber==ServerDatabase.NO_SEAT) {

                        // if not, assign a new One
                        int totalSeats = ClassroomDatabase.seatsOfClassroom(cid);

                        List<Integer> occupiedSeats = ServerDatabase.getOccupiedSeatsList(cid);

                        for(int i=1;i<=totalSeats;i++) {

                            if(!occupiedSeats.contains(i)) {

                                seatNumber = i;
                                ServerDatabase.assignStudentToSeat(sid, cid, seatNumber);
                                break;
                            }
                        }
                    }

                    if(seatNumber==ServerDatabase.NO_SEAT) {

                        sendMsg = "对不起，座位已满。请联系管理人员";
                    }else {

                        sendMsg = "请前往座位"+ Utilities.half2Fullchange(String.valueOf(seatNumber))+"就座";
                    }

                }else {

                    sendMsg = "对不起，你无法使用此教室！";
                }

            }else {
                // from communication system
                int seatNumber = ServerDatabase.seatNumberIfAlreadyAssigned(sid, cid);

                if(seatNumber!=ServerDatabase.NO_SEAT &&
                        seatNumber == Integer.valueOf(msgrcvd.getSrc())) {

                    canUse = true;
                }
            }


        }

        msgsend = new Message(msgrcvd.getSrc(),"SRV",canUse?'C':'D', sendMsg);
    }

    private void dealWithTypeBMsg() {

        if(!msgrcvd.getSrc().equals("MEN")) {

            ServerDatabase.studentLeaveSeat(sid, cid, Integer.valueOf(msgrcvd.getSrc()));
        }
        msgsend = new Message(msgrcvd.getSrc(),"SRV",'C', "");

    }

    private void dealWithTypeEMsg() {

        if(msgrcvd.getSrc().equals("ADD")) {

            BindCardWithSidWindow window = BindCardWithSidWindow.getInstance();
            window.setCardId(msgrcvd.getText());
            window.setVisible(true);
        }else {

            System.out.println("Error: type E msg with Src = " + msgrcvd.getSrc());
        }

    }
}
