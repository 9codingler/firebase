import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    public static ArrayList<File> fileList = new ArrayList<>();
    public static void main(String[] args) {

        String tmp1 = args[0];
        String tmp2 = args[1];

        int port1 = Integer.parseInt(tmp1);
        int port2 = Integer.parseInt(tmp2);

        try {
            ServerSocket welcomeSocket = new ServerSocket(port1);
            ServerSocket fileSocket = new ServerSocket(port2);

            WelcomeThread wt = new WelcomeThread(welcomeSocket, fileSocket);
            wt.start();
        } catch(Exception e) {
            System.out.println("socket closed!");
        }
    }
}
