package de.morgner.cex.api;

import java.io.Serializable;

/**
 *
 * @author Christian Morgner
 */
public enum Pair implements Serializable {

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
