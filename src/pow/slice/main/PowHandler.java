package pow.slice.main;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import space.davidboles.lib.ht.tp.ContextualHttpHandler;

public class PowHandler extends ContextualHttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		String out = t.getRequestURI().getPath().substring(1);
		t.sendResponseHeaders(404, out.length());
        OutputStream os = t.getResponseBody();
        os.write(out.getBytes());
        os.close();
	}

	@Override
	public String getURLPath() {
		return "/";
	}

}
