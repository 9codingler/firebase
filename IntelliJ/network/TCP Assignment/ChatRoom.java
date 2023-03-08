import java.io.Serializable;
import java.util.ArrayList;

public class ChatRoom {
    String chatRoomName;
    ArrayList<Client> userList = new ArrayList<>();

    public ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }


    public void addUser(Client client) {
        userList.add(client);
    }

    public void quitRoom(Client client) {
        ArrayList<Client> userList = client.chatRoom.userList;
        userList.remove(client);
        client.chatRoom = null;
    }
}
