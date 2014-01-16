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

	// ----- interface Comparable -----
	public int compareTo(Amount o) {
		
		// default sort order is "available, descending"
		return Double.valueOf(o.getAvailable()).compareTo(Double.valueOf(getAvailable()));
	}
}
