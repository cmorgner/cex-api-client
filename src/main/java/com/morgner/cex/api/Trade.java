package com.morgner.cex.api;

import com.google.gson.annotations.Expose;

/**
 *
 * @author Christian Morgner
 */
public class Trade {

	@Expose
	private long tid = 0L;
	
	@Expose
	private double amount = 0.0;
		
	@Expose
	private double price = 0.0;
	
	@Expose
	private long timestamp = 0L;

	public long getTid() {
		return tid;
	}

	public double getAmount() {
		return amount;
	}

	public double getPrice() {
		return price;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
