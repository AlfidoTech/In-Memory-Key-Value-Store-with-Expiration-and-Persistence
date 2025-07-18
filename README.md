# In-Memory Key-Value Store (Java Redis Clone)

## ✅ Features
- In-memory key-value store
- Expiration (`EX seconds`)
- RDB-style persistence (`dump.rdb`)
- Multi-threaded TCP server (Redis-like)
- Thread-safe (using ConcurrentHashMap)
- Logging and error handling

## 📦 Commands
- `SET key value [EX seconds]` – store with optional expiration
- `GET key` – retrieve value
- `DEL key` – delete key
- `EXISTS key` – check if key exists
- `TTL key` – get time-to-live (in seconds)

## 🚀 Getting Started

### Compile & Run
```bash
javac *.java
java KeyValueStoreApp
Client Example
Use telnet or nc:

bash
Copy
Edit
telnet localhost 6379
Then try:

sql
Copy
Edit
SET mykey hello EX 10
GET mykey
TTL mykey
DEL mykey
🧪 Testing
Write JUnit tests for:

CRUD methods in KeyValueStore

TTL, EXISTS edge cases

Persistence read/write

Full integration via socket

📁 Persistence
File: dump.rdb saved every 5 mins. Auto-loaded on startup.

🧠 Internals
ConcurrentHashMap ensures thread safety

ScheduledExecutorService handles expiration

ObjectOutputStream for persistence

🔐 Bonus Ideas
Add AUTH password support

Add PUBLISH / SUBSCRIBE PubSub channels

Built with ❤️ in Java 11

yaml
Copy
Edit

---

Would you like me to now write unit tests and integration tests?
