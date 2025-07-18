import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

public class TCPServer {
    private final KeyValueStore store;
    private static final int PORT = 6379;
    private final Logger logger = Logger.getLogger(TCPServer.class.getName());

    public TCPServer(KeyValueStore store) {
        this.store = store;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, store)).start();
                logger.info("New client connected: " + socket);
            }
        } catch (IOException e) {
            logger.severe("Error: " + e.getMessage());
        }
    }
}
