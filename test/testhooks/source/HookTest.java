package testhooks.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Protocol;
import org.restlet.data.Status;

import testhooks.test.HookServer;
import testhooks.test.HookManager;

public class HookTest {

    private static class RespHook extends Hook {

        public Response resp;

        public RespHook() {
            super("my-subsys");
        }

        @Override
        protected void handle(Request request) {
            Client client = new Client(Protocol.HTTP);
            resp = client.handle(request);
        }

    }

    private static class NoServHook extends Hook {

        public Request request;

        public NoServHook() {
            super("my-subsys");
        }

        @Override
        protected void handle(Request request) {
            this.request = request;
        }

    }

    @Test
    public void successfulRequest() {
        HookServer server = new HookServer();
        try {
            server.startServer();
            RespHook hook = new RespHook();
            hook.send();
            assertNotNull(hook.resp);
            assertEquals(Status.SUCCESS_OK, hook.resp.getStatus());
        } finally {
            server.stopServer();
        }
    }

    @Test
    public void sendsData() {
        NoServHook hook = new NoServHook();
        hook.add("xyz", "abc");
        hook.add("hij", "klm");
        hook.send();
        assertNotNull(hook.request);
        assertEquals("xyz:=abc\nhij:=klm\n", hook.request.getEntityAsText());
    }

    @Test
    public void fullRequest() {
        HookServer server = new HookServer();
        try {
            server.startServer();
            Hook hook = new Hook("my-subsys");
            hook.add("xyz", "abc");
            hook.add("hij", "klm");
            HookManager tf = HookManager.getInstance();
            assertFalse(tf.has("my-subsys"));

            hook.send();

            assertTrue(tf.has("my-subsys"));
            assertEquals("abc", tf.get("my-subsys", "xyz"));
            assertEquals("klm", tf.get("my-subsys", "hij"));
        } finally {
            server.stopServer();
        }
    }
}
