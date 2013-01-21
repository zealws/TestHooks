package testhooks.test;

import java.util.Hashtable;
import java.util.Map;

import testhooks.common.HookData;

public class HookManager {

    private static final HookManager singleton = new HookManager();

    private Map<String, HookData> subsystems = new Hashtable<String, HookData>();
    private HookServer server = new HookServer();
    private boolean initialized = false;

    private HookManager() {
        // Singleton
    }

    public static HookManager getInstance() {
        return singleton;
    }

    public synchronized String get(String subsys, String key) {
        if (!has(subsys))
            return null;
        return subsystems.get(subsys).get(key);
    }

    public synchronized boolean has(String subsys) {
        return subsystems.containsKey(subsys);
    }

    public synchronized boolean has(String subsys, String value) {
        if (!has(subsys))
            return false;
        return subsystems.get(subsys).has(value);
    }

    public synchronized void set(String subsys, HookData data) {
        subsystems.put(subsys, data);
    }

    public synchronized void remove(String subsys) {
        subsystems.remove(subsys);
    }

    public void startServer() {
        server.startServer();
    }

    public void stopServer() {
        server.stopServer();
    }

    public boolean initialized() {
        return initialized;
    }

    public void initialize() {
        initialized = true;
    }

    public void reset() {
        stopServer();
        initialized = false;
        subsystems = new Hashtable<String, HookData>();
    }
}
