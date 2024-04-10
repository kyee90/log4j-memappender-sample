package nz.kyee.log4jmemappender;

import java.io.IOException;

public interface MemAppenderMBean {
    public String[] getLogs();
    public long getLogCount();
    public long getDiscardedLogCount();
    public void exportToJSON(String fileName) throws IOException;
}
