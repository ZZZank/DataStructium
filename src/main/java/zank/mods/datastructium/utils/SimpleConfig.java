package zank.mods.datastructium.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.val;

import java.io.Reader;
import java.io.Writer;

/**
 * @author ZZZank
 */
public class SimpleConfig {

    private JsonObject jsonRead = new JsonObject();
    private JsonObject jsonWrite = new JsonObject();

    public void reset() {
        jsonRead = new JsonObject();
        jsonWrite = new JsonObject();
    }

    public void read(Gson gson, Reader reader) {
        jsonRead = gson.fromJson(reader, JsonObject.class);
    }

    public void write(Gson gson, Writer writer) {
        gson.toJson(jsonWrite, writer);
    }

    public JsonPrimitive getPrimitive(String key, JsonPrimitive fallback) {
        val got = jsonRead.get(key);
        val value = got != null ? got.getAsJsonPrimitive() : fallback;
        jsonWrite.add(key, value);
        return value;
    }

    public boolean getBool(String key, boolean _default) {
        return getPrimitive(key, new JsonPrimitive(_default)).getAsBoolean();
    }

    public int getInt(String key, int _default) {
        return getPrimitive(key, new JsonPrimitive(_default)).getAsInt();
    }
}
