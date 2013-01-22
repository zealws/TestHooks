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

import testhooks.test.HookManager;
import testhooks.test.HookTestlet;

public class HookTest extends HookTestlet {

    private static final String jdbc = "jdbc:postgresql://localhost:5432/testhooks?user=testhooks&password=testhooks";

    static {
        // Initialize both client and server end since we're testing both here.
        Hook.initializeDb(jdbc);
        HookManager.initializeDb(jdbc);
    }

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

    @Test
    public void successfulRequest() {
        RespHook hook = new RespHook();
        hook.send();
        assertNotNull(hook.resp);
        assertEquals(Status.SUCCESS_OK, hook.resp.getStatus());
    }

    @Test
    public void fullRequest() {
        Hook hook = new Hook("my-subsys");
        hook.add("xyz", "abc");
        hook.add("hij", "klm");
        HookManager tf = HookManager.getInstance();
        assertFalse(tf.has("my-subsys"));

        hook.send();

        assertTrue(tf.has("my-subsys"));
        assertEquals("abc", tf.get("my-subsys", "xyz"));
        assertEquals("klm", tf.get("my-subsys", "hij"));
    }
}
