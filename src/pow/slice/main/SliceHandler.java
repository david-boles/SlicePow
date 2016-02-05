package pow.slice.main;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import space.davidboles.lib.ht.tp.ContextualHttpHandler;

public class SliceHandler extends ContextualHttpHandler {

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		System.out.println("new slice" + arg0.getRequestURI());

	}

	@Override
	public String getURLPath() {
		return Start.localContext + "/submit";
	}

}
