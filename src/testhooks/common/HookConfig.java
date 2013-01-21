package testhooks.common;

import org.restlet.data.Method;

public class HookConfig {

    private static final HookConfig defaultConfig = new HookConfig("localhost", 10777, "/subsys/{subsys}/status", Method.PUT);
    private static final HookConfig singleton = new HookConfig("localhost", 10777, "/subsys/{subsys}/status", Method.PUT);

    private int port;
    private Method method;
    private String hostname;
    private String uri;

    private HookConfig(String hostname, int port, String uri, Method method) {
        this.hostname = hostname;
        this.port = port;
        this.uri = uri;
        this.method = method;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized Method getMethod() {
        return method;
    }

    public synchronized void setMethod(Method method) {
        this.method = method;
    }

    public synchronized String getHostname() {
        return hostname;
    }

    public synchronized void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public synchronized String getUri() {
        return uri;
    }

    public synchronized void setUri(String uri) {
        this.uri = uri;
    }

    public synchronized String getFullUri(String subsys) {
        return String.format("http://" + hostname + ":" + port + uri.replace("{subsys}", subsys));
    }

    public static HookConfig getDefaultConfig() {
        return defaultConfig;
    }

    public static HookConfig getInstance() {
        return singleton;
    }

    public static void initialize(String hostname, int port, String uri, Method method) {
        singleton.setHostname(hostname);
        singleton.setPort(port);
        singleton.setUri(uri);
        singleton.setMethod(method);
    }

}
