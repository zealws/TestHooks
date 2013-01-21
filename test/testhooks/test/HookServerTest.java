package testhooks.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;

import testhooks.test.HookServer;
import testhooks.test.SubsysStatusResource;
import testhooks.test.HookManager;

public class HookServerTest {

    private HookServer srv = new HookServer();

    @Before
    public void setup() {
        srv.startServer();
    }

    @After
    public void cleanup() {
        srv.stopServer();
    }

    @Test
    public void resourceEnabled() {
        HookManager fw = HookManager.getInstance();
        assertFalse(fw.has("my-subsys"));

        Client client = new Client(Protocol.HTTP);
        String uri = "http://localhost:" + srv.getConfig().getPort() + SubsysStatusResource.URI.replace("{subsys}", "my-subsys");
        Request request = new Request(Method.PUT, uri);
        request.setEntity("xyz:=abc", MediaType.TEXT_PLAIN);
        Response resp = client.handle(request);
        assertEquals(resp.getEntityAsText(), Status.SUCCESS_OK, resp.getStatus());

        assertTrue(fw.has("my-subsys"));
        assertEquals("abc", fw.get("my-subsys", "xyz"));
    }
}
