package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Creditcard;
import model.Orderline;
import model.Orders;
import model.Person;
import model.Product;
import model.Shopuser;

public class CreateSampleDataMongo {
	
	private ArrayList<Creditcard> creditcardlist = new ArrayList<Creditcard>();
	private ArrayList<Person> personlist = new ArrayList<Person>();
	private ArrayList<Product> productlist = new ArrayList<Product>();
	private ArrayList<Shopuser> shopuserlist = new ArrayList<Shopuser>();
	private ArrayList<Orders> orderlist = new ArrayList<Orders>();
	private HashSet<Orderline> orderlinelist = new HashSet<Orderline>();

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CreateSampleDataMongo csdm = new CreateSampleDataMongo();
		
		csdm.fillCreditCards();
		csdm.fillPersons();
		csdm.fillProducts();
		csdm.fillShopusers();
		csdm.fillOrders();
		csdm.fillOrderline();

		
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
		String connectPoint = mongo.getConnectPoint();
		
		/*
		MongoDatabase db = mongo.getDatabase("tests");
		System.out.println("name="+db.getName());
		*/
		
		DB database = mongo.getDB("tests");
		
		/*
		MongoCollection coll = db.getCollection("person");
		System.out.println("collection="+coll.count());
		*/

		DBCollection collection = database.getCollection("customers");
		
		/*
		BasicDBObject document = new BasicDBObject();
		document.put("name", "Shubham");
		document.put("company", "Baeldung");
		collection.insert(document);
		DBCursor cursor = collection.find();
        int i = 1;
        while (cursor.hasNext()) { 
           System.out.println("Inserted Document: "+i); 
           System.out.println(cursor.next()); 
           i++;
        }
        */
		
		csdm.writeOrdersToMongo(collection);
		
