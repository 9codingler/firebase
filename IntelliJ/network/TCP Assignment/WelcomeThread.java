import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeThread extends Thread {
    ServerSocket welcomeSocket;
    ServerSocket fileSocket;
    Socket clientSocket;
    ChatRoom room;
    public WelcomeThread(ServerSocket welcomeSocket, ServerSocket fileSocket) {
        this.welcomeSocket = welcomeSocket;
        this.fileSocket = fileSocket;
    }

    public void run() {
        try {
            while (true) {
                // 이거 accept때문에 blocking 되니까, 나중에 통신 thread 하나 더 만들어줘야 한다.
                String info;
                String[] tmp;
                Socket clientSocket = welcomeSocket.accept();
                Socket clientfileSocket = fileSocket.accept();

                Client client = new Client(clientfileSocket, clientSocket);

                ClientThread ct = new ClientThread(client);
                ct.start();

                FileThread ft = new FileThread(clientfileSocket);
                ft.start();
            }
        } catch (Exception e) {
            System.out.println("socket closed!");
        }
    }
}
