package nz.kyee.log4jmemappender;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.Test;

public class MemAppenderTest {

    @Test
    public void MemAppenderTest1() throws Exception{
        MemAppender testapp = new MemAppender();
        long newsize = 600;
        assertEquals(testapp.getMaxSize(),1000);
        testapp.setMaxSize(newsize);
        assertEquals(testapp.getMaxSize(), newsize);
        testapp.close();
    }    

    @Test
    public void MemAppenderTest2() throws Exception {
        MemAppender testapp = new MemAppender();
        List<LoggingEvent> loglist = testapp.getCurrentLogs();
        assertNotNull(loglist);
        long discardedlogs = testapp.getDiscardedLogCount();
        assertNotNull(discardedlogs);
    }    

    @Test
    public void MemAppenderTest3() throws Exception{
        MemAppender testapp = new MemAppender();
        String name = "TestName";
        testapp.setName(name);
        String checkname = testapp.getName();
        assertEquals(name, checkname);
    } 
    
    @Test
    public void MemAppenderTest4() throws Exception {
        String testJSON = "export-JSON";
        MemAppender memapp = new MemAppender();
        BasicConfigurator.configure(memapp);
        Logger logtest = Logger.getLogger("TestLogger");
        logtest.debug("Testing export to JSON!");
        memapp.exportToJSON(testJSON);
        String[] logs = memapp.getLogs();
        assertNotNull(logs);
        assertEquals(memapp.getLogCount(), 1);
        File file = new File(testJSON+".json");
        assertTrue(file.isFile());
        }    

    @Test
    public void MemAppenderTest5() throws Exception{
        MemAppender testapp = new MemAppender();
        boolean bool = testapp.requiresLayout();
        assertFalse(bool);
    }

    @Test
    public void MemAppenderTest6() throws Exception{
        MemAppender testapp = new MemAppender();
        testapp.setLayout(new HTMLLayout());
        BasicConfigurator.configure(testapp);
        String loggername = "TesterLogger";
        String message = "This is an test for INFO!";
        Logger logtest = Logger.getLogger(loggername);
        logtest.error(message);
        List<LoggingEvent> logvents = testapp.getCurrentLogs();
        LoggingEvent checklog = logvents.get(0);
        assertEquals(checklog.getLoggerName(), loggername);
        assertEquals(checklog.getRenderedMessage(), message);
    }

    @Test
    public void MemAppenderTest7() throws Exception{
        MemAppender testapp = new MemAppender();
        testapp.setLayout(new HTMLLayout());
        BasicConfigurator.configure(testapp);
        String loggername = "TesterLogger";
        String message = "This is an test for INFO!";
        Logger logtest = Logger.getLogger(loggername);
        logtest.info(message);
        List<LoggingEvent> logvents = testapp.getCurrentLogs();
        LoggingEvent checklog = logvents.get(0);
        assertEquals(checklog.getLoggerName(), loggername);
        assertEquals(checklog.getRenderedMessage(), message);
    }

    @Test
    public void MemAppenderTest8() throws Exception{
        MemAppender testapp = new MemAppender();
        testapp.setLayout(new HTMLLayout());
        BasicConfigurator.configure(testapp);
        String loggername = "TesterLogger";
        String message = "This is an test for INFO!";
        Logger logtest = Logger.getLogger(loggername);
        logtest.fatal(message);
        List<LoggingEvent> logvents = testapp.getCurrentLogs();
        LoggingEvent checklog = logvents.get(0);
        assertEquals(checklog.getLoggerName(), loggername);
        assertEquals(checklog.getRenderedMessage(), message);
    }

}