        System.out.println("finished");
        mongo.close();
		
	}
	
	
	// ---------------------------------------------------------------------------

	/**
	 * Fill the ArrayList with 10000 sample Creditcard Objects
	 */
	private void fillCreditCards() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		Creditcard c1;
		for (int i = 1; i <= 10000; i++) {
			String cardname = "Visa";
			if(i%3==0) {
				cardname = "Mastercard";
			}
			else if (i%8 == 0) {
				cardname = "Amex";
			}
			
			c1 = new Creditcard(i, cardname, "1234"+i, DateHelper.getCurrentTimeStampAsDate());
			creditcardlist.add(c1);
		}
		endtime = System.nanoTime();
		System.out.println("Duration of fillCreditCards (ms): "+(endtime-starttime)/1000000);
		
	}
	
	
	/**
	 * Fill the ArrayList with 10000 sample Person Objects
	 */
	private void fillPersons() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		Person p1;
		for (int i = 1; i <= 10000; i++) {
			String country = "Austria";
			if(i%4==0) {
				country = "Germany";
			}
			else if (i%13 == 0) {
				country = "Italy";
			} 
			else if (i%7 == 0) {
				country = "France";
			}
			p1 = new Person(i, "Firstname"+i, "Lastname"+i, new java.util.Date(), "0664"+i, country, "Testcity", (i+1000)+"", "Teststreet "+i, creditcardlist.get(i-1));
			personlist.add(p1);
		}
		endtime = System.nanoTime();
		System.out.println("Duration of fillPersons (ms): "+(endtime-starttime)/1000000);
		
	}

	/**
	 * Fill the ArrayList with 100000 sample Product Objects
	 */
	private void fillProducts() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		Random randy = new Random();
		
		Product p1;
		for (int i = 1; i <= 1000000; i++) {
			p1 = new Product(i, "Product"+i, "The best Product!", randy.nextFloat()*randy.nextInt(1000), randy.nextFloat()*randy.nextInt(500));
			fillProducts2Categories(p1);
			productlist.add(p1);
		}
		endtime = System.nanoTime();
		System.out.println("Duration of fillProducts (ms): "+(endtime-starttime)/1000000);
		
	}
	
	/**
	 * Fill some sample Categories into the Products
	 */
	private void fillProducts2Categories(Product p) {
		Random randy = new Random();
		HashSet<String> cats = new HashSet<String>();
		//set up to 10 categories for the product
		for(int j = 1; j <= randy.nextInt(9)+1; j++) {
			cats.add("category"+j);
		}
		p.setCategories(cats);	
	}
	
	/**
	 * Fill the ArrayList with 10000 sample Shopuser Objects
	 */
	private void fillShopusers() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		

		Shopuser s1;
		for (int i = 1; i <= 10000; i++) {
			
			java.util.Date d = null;
			if(i%27 == 0) {
				d = new java.util.Date();
			}
			
			//create MD5 Hash of Password
			String pwd = "pwd"+i;
			byte[] bytesOfMessage = null;
			try {
				bytesOfMessage = pwd.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] thedigest = md.digest(bytesOfMessage);
			StringBuffer sb = new StringBuffer();
			for (byte b : thedigest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			pwd = sb.toString();

			s1 = new Shopuser("user"+i, "user"+i+"@testmail.at", pwd, new java.util.Date(), d, personlist.get(i-1));
			shopuserlist.add(s1);
		}
		endtime = System.nanoTime();
		System.out.println("Duration of fillShopusers (ms): "+(endtime-starttime)/1000000);
		
	}
	
	
	/**
	 * Fill the ArrayList with 100000 sample Order Objects
	 */
	private void fillOrders() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		Random randy = new Random();

		Shopuser user;
		Orders o1;
		for(int i = 1; i <= 100000; i++) {
			
			// generate a random date
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, randy.nextInt(17)+2000);
			calendar.set(Calendar.DAY_OF_YEAR, randy.nextInt(365));
			java.util.Date d = calendar.getTime();
			
			// get a random user from the user list
			user = shopuserlist.get(randy.nextInt(10000));
			
			o1 = new Orders(i, d, user);
			orderlist.add(o1);
		}
		
		
		endtime = System.nanoTime();
		System.out.println("Duration of fillOrders (ms): "+(endtime-starttime)/1000000);
		
	}
	

	/**
	 * Fill the HashSet with 5000000 sample Orderline Objects
	 */
	private void fillOrderline() {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		Random randy = new Random();

		Product p;
		Orders o;
		
		Orderline o1;
		
		for(int i = 1; i <= 5000000; i++) {
			o = orderlist.get(randy.nextInt(100000));
			p = productlist.get(randy.nextInt(1000000));
			
			
			o1 = new Orderline(i, o, p);
			o1.setAmount(randy.nextFloat()+randy.nextInt(1000));
			orderlinelist.add(o1);
		}
		
		//for (Orderline ol : orderlinelist) {
		//	ol.setAmount(randy.nextFloat()+randy.nextInt(1000));
		//}
		
		endtime = System.nanoTime();
		System.out.println("Duration of fillOrderline (ms): "+(endtime-starttime)/1000000);
		
	}
	
	
	// ---------------------------------------------------------------------------
	
	/**
	 * Insert the Creditcards from the ArrayList into the Oracle DB
	 */
	private void writeOrdersToMongo(DBCollection collection) {
		long starttime;
		long endtime;
		starttime = System.nanoTime();
		
		BasicDBObject order = null;
		BasicDBObject user = null;
		BasicDBObject product = null;
		BasicDBList cats = null;
		
		for(Orderline o1 : orderlinelist) {

			order = new BasicDBObject();
			order.put("order_id", o1.getOrder().getOrder_id());
			
			user = new BasicDBObject();
			user.put("username", o1.getOrder().getUser().getUsername());			
			order.put("user", user);
			
			order.put("orderdate",o1.getOrder().getOrder_date());
			order.put("amount", o1.getAmount());
			
			product = new BasicDBObject();
			product.put("product_id", o1.getProduct().getProduct_id());
			product.put("price", o1.getProduct().getPrice());
			
			cats = new BasicDBList();
			cats.addAll(o1.getProduct().getCategories());
			product.put("categories", cats);
			
			order.put("product", product);
			
			
			collection.insert(order);
			
		}
		

		
		endtime = System.nanoTime();
		System.out.println("Duration of writeOrdersToMongo (ms): "+(endtime-starttime)/1000000);
		
	}

}
