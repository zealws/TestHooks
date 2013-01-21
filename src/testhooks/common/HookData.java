package testhooks.common;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class HookData implements Serializable {

    private static final long serialVersionUID = 2898942161309585959L;

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

}
