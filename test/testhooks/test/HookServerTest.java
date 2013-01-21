package testhooks.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.ObjectRepresentation;

import testhooks.common.HookData;

public class HookServerTest extends HookTestlet {

    public HookServerTest() {
        setInitTime(-1);
    }

    @Test
    public void resourceEnabled() {
        assertFalse(hookManager.has("my-subsys"));

        HookData data = new HookData();
        data.put("xyz", "abc");

        Client client = new Client(Protocol.HTTP);
        String uri = "http://localhost:10777" + SubsysStatusResource.URI.replace("{subsys}", "my-subsys");
        Request request = new Request(Method.PUT, uri);
        request.setEntity(new ObjectRepresentation<HookData>(data));
        Response resp = client.handle(request);
        assertEquals(resp.getEntityAsText(), Status.SUCCESS_OK, resp.getStatus());

        assertTrue(hookManager.has("my-subsys"));
        assertEquals("abc", hookManager.get("my-subsys", "xyz"));
    }
}
