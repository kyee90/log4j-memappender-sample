package nz.kyee.log4jmemappender;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent.Builder;
import org.apache.logging.log4j.message.SimpleMessage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonAdapter implements JsonSerializer<Log4jLogEvent>, JsonDeserializer<Log4jLogEvent> {
    @Override
    public JsonElement serialize(Log4jLogEvent src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        Date datetime = new Date(src.getTimeMillis());
        String dtf = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()).format(datetime.toInstant());
        obj.addProperty("name", src.getLoggerName());
        obj.addProperty("level",src.getLevel().toString());
        obj.addProperty("timestamp", dtf);
        obj.addProperty("thread", src.getThreadName());
        obj.addProperty("message", src.getMessage().toString());
        return obj;
    }

    @Override
    public Log4jLogEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jobj = json.getAsJsonObject();
        String time = jobj.get("timestamp").getAsString();
        String level = jobj.get("level").getAsString();
        Logger cat = LogManager.getLogger(jobj.get("name").getAsString());
        LocalDateTime date = LocalDateTime.parse(time, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
        long longtime = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Level lev = getLevelObj(level);
        Builder levent = new Log4jLogEvent.Builder();
        levent.setLoggerName(cat.getName());
        levent.setLevel(lev);
        levent.setMessage(new SimpleMessage(jobj.get("message").getAsString()));
        levent.setTimeMillis(longtime);
        return levent.build();
    }    

    Level getLevelObj(String level){        
        if(level.equals("ALL")){return Level.ALL;}
        if(level.equals("DEBUG")){return Level.DEBUG;}
        if(level.equals("ERROR")){return Level.ERROR;}
        if(level.equals("FATAL")){return Level.FATAL;}
        if(level.equals("INFO")){return Level.INFO;}
        if(level.equals("OFF")){return Level.OFF;}
        if(level.equals("WARN")){return Level.WARN;}
        return Level.ALL;
    }
}