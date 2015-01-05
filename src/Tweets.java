import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

@SuppressWarnings("deprecation")
public class Tweets {

	private HttpGet request = null;
	private int number = 0;

	//MongoDB connection
	private DB db = null;

	//MongoDB server
	private Mongo m = null;

	public Tweets(String args) {
		if (args.equals("save")) {
			Authentication app = getConnectionData("src/twitter.xml");
			connect();
			request = connect(app);
			retrieve(request);
		}
		else { System.out.println("Mauvais argument"); }
	}

	/**
	 * The connection method connects to the MongoDB database
	 */
	private void connect() {
		
		try {
			
			m = new Mongo( Configuration.mongo_location , Configuration.mongo_port );
			db = m.getDB(Configuration.mongo_database);
			
		} //try
		catch (UnknownHostException uhe) {
			
			uhe.printStackTrace();
			
		} //catch
		catch (MongoException uhe) {
			
			uhe.printStackTrace();
			
		}
		
	} //connect
	
	/**
	 * The retrieve method is responsible to read the content from the stream
	 * and to provide to consumers
	 * 
	 * @param request
	 *            the request to access the stream
	 */
	private void retrieve(HttpGet request) {

		String in;
		try {

			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {

				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));

				// since this is a continuous stream, there is no end with the
				// readLine, we just check whether live boolean variable is set
				// to false
				while ((in = reader.readLine()) != null) {
					if (!in.startsWith("{\"delete"))
					{	
						BasicDBList object = (BasicDBList)(JSON.parse(in));
						
						for(int i=0; i < object.size(); i++) {
							BasicDBObject aTweet = (BasicDBObject)object.get(i);
							
							
							BasicDBObject user = (BasicDBObject)aTweet.get("user");
							BasicDBObject data = new BasicDBObject();
							data.put("screen_name", user.get("screen_name"));
							data.put("created_at", aTweet.get("created_at"));
							data.put("text", aTweet.get("text"));
							data.put("entities", aTweet.get("entities"));
					  		System.out.println(data);  
					  		number++;
					  		if(number == 3)
								
								System.exit(0);

							}
						}
						               
	//					DBCollection collection = db.getCollection(Configuration.mongo_collection);
	//					collection.insert(object);
				  		
					}
					 // while
				
			} // if

		} // try
		catch (Exception e) {

			e.printStackTrace();

		} // catch

	} // retrieve()
	
	/**
	 * getConnectionData retrieves the data for the authentication
	 * @param stFile the file containing the data for the OAuth authentication
	 * @return an instance of Authentication containing the data
	 */
	private Authentication getConnectionData(String stFile) {
	
		Authentication sr = null;
		
		try {
			
			//open the file and parse it to retrieve the four required information
			File file = new File(stFile);
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "ISO-8859-1");
	
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			
			XMLReader saxReader = XMLReaderFactory.createXMLReader();
			sr = new Authentication();
			saxReader.setContentHandler(sr);
			saxReader.parse(is);
			
		} //try
		catch (FileNotFoundException e) {

			e.printStackTrace();

		} // catch
		
		catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		} // catch
		
		catch (SAXException e) {

			e.printStackTrace();

		} // catch
		
		catch (IOException e) {

			e.printStackTrace();

		} // catch

		return sr;
		
	} //getConnectionData()
	
	/**
	 * The connect method connects to the stream via OAuth
	 * 
	 * @param app
	 *            the data for connection
	 * @return the request
	 */
	private HttpGet connect(Authentication app) {

		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
				app.getConsumerKey(), app.getConsumerSecret());

		consumer.setTokenWithSecret(app.getAccessToken(), app.getAccessSecret());
		//HttpGet request = new HttpGet("https://stream.twitter.com/1.1/statuses/sample.json");
		HttpGet request = new HttpGet("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=taschert");


		try {

			consumer.sign(request);

		} // try
		catch (OAuthMessageSignerException e) {

			e.printStackTrace();

		} // catch
		catch (OAuthExpectationFailedException e) {

			e.printStackTrace();

		} // catch
		catch (OAuthCommunicationException e) {

			e.printStackTrace();

		} // catch

		return request;

	} // connect()
	
	public static void main(String[] args) {
		
		if (args.length == 1) {
			new Tweets(args[0]);
			
		}
		else {
			System.out.println("mode analyse");
		}
	}
}
