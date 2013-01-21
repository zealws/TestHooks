package testhooks.test;

import java.io.IOException;

import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.ObjectRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import testhooks.common.HookData;

public class SubsysStatusResource extends ServerResource {

    public static final String URI = "/subsys/{subsys}/status";
    public static final Method METHOD = Method.PUT;

    @Put
    public String update(Representation raw) throws IOException {

        ObjectRepresentation<HookData> input;
        try {
            input = new ObjectRepresentation<HookData>(raw);
        } catch (IllegalArgumentException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return "Could not parse input: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            setStatus(Status.SERVER_ERROR_INTERNAL);
            return "Could not find class: " + e.getMessage();
        }

        if (!getRequest().getAttributes().containsKey("subsys")) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return "Invalid subsys: could not parse";
        }
        String subsys = getRequest().getAttributes().get("subsys").toString();
        try {
            HookManager.getInstance().initialize();
            HookManager.getInstance().set(subsys, input.getObject());
            return "Status updated";
        } catch (IOException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            e.printStackTrace();
            return "Could not parse input: " + e.getMessage();
        }
    }
}
