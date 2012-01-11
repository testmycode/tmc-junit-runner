package fi.helsinki.cs.tmc.testrunner;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class StackTraceSerializer implements InstanceCreator<StackTraceElement>, JsonSerializer<StackTraceElement> {
    public StackTraceElement createInstance(Type type) {
        return new StackTraceElement("", "", "", 0);
    }

    public JsonElement serialize(StackTraceElement ste, Type type, JsonSerializationContext jsc) {
        JsonObject obj = new JsonObject();
        obj.addProperty("declaringClass", ste.getClassName());
        obj.addProperty("methodName", ste.getMethodName());
        obj.addProperty("fileName", ste.getFileName());
        obj.addProperty("lineNumber", ste.getLineNumber());
        return obj;
    }
}
