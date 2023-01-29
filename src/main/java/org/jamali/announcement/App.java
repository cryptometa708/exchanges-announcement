package org.jamali.announcement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jamali.announcement.exchange.Binance;
import org.jamali.announcement.exchange.Bitfinex;
import org.jamali.announcement.exchange.Bitmart;
import org.jamali.announcement.exchange.Bitstamp;
import org.jamali.announcement.exchange.ByBit;
import org.jamali.announcement.exchange.Coinbase;
import org.jamali.announcement.exchange.Coinbase2;
import org.jamali.announcement.exchange.Coingecko;
import org.jamali.announcement.exchange.Coinmarketcap;
import org.jamali.announcement.exchange.Crypto;
import org.jamali.announcement.exchange.Digifinex;
import org.jamali.announcement.exchange.Exchange;
import org.jamali.announcement.exchange.Ftx;
import org.jamali.announcement.exchange.GateIo;
import org.jamali.announcement.exchange.Huobi;
import org.jamali.announcement.exchange.Kraken;
import org.jamali.announcement.exchange.KuCoin;
import org.jamali.announcement.exchange.KuCoin2;
import org.jamali.announcement.exchange.Lbank;
import org.jamali.announcement.exchange.Lbank2;
import org.jamali.announcement.exchange.Mandala;
import org.jamali.announcement.exchange.Mexc;
import org.jamali.announcement.exchange.News;
import org.jamali.announcement.exchange.Okex;
import org.jamali.announcement.exchange.Poloniex;
import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.DefaultClient;
import org.jamali.announcement.utils.Util;

/**
 * 
 * @author Ibrahim Jamali
 *
 */
public class App {
	public App(String[] args) throws IOException {
		Client client = new DefaultClient();
		Properties properties = Util.loadConfigurtion();
		System.setProperty("webdriver.chrome.silentOutput", "true");
		Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
		List<Exchange> exchanges = new ArrayList<Exchange>();
		List<String> arguments = Arrays.asList(args);
		System.out.println(arguments);
		if (arguments.contains("all")) {
			exchanges.addAll(Arrays.asList(new Binance(client, properties), new ByBit(client, properties),
					new Coinbase(client, properties), new Ftx(client, properties), new GateIo(client, properties),
					new Huobi(client, properties), new Kraken(client, properties), new KuCoin(client, properties),
					new Crypto(client, properties), new Bitstamp(client, properties), new Bitfinex(client, properties),
					new Lbank(client, properties), new Okex(client, properties), new Mexc(client, properties),
					new Bitmart(client, properties), new Poloniex(client, properties), new Mandala(client, properties),
					new Digifinex(client, properties), new News(client, properties)));
		}
		if (arguments.contains("binance")) {
			exchanges.add(new Binance(client, properties));
		}
		if (arguments.contains("bybit")) {
			exchanges.add(new ByBit(client, properties));
		}
		if (arguments.contains("gateio")) {
			exchanges.add(new GateIo(client, properties));
		}
		if (arguments.contains("kraken")) {
			exchanges.add(new Kraken(client, properties));
		}
		if (arguments.contains("coinbase")) {
			exchanges.add(new Coinbase(client, properties));
		}
		if (arguments.contains("ftx")) {
			exchanges.add(new Ftx(client, properties));
		}
		if (arguments.contains("kucoin")) {
			exchanges.add(new KuCoin(client, properties));
		}
		if (arguments.contains("huobi")) {
			exchanges.add(new Huobi(client, properties));
		}
		if (arguments.contains("crypto")) {
			exchanges.add(new Crypto(client, properties));
		}
		if (arguments.contains("bitstamp")) {
			exchanges.add(new Bitstamp(client, properties));
		}
		if (arguments.contains("bitfinex")) {
			exchanges.add(new Bitfinex(client, properties));
		}
		if (arguments.contains("lbank")) {
			exchanges.add(new Lbank(client, properties));
		}
		if (arguments.contains("okex")) {
			exchanges.add(new Okex(client, properties));
		}
		if (arguments.contains("mexc")) {
			exchanges.add(new Mexc(client, properties));
		}
		if (arguments.contains("bitmart")) {
			exchanges.add(new Bitmart(client, properties));
		}
		if (arguments.contains("poloniex")) {
			exchanges.add(new Poloniex(client, properties));
		}
		if (arguments.contains("mandala")) {
			exchanges.add(new Mandala(client, properties));
		}
		if (arguments.contains("digifinex")) {
			exchanges.add(new Digifinex(client, properties));
		}
		if (arguments.contains("news")) {
			exchanges.add(new News(client, properties));
		}
		if (arguments.contains("coinmarketcap")) {
			exchanges.add(new Coinmarketcap(client, properties));
		}
		if (arguments.contains("coingecko")) {
			exchanges.add(new Coingecko(client, properties));
		}
		if (arguments.contains("poloniex1")) {
			exchanges.add(new Poloniex(client, properties));
		}
		if (arguments.contains("lbank2")) {
			exchanges.add(new Lbank2(client, properties));
		}
		if (arguments.contains("coinbase2")) {
			exchanges.add(new Coinbase2(client, properties));
		}
		if (arguments.contains("Kucoin2")) {
			exchanges.add(new KuCoin2(client, properties));
		}
		if (arguments.contains("help")) {
			System.err.println(
					"Announcement.jar binance|bybit|gateio|kraken|coinbase|ftx|kucoin|huobi|crypto|bitstamp|bitfinex|lbank|okex|all");
		}

		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(exchanges.size());
		for (Exchange exchange : exchanges) {
			executorService.scheduleAtFixedRate(exchange, 0, 2, TimeUnit.MINUTES);
		}
	}

	public static void main(String[] args) throws Exception {
		new App(args);

	}
}
