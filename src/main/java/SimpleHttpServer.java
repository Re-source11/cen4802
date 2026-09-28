import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

    public class SimpleHttpServer {

        public static void main(String[] args) throws IOException {
            // 1. Create an HttpServer instance listening on port 8080
            // The second argument is the backlog (0 means use the system default)
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // 2. Create a context mapping the "/" URI path to a custom handler
            server.createContext("/", new RootHandler());
            server.createContext("/orm", new OrmHandler());
            // 3. Set a default executor for handling threads (null uses the default executor)
            server.setExecutor(null);

            // 4. Start the server
            System.out.println("Server started on port 8080...");
            server.start();
        }

        public static double epley(double weight, int reps) {
            return weight * (1 + reps / 30.0);
        }
        public static double brzycki(double weight, int reps){
            return weight * (36.0/(37.0 - reps));
        }
        // Custom handler to process incoming HTTP requests
        static class RootHandler implements HttpHandler {

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "RepMax server -CI Built V3. Use /orm?weight=225&reps=5";

                // Send HTTP response headers (Status Code: 200 OK, Content Length)
                exchange.sendResponseHeaders(200, response.getBytes().length);

                // Write the response string to the output stream
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());

                // Close the stream (crucial to finalize the exchange)
                os.close();
            }
        }


        public static class OrmHandler implements HttpHandler{
          @Override
          public void handle(HttpExchange exchange) throws IOException {
              String query = exchange.getRequestURI().getQuery();
              String[] parts = query.split("&");
              String weightText = parts[0].split("=")[1];
              String repsText = parts[1].split("=")[1];
              double weight = Integer.parseInt(weightText);
              int reps = (int) Double.parseDouble(repsText);
              double orm = epley(weight, reps);
              double brz = brzycki(weight, reps);
              String response = "Epley: " + orm + " lbs | Brzycki: " + brz + " lbs";
        // Send HTTP response headers (Status Code: 200 OK, Content Length)
              exchange.sendResponseHeaders(200, response.getBytes().length);

        // Write the response string to the output stream
              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());

        // Close the stream (crucial to finalize the exchange)
              os.close();
     }

            }
        }
