package de.morgner.cex.api;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 *
 * @author Christian Morgner
 */
public class Balance implements Serializable {
	
	@Expose
	private String username = null;
	
	@Expose
	private long timestamp  = 0L;
	
	@Expose
	private Amount BTC = null;
	
	@Expose
	private Amount GHS = null;
	
	@Expose
	private Amount NMC = null;
	
	@Expose
	private Amount IXC = null;
	
	@Expose
	private Amount DVC = null;

	@Override
	public String toString() {
		
		final StringBuilder buf = new StringBuilder();
		
		if (BTC != null) {
			buf.append("BTC: ").append(BTC.toString());
		} else {
			buf.append("null");
		}
		
		if (GHS != null) {
			
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append("GHS: ").append(GHS.toString());
		} else {
			buf.append("null");
		}
		
		if (NMC != null) {
			
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append("NMC: ").append(NMC.toString());
		} else {
			buf.append("null");
		}
		
		if (IXC != null) {
			
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append("IXC: ").append(IXC.toString());
		} else {
			buf.append("null");
		}
		
		if (DVC != null) {
			
			if (buf.length() > 0) {
				buf.append(", ");
			}
			buf.append("DVC: ").append(DVC.toString());
		} else {
			buf.append("null");
		}
		
		return buf.toString();
	}
	
	public String getUsername() {
		return username;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public Amount getBTC() {
		return BTC;
	}

	public Amount getGHS() {
		return GHS;
	}

	public Amount getNMC() {
		return NMC;
	}

	public Amount getIXC() {
		return IXC;
	}

	public Amount getDVC() {
		return DVC;
	}
	
}
