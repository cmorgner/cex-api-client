package com.morgner.cex.api;

/**
 *
 * @author Christian Morgner
 */
public enum Pair {

	GHS_BTC("GHS/BTC"),
	GHS_NMC("GHS/NMC"),
	NMC_BTC("NMC/BTC");

	private String url = null;

	Pair(final String url) {
		this.url = url;
	}

	public String url() {
		return url;
	}
}
