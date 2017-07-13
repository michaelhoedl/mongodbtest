package mongogroup.mongoartifact;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

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
		System.out.println(connectPoint);
		
		
		MongoDatabase database = mongo.getDatabase("mycustomers");
		MongoCollection collection = database.getCollection("customers");
		
		System.out.println(database.getName());
		
		List<Document> documents = (List<Document>) collection.find().into(
				new ArrayList<Document>());
 
		System.out.println("size="+documents.size());
		
		
               for(Document document : documents){
                   System.out.println(document.get("first_name"));
               }
		
		
		mongo.close();
    }
}
