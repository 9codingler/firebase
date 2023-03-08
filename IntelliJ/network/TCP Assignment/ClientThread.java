import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{
    Client client;
    PrintWriter writer;
    public ClientThread(Client client) {
        this.client = client;
    }

    public void run() {
        try {
            while(true) {
                InputStream input = client.clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                DataOutputStream filetoClient = new DataOutputStream(client.clientfileSocket.getOutputStream());

                String msg = reader.readLine();
                String[] tmp = msg.split(" ");
                String command = tmp[0];

                if(command.equals("#EXIT")) {
                    client.chatRoom.quitRoom(client);
                    continue;
                }

                if(command.equals("#CREATE")) {
                    String chatRoomName = tmp[1];
                    OutputStream output = client.clientSocket.getOutputStream();
                    if(CLists.findRoom(chatRoomName) != null) {
                        writer = new PrintWriter(output, true);
                        writer.println("There already exists room! re-type!!");
                        continue;
                    }
                    String userName = tmp[2];
                    client.setUserName(userName);
                    ChatRoom room = new ChatRoom(chatRoomName);
                    client.setChatRoom(room);
                    CLists.chatRoomList.add(room);
                    room.addUser(client);
                    writer = new PrintWriter(output, true);
                    writer.println("Succesfully Created " + chatRoomName + " !!");
                    continue;
                }

                if(command.equals("#JOIN")) {
                    OutputStream output = client.clientSocket.getOutputStream();
                    String chatRoomName = tmp[1];
                    ChatRoom room = CLists.findRoom(chatRoomName);
                    if(room == null) {
                        writer = new PrintWriter(output, true);
                        writer.println("There is no room! re-type!!");
                        continue;
                    }
                    String userName = tmp[2];
                    client.setUserName(userName);
                    client.setChatRoom(room);
                    room.addUser(client);
                    writer = new PrintWriter(output, true);
                    writer.println("Succesfully joined " + chatRoomName + " !!");
                    continue;
                }

                if(command.equals("#STATUS")) {
                    boolean firstPrint = true;
                    OutputStream out = client.clientSocket.getOutputStream();
                    String Info = "room name : " + client.chatRoom.chatRoomName;
                    writer = new PrintWriter(out);

                    Info = Info + " | user name : ";
                    for(Client c : client.chatRoom.userList) {
                        if(firstPrint) {
                            Info = Info + c.userName;
                            firstPrint = false;
                            continue;
                        }
                        Info = Info + " , " + c.userName;
                    }
                    writer.write(Info);
                    writer.println();
                    writer.flush();
                    continue;
                }

                if(command.equals("#GET")) {
                    String fileName = tmp[1];
                    // filelist에 있는 file을 보내준다.
                    // file -> byte buffer -> send.
                    // send할 땐 client의 outputstream으로.
                    File sendfile = null;
                    boolean fileflag = false;
                    for(File f : Server.fileList) {
                        if(fileName.equals(f.getName())) {
                            sendfile = f;
                            System.out.println("found " + f.getName());
                            fileflag = true;
                            break;
                        }
                    }

                    if(!fileflag) {
                        OutputStream output = client.clientSocket.getOutputStream();
                        writer = new PrintWriter(output, true);
                        writer.println("there is no file!");
                        continue;
                    }
                    FileInputStream fin = new FileInputStream(sendfile);
                    DataOutputStream dataout = new DataOutputStream(client.clientSocket.getOutputStream());

                    // file -> byte buffer.
                    byte[] buffer = new byte[64000];
                    int len;
                    int data = 0;

                    while((len = fin.read(buffer)) > 0) {
                        data++;
                    }

                    fin = new FileInputStream(sendfile);
                    filetoClient.writeUTF(sendfile.getName());
                    filetoClient.writeInt(data);
                    filetoClient.writeLong(sendfile.length());
                    int total = 0;
                    for(; data > 0; data--) {
                        len = fin.read(buffer);
                        total += len;
                        filetoClient.write(buffer, 0, len);
                    }
                    continue;
                }

                ArrayList<Client> userList = client.chatRoom.userList;
                for(Client anotherclient : userList) {
                    if(client.clientSocket.equals(anotherclient.clientSocket)) {
                        continue;
                    }
                    OutputStream out = anotherclient.clientSocket.getOutputStream();
                    writer = new PrintWriter(out, true);
                    msg = this.client.userName + " : " + msg;
                    writer.println(msg);
                }
            }

        } catch(Exception e) {
            System.out.println("socket closed!");
        }
    }
}
