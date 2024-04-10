package nz.kyee.log4jmemappender;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonLayoutTest {

    @BeforeEach
    public void initEach() {
        BasicConfigurator.configure();
    }

    @Test
    public void JsonLayoutTest1() {
        Layout testlayout = new JsonLayout();
        assertNotNull(testlayout);
    }

    @Test
    public void JsonLayoutTest2() {
        Layout testlayout = new JsonLayout();
        String name = "TestLayout";
        String message = "Testing Message!";
        Logger log = Logger.getLogger(name);
        Logger root = log.getRootLogger();
        Appender app = (Appender) root.getAllAppenders().nextElement();
        app.setLayout(testlayout);
        root.warn(message);
    }

    // Parse JSON produced and compare parsed data with the attributes
    // of the original log events used for test
    @Test
    public void JsonLayoutTest3() {
        GsonBuilder gson = new GsonBuilder();
        JsonAdapter jadapt = new JsonAdapter();
        gson.registerTypeAdapter(LoggingEvent.class, jadapt);
        Gson customGson = gson.create();
        Layout testlayout = new JsonLayout();
        StringWriter testwriter = new StringWriter();
        WriterAppender testapp = new WriterAppender(testlayout, testwriter);
        String name = "TestLayout";
        Level level = Level.DEBUG;
        String message = "Testing Message!";
        Logger log = Logger.getLogger(name);
        log.addAppender(testapp);
        Logger root = log.getRootLogger();
        Appender app = (Appender) root.getAllAppenders().nextElement();
        app.setLayout(testlayout);
        log.debug(message);
        String jsontest = testwriter.toString();
        LoggingEvent jsonparse = customGson.fromJson(jsontest, LoggingEvent.class);
        assertEquals(jsonparse.getLoggerName(), name);
        assertEquals(jsonparse.getLevel(), level);
        assertEquals(jsonparse.getRenderedMessage(), message);
    }
}
