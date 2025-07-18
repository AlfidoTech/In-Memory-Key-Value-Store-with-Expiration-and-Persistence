import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final KeyValueStore store;

    public ClientHandler(Socket socket, KeyValueStore store) {
        this.socket = socket;
        this.store = store;
    }

    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                String cmd = parts[0].toUpperCase();
                String response = switch (cmd) {
                    case "SET" -> {
                        if (parts.length >= 3) {
                            int ttl = 0;
                            if (parts.length == 5 && parts[3].equalsIgnoreCase("EX")) {
                                ttl = Integer.parseInt(parts[4]);
                            }
                            store.set(parts[1], parts[2], ttl);
                            yield "OK";
                        } else yield "ERR";
                    }
                    case "GET" -> store.get(parts[1]);
                    case "DEL" -> String.valueOf(store.del(parts[1]));
                    case "EXISTS" -> String.valueOf(store.exists(parts[1]));
                    case "TTL" -> String.valueOf(store.ttl(parts[1]));
                    default -> "ERR unknown command";
                };
                writer.write(response + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
