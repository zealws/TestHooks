package testhooks.test;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.restlet.data.Method;

import testhooks.common.HookConfig;
import testhooks.common.HookData;

public class HookManager {

    private static final HookManager singleton = new HookManager();

    private HookConfig conf = new HookConfig("localhost", 10777, "/subsys/{subsys}/status", Method.PUT);
    private Map<String, HookData> subsystems = new Hashtable<String, HookData>();
    private HookServer server = new HookServer();
    private Set<String> jdbcs = new HashSet<String>();

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

    public HookConfig getConf() {
        return conf;
    }

    public void setConf(HookConfig conf) {
        this.conf = conf;
    }

    public void reset() {
        stopServer();
        subsystems = new Hashtable<String, HookData>();
    }

    public void addJdbc(String jdbc) {
        if (!jdbcs.contains(jdbc))
            jdbcs.add(jdbc);
    }

    public static void initializeDb(String jdbc) {
        singleton.addJdbc(jdbc);
    }

    public void propagateConf() {
        for (String jdbc : jdbcs)
            conf.toDb(jdbc);
    }

    public void cleanDbs() {
        for (String jdbc : jdbcs)
            HookConfig.cleanAll(jdbc);
    }
}
