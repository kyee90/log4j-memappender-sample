package nz.kyee.log4jmemappender;

import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class JsonLayout extends EnhancedPatternLayout  {
    
    public String format(LoggingEvent event) {
        Gson gson = new GsonBuilder()
        .registerTypeAdapter(LoggingEvent.class, new JsonAdapter())
        .create();
        String gs = gson.toJson(event);
        return gs;
    }  
}
