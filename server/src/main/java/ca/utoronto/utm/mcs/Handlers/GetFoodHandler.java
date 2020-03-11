package ca.utoronto.utm.mcs.Handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.utoronto.utm.mcs.MongoDBConnector;
import ca.utoronto.utm.mcs.Utility.TextSplitter;
import ca.utoronto.utm.mcs.Utility.Utils;

import org.json.*;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bson.types.ObjectId;

import com.mongodb.client.model.Filters;

public class GetFoodHandler {
	
	private Object mongoClient;
	
	private static MongoDBConnector connecter = new MongoDBConnector();

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
		String body = Utils.convert(httpExchange.getRequestBody());
		List<String> foodList = new ArrayList<>();
        JSONObject deserialized = new JSONObject(body); 
        String food = "";
        food = deserialized.getString("food"); 
        
        MongoClient db =  connecter.getMongoDBConnection();

    	MongoDatabase dbdata = db.getDatabase("UTMFoodTracker");

		MongoCollection collection = dbdata.getCollection("Menus");

		BasicDBObject query = new BasicDBObject();
		query.put("Food", food);
        FindIterable<Document> iterable = collection.find(query);
        for(Document doc : iterable) {
        	foodList.add(doc.get("name").toString()); //i dont know what to add
            Utils.writeResponse(httpExchange, getFinalJSON(foodList).toString(), 200);
            return;
        }

		MongoCursor<Document> mongoCursor = findIt.iterator();
        
        /*
        List<String> foodList = new ArrayList<>();
        MongoDatabase database = ((MongoClient) this.mongoClient).getDatabase("UTMFoodTracker");
        MongoCollection<Document> collection = database.getCollection("Restaurants");
        
        
        FindIterable<Document> iterable = collection.find();
        for(Document doc : iterable) {
            foodList.add(doc.get("Restaurant").toString());
        }*/
        //Utils.writeResponse(httpExchange, getFinalJSON(restrauntList).toString(), 200);
    }
	
    private JSONObject getFinalJSON(List<String> foodList) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Restaraunts", foodList); // TODO fix
        return jsonObject;
    }
	

}
