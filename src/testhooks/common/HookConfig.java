package testhooks.common;

import java.util.Map;

import org.restlet.data.Method;

public class HookConfig {

    private static final HookConfig defaultConfig = new HookConfig("localhost", 10777, "/subsys/{subsys}/status", Method.PUT);

    private int port;
    private Method method;
    private String hostname;
    private String uri;

    public HookConfig() {
        // Do Nothing
    }

    public HookConfig(String hostname, int port, String uri, Method method) {
        this.hostname = hostname;
        this.port = port;
        this.uri = uri;
        this.method = method;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static HookConfig fromString(String data) {
        StringEncoder enc = new StringEncoder();
        Map<String, String> results = enc.read(data);

        HookConfig conf = new HookConfig();
        conf.setPort(Integer.parseInt(results.get("port")));
        conf.setHostname(results.get("hostname"));
        conf.setUri(results.get("uri"));
        if (results.get("method").equalsIgnoreCase("PUT"))
            conf.setMethod(Method.PUT);
        else if (results.get("method").equalsIgnoreCase("POST"))
            conf.setMethod(Method.POST);
        return conf;
    }

    public String asString() {
        StringEncoder enc = new StringEncoder();
        enc.add("hostname", hostname);
        enc.add("port", port);
        enc.add("uri", uri);
        enc.add("method", method);
        enc.add("hostname", hostname);
        return enc.toString();
    }

    public static HookConfig getDefaultConfig() {
        return defaultConfig;
    }

    public String getFullUri(String subsys) {
        return String.format("http://" + hostname + ":" + port + uri.replace("{subsys}", subsys));
    }

}
