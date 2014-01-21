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
	
	// ----- equals / hashCode contract -----
	@Override
	public int hashCode() {
		return Long.valueOf(tid).hashCode();
	}
	
	@Override
	public boolean equals(final Object other) {
		
		if (other instanceof Trade) {
			
			return ((Trade)other).hashCode() == this.hashCode();
		}
		
		return false;
	}

	// ----- interface Comparable -----
	public int compareTo(Trade o) {
		
		// default sort order is "date, descending"
		return Long.valueOf(o.getDate()).compareTo(Long.valueOf(getDate()));
	}
}
