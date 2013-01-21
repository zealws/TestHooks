package testhooks.common;

import java.util.Hashtable;
import java.util.Map;

public class StringEncoder {

    private StringBuilder s = new StringBuilder();
    private String infix = ":=";
    private String suffix = "\n";

    public void add(String key, String value) {
        s.append(encode(key));
        s.append(infix);
        s.append(encode(value));
        s.append(suffix);
    }

    public void add(String key, Object value) {
        add(key, value.toString());
    }

    @Override
    public String toString() {
        return s.toString();
    }

    public Map<String, String> read(String full) {
        Map<String, String> result = new Hashtable<String, String>();
        String[] data = full.split("\n");
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(""))
                continue;
            String[] raw = data[i].split(":=");
            if (raw.length != 2)
                throw new StringIndexOutOfBoundsException("Could not parse string: '" + data[i] + "'");
            String key = decode(raw[0]);
            String value = decode(raw[1]);
            result.put(key, value);
        }
        return result;
    }

    static String encode(String string) {
        return string.replace(":", ":!");
    }

    static String decode(String string) {
        return string.replace(":!", ":");
    }

}
