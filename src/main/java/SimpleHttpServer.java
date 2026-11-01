import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SimpleHttpServer {

    private static final Logger LOGGER = Logger.getLogger(SimpleHttpServer.class.getName());

    public static void main(String[] args) throws IOException {
        // 1. Create an HttpServer instance listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 2. Create a context mapping the "/" URI path to a custom handler
        server.createContext("/", new RootHandler());
        server.createContext("/orm", new OrmHandler());
        // 3. Set a default executor for handling threads
        server.setExecutor(null);

        // 4. Start the server
        LOGGER.info("Server started on port 8080");
        server.start();
    }

    public static double epley(double weight, int reps) {
        return weight * (1 + reps / 30.0);
    }
    public static double brzycki(double weight, int reps){
        return weight * (36.0/(37.0 - reps));
    }
    public static double lombardi(double weight, int reps) {return weight * Math.pow(reps, 0.10);}

    // Custom handler to process incoming HTTP requests
    static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            LOGGER.info("Request: " + exchange.getRequestURI() + " from " + exchange.getRemoteAddress());
            String response = "RepMax server -(Lombardi Added) V5. Use /orm?weight=225&reps=5";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public static class OrmHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            LOGGER.info("Request: /orm?" + query + " from " + exchange.getRemoteAddress());

            String response;
            int status = 200;
            try {
                String[] parts = query.split("&");
                String weightText = parts[0].split("=")[1];
                String repsText = parts[1].split("=")[1];
                double weight = Integer.parseInt(weightText);
                int reps = (int) Double.parseDouble(repsText);

                if (reps < 1 || reps > 15) {
                    LOGGER.warning("reps=" + reps + " is outside the typical 1-15 range; formula accuracy degrades");
                }

                double orm = epley(weight, reps);
                double brz = brzycki(weight, reps);
                double lom = lombardi(weight, reps);
                LOGGER.fine("Computed weight=" + weight + " reps=" + reps
                        + " -> epley=" + orm + " brzycki=" + brz + " lombardi=" + lom);

                response = "Epley: " + orm + " lbs | Brzycki: " + brz + " lbs | Lombardi: " + lom + " lbs";
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Non-numeric input in query: " + query, e);
                status = 400;
                response = "Bad request: weight and reps must be numbers. Use /orm?weight=225&reps=5";
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Malformed query: " + query, e);
                status = 400;
                response = "Bad request: use /orm?weight=225&reps=5";
            }

            exchange.sendResponseHeaders(status, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}