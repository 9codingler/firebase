import java.io.*;
import java.net.Socket;

public class CWritingThread extends Thread {
    Socket clientSocket;
    Socket clientfileSocket;

    public CWritingThread(Socket clientSocket, Socket clientfileSocket) {
        this.clientSocket = clientSocket;
        this.clientfileSocket = clientfileSocket;
    }

    public void run() {
        String send;
        try {
            while(true) {
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                DataOutputStream fileToServer = new DataOutputStream(clientfileSocket.getOutputStream());
                OutputStream out = clientfileSocket.getOutputStream();
                send = inFromUser.readLine();

                String tmp[] = send.split(" ");
                String command = tmp[0];

                if(command.equals("#PUT")) {
                    // stream 만들어서 보낸다.
                    String fileName = tmp[1];
                    File file = new File(fileName);
                    FileInputStream fin = new FileInputStream(file);

                    byte[] buffer = new byte[64000];
                    int len;
                    int data = 0;
                    int total = 0;

                    while((len = fin.read(buffer)) > 0) {
                        data++;
                    }

                    fin.close();
                    fin = new FileInputStream(file);
                    buffer = fin.readAllBytes();
                    fin.close();
                    fin = new FileInputStream(file);
                    fileToServer.writeUTF(send);
                    fileToServer.writeInt(data);
                    fileToServer.writeLong(file.length());
                    long filesize = file.length();
                    int offset = 0;
                    System.out.print("sending : ");
                    for(; data > 0; data--) {
                        if(filesize > 64000) {
                            len = 64000;
                        }
                        else {
                            len = (int)filesize;
                        }
                        total += len;
                        filesize -= len;
                        out.write(buffer, offset, len);
                        offset += len;
                        System.out.print("#");
                        if(filesize == 0) {
                            break;
                        }
                    }

                    System.out.println();
                    System.out.println(fileName + " , " + total + "bytes sending complete!");
                    continue;
                }

                if(!command.startsWith("#") | command.equals("#CREATE") | command.equals("#JOIN") | command.equals("#EXIT") | command.equals("#STATUS") | command.equals("#GET")) {
                    outToServer.writeBytes(send + '\n');
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
