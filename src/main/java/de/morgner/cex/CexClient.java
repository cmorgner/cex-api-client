package de.morgner.cex;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.morgner.cex.api.Balance;
import de.morgner.cex.api.Endpoint;
import de.morgner.cex.api.Order;
import de.morgner.cex.api.OrderBook;
import de.morgner.cex.api.OrderType;
import de.morgner.cex.api.Pair;
import de.morgner.cex.api.Ticker;
import de.morgner.cex.api.Trade;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * The main class of the CEX.IO Java API client.
 *
 * @author Christian Morgner
 */
public class CexClient {

	private static final Logger logger      = Logger.getLogger(CexClient.class.getName());
	private static final String BASE_URL    = "https://cex.io/api/";
	
	private Gson gson             = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
	private HttpClient httpClient = new DefaultHttpClient();
	private String apiKey         = null;
	private String apiSecret      = null;
	private String username       = null;
	private long lastCall         = 0L;
	
	/**
	 * Constructs a new CEX.IO API client with the given credentials.
	 * 
	 * @param username the username
	 * @param apiKey the API key
	 * @param apiSecret the API secret
	 */
	public CexClient(final String username, final String apiKey, final String apiSecret) {
		
		this.username  = username;
		this.apiKey    = apiKey;
		this.apiSecret = apiSecret;
	}
	
	// ----- public methods -----
	public Ticker getTicker(final Pair pair) throws IOException {
		return doGet(prepareCall(Endpoint.ticker, pair.url()), Ticker.class);
	}
	
	public OrderBook getOrderBook(final Pair pair) throws IOException {
		return doGet(prepareCall(Endpoint.order_book, pair.url()), OrderBook.class);
	}
	
	public List<Trade> getHistory(final Pair pair) throws IOException {
		return getHistory(pair, 0);
	}
	
	public List<Trade> getHistory(final Pair pair, final long tradeId) throws IOException {
		return doGet(prepareCall(Endpoint.trade_history, pair.url()) + "?since=" + tradeId, new TypeToken<List<Trade>>(){}.getType());
	}

	public List<Order> getOrders(final Pair pair) throws IOException {
		return doPost(prepareCall(Endpoint.open_orders, pair.url()), new TypeToken<List<Order>>(){}.getType());
	}
	
	public boolean cancelOrder(final Order order) throws IOException {
		return doPost(prepareCall(Endpoint.cancel_order), Boolean.class, toMap("id", order.getId()));
	}
	
	public Order placeOrder(final Pair pair, final OrderType type, final double amount, final double price) throws IOException {
		return doPost(prepareCall(Endpoint.place_order, pair.url()), Order.class, toMap("type", type.name().toLowerCase(), "amount", amount, "price", price));
	}
	
	public Balance getBalance() throws IOException {
		return doPost(prepareCall(Endpoint.balance), Balance.class);
	}
	
	// ----- private methods -----
	private synchronized <T> T doGet(final String url, final Type resultType) throws IOException {
	
		throttle();
		
		final HttpGet get           = new HttpGet(url);
		final HttpResponse response = httpClient.execute(get);
		final InputStream input     = response.getEntity().getContent();
		final String json           = IOUtils.toString(input);

		// close input
		input.close();
		get.abort();

		try {
		
			return gson.fromJson(json, resultType);
			
		} catch (Throwable t) {
			
			logger.log(Level.SEVERE, "Exception while parsing JSON string {0}: {1}", new Object[] { json, t.getMessage() } );
		}
		
		return null;
	}
	
	private synchronized <T> T doPost(final String url, final Type resultType) throws IOException {
		return doPost(url, resultType, null);
	}
	
	private synchronized <T> T doPost(final String url, final Type resultType, final Map<String, Object> params) throws IOException {
		
		throttle();
		
		final HttpPost post     = new HttpPost(url);
		final long nonce        = System.currentTimeMillis();
		final StringBuilder buf = new StringBuilder();

		// create signature etc.
		buf.append("key=").append(apiKey);
		buf.append("&signature=").append(getSignature(nonce, username, apiKey));
		buf.append("&nonce=").append(nonce);

		if (params != null) {

			for (final Entry<String, Object> entry : params.entrySet()) {

				buf.append("&");
				buf.append(entry.getKey());
				buf.append("=");
				buf.append(entry.getValue());
			}
		}

		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setEntity(new StringEntity(buf.toString()));

		
		final HttpResponse response = httpClient.execute(post);
		final InputStream input     = response.getEntity().getContent();
		final String json           = IOUtils.toString(input);

		// close input
		input.close();
		post.abort();

		try {
		
			return gson.fromJson(json, resultType);
			
		} catch (Throwable t) {
			
			logger.log(Level.SEVERE, "Exception while parsing JSON string {0}: {1}", new Object[] { json, t.getMessage() } );
		}
		
		return null;
	}
	
	private String prepareCall(final Endpoint endpoint, final String... urlParts) {

		final StringBuilder buf = new StringBuilder(BASE_URL);

		// endpoint
		if (endpoint != null) {
			
			buf.append(endpoint.name());
			buf.append("/");
			
		} else {
			
			throw new NullPointerException("Endpoint must not be null.");
		}
		
		// append additional parts
		for (final String part : urlParts) {
			buf.append(part);
			buf.append("/");
		}
		
		return buf.toString();
	}
	
	private String getSignature(final long nonce, final String username, final String apiKey) {
		
		final StringBuilder msg = new StringBuilder();
		
		// build message
		msg.append(nonce);
		msg.append(username);
		msg.append(apiKey);
		
		// return encoded message
		return encode(apiSecret, msg.toString());
	}
	
	private String encode(final String key, final String data) {
		
		try {
			final Mac sha256_HMAC          = Mac.getInstance("HmacSHA256");
			final SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");

			sha256_HMAC.init(secret_key);

			return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes())).toUpperCase();
			
		} catch (NoSuchAlgorithmException nsex) {
			
			logger.log(Level.SEVERE, "Unable to encode signature, algorithm not found.", nsex);
			
		} catch (InvalidKeyException ikex) {
			
			logger.log(Level.SEVERE, "Unable to encode signature, invalid key.", ikex);
		}
		
		return null;
	}
	
	private synchronized void throttle() {
		
		try {
			
			// do not make more than 1 request per second
			while (System.currentTimeMillis() < (lastCall + 1001)) {
				Thread.sleep(10);
			}

			// set timer for throttling
			lastCall = System.currentTimeMillis();
			
		} catch (Throwable t) { }
	}
	
	private Map<String, Object> toMap(final String key, final Object value) {
		
		final Map<String, Object> map = new LinkedHashMap<>();
		
		map.put(key, value);
		
		return map;
	}
	
	private Map<String, Object> toMap(final String key1, final Object value1, final String key2, final Object value2, final String key3, final Object value3) {
		
		final Map<String, Object> map = new LinkedHashMap<>();
		
		map.put(key1, value1);
		map.put(key2, value2);
		map.put(key3, value3);
		
		return map;
	}
}
