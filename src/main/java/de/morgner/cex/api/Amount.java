package de.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author Christian Morgner
 */
public class Amount implements Comparable<Amount>, Serializable {
	
	@Expose
	private double available = 0.0;
	
	@Expose
	private double orders    = 0.0;

	@Override
	public String toString() {
		
		final DecimalFormat df = new DecimalFormat("0.00000000");
		return df.format(available) + "/" + df.format(orders);
	}
	
	public double getAvailable() {
		return available;
	}
	
	public double getOrders() {
		return orders;
	}
	
	// ----- equals / hashCode contract -----
	@Override
	public int hashCode() {
		return Double.valueOf(available).hashCode() ^ Double.valueOf(orders).hashCode();
	}
	
	@Override
	public boolean equals(final Object other) {
		
		if (other instanceof Amount) {
			
			return ((Amount)other).hashCode() == this.hashCode();
		}
		
		return false;
	}

	// ----- interface Comparable -----
	public int compareTo(Amount o) {
		
		// default sort order is "available, descending"
		return Double.valueOf(o.getAvailable()).compareTo(Double.valueOf(getAvailable()));
	}
}
