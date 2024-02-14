package Server;

import java.util.ArrayList;

public interface ServerInterface {
    void startServer();

    void stopServer();

    void sendMessage(String message);

    ArrayList<String> getMessageHistory();
}
