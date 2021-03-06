package ca.utoronto.utm.mcs.Handlers;
import ca.utoronto.utm.mcs.Utility.Utils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetRestaurantsHandler implements HttpHandler {

    private MongoClient mongoClient;

    public GetRestaurantsHandler(MongoClient client) {
        this.mongoClient = client;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException
    {
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                this.handleGet(httpExchange);
            }
            else {
                Utils.writeResponse(httpExchange, "", 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGet(HttpExchange httpExchange) throws JSONException, IOException{
        List<String> restrauntList = new ArrayList<>();
        MongoDatabase database = this.mongoClient.getDatabase("UTMFoodTracker");
        MongoCollection<Document> collection = database.getCollection("Restaurants");
        FindIterable<Document> iterable = collection.find();
        for(Document doc : iterable) {
            restrauntList.add(doc.get("Restaurant").toString());
        }
        Utils.writeResponse(httpExchange, getFinalJSON(restrauntList).toString(), 200);
    }

    private JSONObject getFinalJSON(List<String> restrauntList) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("restaurantlist", restrauntList);
        return jsonObject;
    }
}
