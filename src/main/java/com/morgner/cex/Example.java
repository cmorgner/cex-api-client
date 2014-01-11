package com.morgner.cex;

import com.morgner.cex.api.Amount;
import com.morgner.cex.api.Balance;
import com.morgner.cex.api.Order;
import com.morgner.cex.api.OrderType;
import com.morgner.cex.api.Pair;
import java.io.IOException;

/**
 *
 * @author Christian Morgner
 */
public class Example {

	public static void main(final String[] args) {
		
		final String username  = "";	// your username here
		final String apiKey    = "";	// your API key here
		final String apiSecret = "";	// your API secret here
		
		final CexClient client = new CexClient(username, apiKey, apiSecret);

		try {
			
			
			// fetch balance
			final Balance myBalance = client.getBalance();
			final Amount btc        = myBalance.getBTC();
			
			System.out.println("My BTC balance: " + (btc.getAvailable() + btc.getOrders()));
			System.out.println("BTC available:  " + btc.getAvailable());
			System.out.println("BTC in orders:  " + btc.getOrders());

			
			
			// place order
			final Order order = client.placeOrder(Pair.GHS_BTC, OrderType.Buy, 1.0, 0.0001);
			System.out.println("Placed order: " + order);
			
			
			// cancel order
			client.cancelOrder(order);
			
			
			
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
}
