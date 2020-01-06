package lizzy.medium.compare.helidon;

import io.helidon.microprofile.server.Server;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@ApplicationScoped
@ApplicationPath("/")
public class Main extends javax.ws.rs.core.Application {
    /**
     * Cannot be instantiated.
     */
    private Main() { }

    /**
     * Application main entry point.
     * @param args command line arguments
     * @throws IOException if there are problems reading logging properties
     */
    public static void main(final String[] args) throws IOException {
        // load logging configuration
        setupLogging();

        // start the server
        Server server = startServer();

        System.out.println("http://localhost:" + server.port() + "/issue");
    }

    /**
     * Start the server.
     * @return the created {@link Server} instance
     */
    private static Server startServer() {
        // Server will automatically pick up configuration from
        // microprofile-config.properties
        // and Application classes annotated as @ApplicationScoped
        return Server.create().start();
    }

    /**
     * Configure logging from logging.properties file.
     */
    private static void setupLogging() throws IOException {
        try (InputStream is = Main.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        }
    }
}
