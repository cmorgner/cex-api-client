package com.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.text.DecimalFormat;

/**
 *
 * @author Christian Morgner
 */
public class Amount {
	
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
}
