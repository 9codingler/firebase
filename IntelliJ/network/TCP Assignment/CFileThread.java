import java.io.*;
import java.net.Socket;

public class CFileThread extends Thread{
    Socket clientfileSocket;

    public CFileThread(Socket clientfileSocket) {
        this.clientfileSocket = clientfileSocket;
    }

    public void run() {
        try {
            DataInputStream datain = new DataInputStream(clientfileSocket.getInputStream());
            String fileName = "copy" + datain.readUTF();
            int data = datain.readInt();
            long filesize = datain.readLong();

            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[64000];
            int len;
            int total = 0;
            System.out.print("receiving : ");
            for(; data > 0; data--) {
                len = datain.read(buffer);
                total += len;
                out.write(buffer, 0, len);
                System.out.print("#");
                filesize -= len;
                if(filesize == 0) {
                    break;
                }
            }
            System.out.println();
            System.out.println(fileName + " " + total + "bytes receiving complete!!");
        } catch(IOException e) {
            e.printStackTrace();
        }


    }
}
