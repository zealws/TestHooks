package testhooks.test;

import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import testhooks.common.HookData;

public class SubsysStatusResource extends ServerResource {

    public static final String URI = "/subsys/{subsys}/status";
    public static final Method METHOD = Method.PUT;

    @Put
    public String update(String input) {
        if (!getRequest().getAttributes().containsKey("subsys")) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return "Invalid subsys: could not parse";
        }
        String subsys = getRequest().getAttributes().get("subsys").toString();
        try {
            HookManager.getInstance().initialize();
            if (input == null)
                HookManager.getInstance().remove(subsys);
            else
                HookManager.getInstance().set(subsys, HookData.fromString(input));
            return "Status updated";
        } catch (RuntimeException e) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            e.printStackTrace();
            return "Could not parse HookData from body";
        }
    }
}
