package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.net.InetSocketAddress;

import ca.utoronto.utm.mcs.Handlers.GetRestaurantsHandler;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class App 
{
    static int PORT = 8080;
    public static void main(String[] args) throws IOException
    {
        //Establishing a connection to a Mlabs cluster where so far it stores the restaurants and their hours of operations
        String building = "Davis";
        MongoDBConnector connector = new MongoDBConnector();
        MongoClient connection = connector.getMongoDBConnection();
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/restaurants", new GetRestaurantsHandler());

        server.start();
        System.out.printf("Server started on port %d...\n", PORT);

        //RetrieveHoursOfOperation result = new RetrieveHoursOfOperation(connection, building);
    }
}
