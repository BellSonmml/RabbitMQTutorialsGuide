package rose.simplepattern.subscribe.direct;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectEmitLogSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        DirectEmitLog directEmitLog = new DirectEmitLog("direct_log", connection, "direct");
        directEmitLog.publishSeverityLevelMessage("error","this is error msg");
        directEmitLog.publishSeverityLevelMessage("info","this is info msg");
        directEmitLog.publishSeverityLevelMessage("warn","this is warn msg");
        directEmitLog.publishSeverityLevelMessage("error","this is error msg");
        directEmitLog.publishSeverityLevelMessage("info","this is info msg");
        directEmitLog.publishSeverityLevelMessage("warn","this is warn msg");
        connection.close();
    }
}
