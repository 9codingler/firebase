import java.net.Socket;
import java.util.ArrayList;

public class CLists {
    public static ArrayList<ChatRoom> chatRoomList = new ArrayList<>();

    // #JOIN에서 사용하는 method.
    public static ChatRoom findRoom(String chatRoomName) {
        // 나중에 이거 지우기.
        for(ChatRoom r : CLists.chatRoomList) {
            if(chatRoomName.equals(r.chatRoomName)) {
                System.out.println("found room " + r.chatRoomName);
                return r;
            }
        }
        System.out.println("Cannot find room..");
        return null;
    }
}
