import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.SocketImpl;
import java.nio.BufferOverflowException;
import java.util.Scanner;

public class Client {
    String userName;
    ChatRoom chatRoom;
    Socket clientSocket;
    Socket clientfileSocket;


    public Client(Socket clientfileSocket, Socket clientSocket) {
        this.clientfileSocket = clientfileSocket;
        this.clientSocket = clientSocket;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public static void main(String[] args) {
        String chatRoomName;
        String userName;
        Socket clientSocket = null;
        Socket fileSocket = null;
        Scanner scanner = new Scanner(System.in);

        String ipAdress = args[0];
        String tmp2 = args[1];
        String tmp3 = args[2];

        int port1 = Integer.parseInt(tmp2);
        int port2 = Integer.parseInt(tmp3);
//        String nulls = scanner.nextLine();
        System.out.println("port 1 : " + port1 + " , " + "port 2 : " + port2);
        System.out.println("#CREATE or #JOIN chatRoomName userName");

        try {
            clientSocket = new Socket(ipAdress, port1);
            fileSocket = new Socket(ipAdress, port2);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // receive, send threads.
        CListeningThread l = new CListeningThread(clientSocket);
        CWritingThread w = new CWritingThread(clientSocket, fileSocket);
        CFileThread f = new CFileThread(fileSocket);

        w.start();
        l.start();
        f.start();
    }
}


