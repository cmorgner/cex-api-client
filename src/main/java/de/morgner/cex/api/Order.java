package de.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Christian Morgner
 */
public class Order implements Comparable<Order>, Serializable {

	@Expose
	private long id = 0L;
	
	@Expose
	private long timestamp = 0L;

	@Expose
	private String type = null;
		
	@Expose
	private double price = 0.0;
	
	@Expose
	private double amount = 0.0;
	
	@Expose
	private double pending = 0.0;

	@Override
	public String toString() {
		
		return "id: " + id + ", timestamp: " + timestamp + ", type: " + type + ", price: " + price + ", amount: " + amount + ", pending: " + pending; 
	}
	
	public long getId() {
		return id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public OrderType getType() {
		return OrderType.valueOf(StringUtils.capitalize(type));
	}

	public double getPrice() {
		return price;
	}

	public double getAmount() {
		return amount;
	}

	public double getPending() {
		return pending;
	}
	
	// ----- equals / hashCode contract -----
	@Override
	public int hashCode() {
		return type.hashCode() ^ Double.valueOf(price).hashCode() ^ Double.valueOf(amount).hashCode() ^ Double.valueOf(pending).hashCode();
	}
	
	@Override
	public boolean equals(final Object other) {
		
		if (other instanceof Order) {
			
			return ((Order)other).hashCode() == this.hashCode();
		}
		
		return false;
	}

	// ----- interface Comparable -----
	public int compareTo(final Order o) {
		
		// default sort order is "price, descending"
		return Double.valueOf(o.getPrice()).compareTo(Double.valueOf(getPrice()));
	}
}
