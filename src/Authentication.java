import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class Authentication implements ContentHandler {

	private String AccessToken = null;
	private String AccessSecret = null;
	private String ConsumerKey = null;
	private String ConsumerSecret = null;
	
	private boolean boolAccessToken = false;
	private boolean boolAccessSecret = false;
	private boolean boolConsumerKey = false;
	private boolean boolConsumerSecret = false;

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	///////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (boolAccessToken)
			AccessToken = new String(ch, start, length);
		
		if (boolAccessSecret) 
			AccessSecret = new String(ch, start, length);
		
		if (boolConsumerKey) 
			ConsumerKey = new String(ch, start, length);

		if (boolConsumerSecret) 
			ConsumerSecret = new String(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if(localName.equals("AccessToken")) {
			boolAccessToken = true;
		}
		if(localName.equals("AccessSecret")) {
			boolAccessSecret = true;
		}
		if(localName.equals("ConsumerKey")) {
			boolConsumerKey = true;
		}
		if(localName.equals("ConsumerSecret")) {
			boolConsumerSecret = true;
		}
		
	}
	
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("AccessToken")) {
			boolAccessToken = false;
		}
		if(localName.equals("AccessSecret")) {
			boolAccessSecret = false;
		}
		if(localName.equals("ConsumerKey")) {
			boolConsumerKey = false;
		}
		if(localName.equals("ConsumerSecret")) {
			boolConsumerSecret = false;
		}
	}

	
	
	public String getAccessToken() {
		return AccessToken;
	}
	public String getAccessSecret() {
		return AccessSecret;
	}
	public String getConsumerKey() {
		return ConsumerKey;
	}
	public String getConsumerSecret() {
		return ConsumerSecret;
	}
	
}
