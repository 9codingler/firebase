import java.io.*;
import java.net.Socket;

public class FileThread extends Thread{
    Socket clientfileSocket;

    public FileThread(Socket clientfileSocket) {
        this.clientfileSocket = clientfileSocket;
    }

    public void run() {
        try {
            while(true) {
                InputStream fileinput = clientfileSocket.getInputStream();
                FileOutputStream out = null;
                DataInputStream datain = new DataInputStream(fileinput);

                String converted = datain.readUTF();
                String tmp2[] = converted.split(" ");
                String filecommand = tmp2[0];
                String fileName = tmp2[1];

                if(filecommand.equals("#PUT")) {
                    int data = datain.readInt();
                    long filesize = datain.readLong();
                    // 여기서 stream 만들어서,file 받고, myfile 객체 만들어서 fileList에 저장.
                    File file = new File(fileName);
                    out = new FileOutputStream(file);
                    byte[] buffer = new byte[64000];
                    int len = 0;
                    int total = (int)filesize;
                    while(true) {
                        len = datain.read(buffer);
                        out.write(buffer, 0, len);
                        filesize -= len;
                        if(filesize == 0) {
                            break;
                        }
                    }

                    Server.fileList.add(file);
                    System.out.println("file.length : " + total);
                }
            }
        } catch (Exception e) {

        }
    }

}
