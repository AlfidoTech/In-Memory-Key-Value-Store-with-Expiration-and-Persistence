import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class PersistenceManager {
    private final KeyValueStore store;
    private final String FILE_NAME = "dump.rdb";

    public PersistenceManager(KeyValueStore store) {
        this.store = store;
        load();
    }

    public void startAutoSave() {
        while (true) {
            try {
                save();
                Thread.sleep(5 * 60 * 1000); // every 5 minutes
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(store.getStore());
            out.writeObject(store.getExpiryMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Map<String, String> data = (Map<String, String>) in.readObject();
            Map<String, Long> expiry = (Map<String, Long>) in.readObject();
            store.loadAll(data, expiry);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
