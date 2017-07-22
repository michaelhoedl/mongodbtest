package mongogroup.mongoartifact;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.Document;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        

        MongoClient mongo = new MongoClient( "localhost" , 27017 );
		String connectPoint = mongo.getConnectPoint();
		
		//MongoDatabase db = mongo.getDatabase("tests");
		//System.out.println("name="+db.getName());
		
		DB database = mongo.getDB("tests");
		
		DBCollection collection = database.getCollection("customers");

		DBCursor cursor = collection.find();
		System.out.println("1.count="+cursor.count());
		
		BasicDBObject document = new BasicDBObject();

		// Delete All documents from collection Using blank BasicDBObject
		collection.remove(document);
		System.out.println("2.count="+cursor.count());

		
		while (cursor.hasNext()) {
		    collection.remove(cursor.next());
		}
		
		System.out.println("3.count="+cursor.count());

		
        
    }
}
