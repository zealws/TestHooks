package testhooks.source;

import java.util.Map;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.data.Protocol;
import org.restlet.representation.ObjectRepresentation;

import testhooks.common.HookConfig;
import testhooks.common.HookData;

public class Hook {

    private HookData data;
    private String subsys;

    public Hook(String subsys) {
        this.subsys = subsys;
        this.data = new HookData();
    }

    public Hook(String subsys, Map<String, String> data) {
        this.subsys = subsys;
        this.data = new HookData(data);
    }

    public void add(String key, Object value) {
        data.put(key, value.toString());
    }

    public void send() {
        if (HookConfig.fromDb())
            send(HookConfig.getInstance());
    }

    public void send(HookConfig conf) {
        Request request = new Request(conf.getMethod(), conf.getFullUri(subsys));
        request.setEntity(new ObjectRepresentation<HookData>(data));
        handle(request);
    }

    protected void handle(Request request) {
        Client client = new Client(Protocol.HTTP);
        client.handle(request);
        try {
            client.stop();
        } catch (Exception e) {
            throw new RuntimeException("Could not stop client", e);
        }
    }

    public static void initializeDb(String jdbc) {
        HookConfig.initializeDb(jdbc);
    }
}
