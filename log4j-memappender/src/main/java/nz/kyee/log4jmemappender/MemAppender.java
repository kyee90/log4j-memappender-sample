package nz.kyee.log4jmemappender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;


public class MemAppender extends AbstractAppender implements MemAppenderMBean {
    private long maxSize = 1000;
    private long discarded = 0;
    private ArrayList<LogEvent> events = new ArrayList<>();

    /*public MemAppender() throws MalformedObjectNameException, MBeanRegistrationException, NotCompliantMBeanException {
        
    }*/

    public void append(LogEvent event){
        if (events.size() == maxSize){
            events.subList(0,99).clear();
            discarded += 100;
        }
        events.add(event);
    }

    public void exportToJSON(String fileName) throws IOException {
        StringBuilder jsonout = new StringBuilder();
        for (LogEvent event: this.getCurrentLogs()){
            jsonout.append(new JsonLayout().format(event));
        }
        File file = new File(fileName+".json");
        file.createNewFile();
        Writer writer = new FileWriter(file);
        writer.write(jsonout.toString());
        writer.close();
    }

    public List<LogEvent> getCurrentLogs(){
        return Collections.unmodifiableList(events);
    }

    public long getDiscardedLogCount(){
        return discarded;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public long getMaxSize() {
        return this.maxSize;
    }

        public void close() {
    }

    
    public boolean requiresLayout() {
        return false;
    }

    @Override
    public String[] getLogs() {
        String[] logs = new String[events.size()];
        for (int i=0;i<events.size();i++){
            PatternLayout patt = new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN);
            logs[i] = patt.format(events.get(i));
        }
        return logs;
    }

    @Override
    public long getLogCount() {
        return(this.events.size()-this.discarded);
    }
    
}
