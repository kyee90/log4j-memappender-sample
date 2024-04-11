package nz.kyee.log4jmemappender;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class JsonLayout  {
    
    public String format(Log4jLogEvent event) {
        Gson gson = new GsonBuilder()
        .registerTypeAdapter(Log4jLogEvent.class, new JsonAdapter())
        .create();
        String gs = gson.toJson(event);
        return gs;
    }  
}
