package testhooks.common;

import java.util.Hashtable;
import java.util.Map;

public class HookData {

    private Map<String, String> data;

    public HookData() {
        this.data = new Hashtable<String, String>();
    }

    public HookData(Map<String, String> data) {
        this.data = data;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public String get(String key) {
        if (has(key))
            return data.get(key);
        else
            return null;
    }

    public void put(String key, String value) {
        this.data.put(key, value);
    }

    public String asString() {
        StringEncoder enc = new StringEncoder();
        for (String key : data.keySet()) {
            enc.add(key, data.get(key));
        }
        return enc.toString();
    }

    public static HookData fromString(String full) {
        StringEncoder enc = new StringEncoder();
        return new HookData(enc.read(full));
    }

}
