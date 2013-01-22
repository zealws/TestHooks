package testhooks.test;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import testhooks.common.HookConfig;

public class HookServer extends Application {

    public static final int DEFAULT_PORT = 10777;
    private Component server;
    private HookConfig config;
    private boolean started;

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach(SubsysStatusResource.URI, SubsysStatusResource.class);

        return router;
    }

    public void startServer() {
        if (!started) {
            server = new Component();
            server.getServers().add(Protocol.HTTP, getConfig().getPort());
            server.getDefaultHost().attach(this);
            try {
                server.start();
                started = true;
            } catch (Exception e) {
                stopServer();
                throw new RuntimeException("Exception while starting server", e);
            }
        }
    }

    public void stopServer() {
        if (started) {
            try {
                server.stop();
                started = false;
            } catch (Exception e) {
                throw new RuntimeException("Could not stop server", e);
            }
        }
    }

    public HookConfig getConfig() {
        if (config != null)
            return config;
        else {
            config = new HookConfig("localhost", DEFAULT_PORT, SubsysStatusResource.URI, SubsysStatusResource.METHOD);
            return config;
        }
    }
}
