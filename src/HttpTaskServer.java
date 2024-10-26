import com.sun.net.httpserver.HttpServer;
import http.TaskHandler;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

import static service.Managers.getDefault;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager tm) throws IOException {
        TaskHandler taskHandler = new TaskHandler(tm);
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", taskHandler);
        httpServer.createContext("/epics", taskHandler);
        httpServer.createContext("/subtasks", taskHandler);
        httpServer.createContext("/history", taskHandler);
        httpServer.createContext("/prioritized", taskHandler);
    }

    public void start() {
        httpServer.start();
        System.out.println("Server has been started");
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("end");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(getDefault());
        server.start();
    }
}
