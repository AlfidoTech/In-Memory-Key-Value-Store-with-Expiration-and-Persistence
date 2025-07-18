import java.util.concurrent.*;
import java.util.*;
import java.util.logging.*;

public class KeyValueStore {
    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> expiryMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Logger logger = Logger.getLogger(KeyValueStore.class.getName());

    public KeyValueStore() {
        scheduler.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.SECONDS);
    }

    public synchronized void set(String key, String value, int ttl) {
        store.put(key, value);
        if (ttl > 0) {
            expiryMap.put(key, System.currentTimeMillis() + ttl * 1000);
        } else {
            expiryMap.remove(key);
        }
        logger.info("SET " + key + " = " + value + " TTL=" + ttl);
    }

    public String get(String key) {
        if (isExpired(key)) {
            del(key);
            return null;
        }
        return store.get(key);
    }

    public synchronized boolean del(String key) {
        expiryMap.remove(key);
        return store.remove(key) != null;
    }

    public boolean exists(String key) {
        return !isExpired(key) && store.containsKey(key);
    }

    public long ttl(String key) {
        if (!expiryMap.containsKey(key)) return -1;
        long remaining = expiryMap.get(key) - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : -2;
    }

    private boolean isExpired(String key) {
        if (!expiryMap.containsKey(key)) return false;
        return System.currentTimeMillis() > expiryMap.get(key);
    }

    private void cleanup() {
        expiryMap.keySet().removeIf(this::isExpired);
    }

    public ConcurrentHashMap<String, String> getStore() {
        return store;
    }

    public ConcurrentHashMap<String, Long> getExpiryMap() {
        return expiryMap;
    }

    public void loadAll(Map<String, String> data, Map<String, Long> expiry) {
        store.clear();
        expiryMap.clear();
        store.putAll(data);
        expiryMap.putAll(expiry);
    }
}
