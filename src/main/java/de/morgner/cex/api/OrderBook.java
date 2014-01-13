package de.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 *
 * @author Christian Morgner
 */
public class OrderBook {

	@Expose
	private long timestamp = 0L;
	
	@Expose
	private List<List<Double>> bids = null;
	
	@Expose
	private List<List<Double>> asks = null;

	@Override
	public String toString() {
		return bids.size() + " bids, " + asks.size() + " asks";
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public List<List<Double>> getBids() {
		return bids;
	}

	public List<List<Double>> getAsks() {
		return asks;
	}
}
