package testhooks.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HookDataTest {

    @Test
    public void formatsCorrectly() {
        String raw = ": :: ::: ::::";
        assertEquals(":! :!:! :!:!:! :!:!:!:!", StringEncoder.encode(raw));
    }

    @Test
    public void deformatsCorrectly() {
        String raw = ":! :!:! :!:!:! :!:!:!:!";
        assertEquals(": :: ::: ::::", StringEncoder.decode(raw));
    }

    @Test
    public void formatAndDeformat() {
        String raw = "a:b:c:de::f:g::h::i::::";
        assertEquals(raw, StringEncoder.decode(StringEncoder.encode(raw)));
    }

    @Test
    public void asStringsCorrectly() {
        HookData data = new HookData();
        data.put("key1", "val1");
        data.put("key:", "val:");
        data.put("key5::7", "val81:");
        String raw = "key5:!:!7:=val81:!\nkey1:=val1\nkey:!:=val:!\n";
        assertEquals(raw, data.asString());
    }

    @Test
    public void fromStringsCorrectly() {
        String raw = "key5:!:!7:=val81:!\nkey1:=val1\nkey:!:=val:!\n";
        HookData data = HookData.fromString(raw);
        assertEquals("val1", data.get("key1"));
        assertEquals("val:", data.get("key:"));
        assertEquals("val81:", data.get("key5::7"));
    }

    @Test
    public void fromEmptyString() {
        String raw = "";
        // Just make sure it doesn't throw an exception
        HookData.fromString(raw);
    }
}
