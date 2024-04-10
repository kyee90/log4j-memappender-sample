package nz.kyee.log4jmemappender;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonAdapter implements JsonSerializer<LoggingEvent>, JsonDeserializer<LoggingEvent> {
    @Override
    public JsonElement serialize(LoggingEvent src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        Date datetime = new Date(src.getTimeStamp());
        String dtf = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()).format(datetime.toInstant());
        obj.addProperty("name", src.getLoggerName());
        obj.addProperty("level",src.getLevel().toString());
        obj.addProperty("timestamp", dtf);
        obj.addProperty("thread", src.getThreadName());
        obj.addProperty("message", src.getRenderedMessage());
        return obj;
    }

    @Override
    public LoggingEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jobj = json.getAsJsonObject();
        String time = jobj.get("timestamp").getAsString();
        String level = jobj.get("level").getAsString();
        Logger cat = Logger.getLogger(jobj.get("name").getAsString());
        LocalDateTime date = LocalDateTime.parse(time, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()));
        long longtime = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Level lev = getLevelObj(level);
        LoggingEvent levent = new LoggingEvent(null, cat, longtime, lev, jobj.get("message").getAsString(), jobj.get("thread").getAsString(), null, null, null, null); 
        return levent;
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