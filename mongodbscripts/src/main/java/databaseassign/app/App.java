package databaseassign.app;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.lang.StringBuilder;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.FileWriter;

public class App
{
    public static void main( String[] args )
    {
	try
	{
	    String csvFile1 = "/home/ec2-user/BUSINESS_NAMES_201803.csv";
	    Scanner sc = new Scanner(new File(csvFile1));
	    String csvFile2 = "/home/ec2-user/BUSINESS_NAMES_201803-1.csv";	    
	    FileWriter writer = new FileWriter(csvFile2);
	    writer.append(sc.nextLine());
	    while(sc.hasNextLine())
	    {
		String line = sc.nextLine();
		StringTokenizer st = new StringTokenizer(line, "\t");
		int tokenCount = 0;
		ArrayList<String> linetokens = new ArrayList<String>();
		while(st.hasMoreTokens())
		{
		    String token = st.nextToken();
		    if(token.charAt(0) != '"' && token.charAt(token.length() - 1) != '"' && token.length() > 1)
		    {
			token = "\"" + token + "\"";
		    }
		    linetokens.add(token);
		    //System.out.println("Number of Tokens left: " + tokenCount + " Token: " + token);
		    tokenCount++;
		}
		//System.out.println("Number of Tokens:" + linetokens.size());
		StringBuilder writeline = new StringBuilder();
		Iterator<String> itr = linetokens.iterator();
		while(itr.hasNext())
		{
		    String value = itr.next();
		    if(itr.hasNext())	
		    {
			writeline.append(value+",");
		    }
		    else	
		    {
			writeline.append(value+"\n");
		    }
		}
		
		//System.out.println(writeline.toString());
		//System.out.println();
		writer.append(writeline.toString());
	    }
	    writer.flush();
	    writer.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
        //MongoClient mongoClient = new MongoClient();
	//MongoDatabase database = mongoClient.getDatabase("myDatabase");
	//MongoCollection<Document> collection = database.getCollection("myCollection");
	//List<Document> documents = new ArrayList<Document>();
	//System.out.println(collection.count());
	    /*for (int i = 0; i < 100; i++) {
	    documents.add(new Document("i", i));
	    }
	    collection.insertMany(documents);*/
	/*Document doc = new Document("name", "MongoDB")
	    .append("type", "database")
	    .append("count", 1)
	    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
	    .append("info", new Document("x", 203).append("y", 102));
	    collection.insertOne(doc);		
	System.out.println(collection.count());
	System.out.println(doc.toJson());*/
    }
}
