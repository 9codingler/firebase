import java.io.*;
import java.net.Socket;

public class CListeningThread extends Thread {
    Socket clientSocket;

    public CListeningThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        String receive;
        try {
            while (true) {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // msg
                receive = inFromServer.readLine();
                if(receive != null) {
                    System.out.println(receive);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }


    }
}
