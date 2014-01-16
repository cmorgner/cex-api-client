package de.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 *
 * @author Christian Morgner
 */
public class Trade implements Comparable<Trade>, Serializable {

	@Expose
	private long tid = 0L;
	
	@Expose
	private double amount = 0.0;
		
	@Expose
	private double price = 0.0;
	
	@Expose
	private long date = 0L;

	public long getTid() {
		return tid;
	}

	public double getAmount() {
		return amount;
	}

	public double getPrice() {
		return price;
	}

	public long getDate() {
		return date;
	}

	// ----- interface Comparable -----
	public int compareTo(Trade o) {
		
		// default sort order is "date, descending"
		return Long.valueOf(o.getDate()).compareTo(Long.valueOf(getDate()));
	}
}
