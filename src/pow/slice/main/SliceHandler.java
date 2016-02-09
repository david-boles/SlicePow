package pow.slice.main;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import space.davidboles.lib.ht.tp.ContextualHttpHandler;
import space.davidboles.lib.program.Logger;
import space.davidboles.lib.program.ProgramFs;

public class SliceHandler extends ContextualHttpHandler {
	Logger logger = Logger.uLogger;
	
	@Override
	public void handle(HttpExchange h) throws IOException {//TODO Sliced when pows?
		String url = h.getRequestURI().toString();
		
		url = url.substring(18, url.length());
		url = url.substring(0, url.lastIndexOf("&submit.x="));
		
		url = java.net.URLDecoder.decode(url, "UTF-8");
		
		SlicedURL sUrl = URLSlicer.parseURL(url);
		
		String sliced = ProgramFs.loadString(ProgramFs.getProgramFile("web/sliced.html"));
		sliced = sliced.replaceAll("!SURL!", sUrl.shortenedURL);
		sliced = sliced.replaceAll("!DAYS!", Float.toString((sUrl.decompDelay-(System.currentTimeMillis()-sUrl.creationTime))/(float)(1000*60*60*24)));
		
		h.sendResponseHeaders(200, sliced.length());
        OutputStream os = h.getResponseBody();
        os.write(sliced.getBytes());
        os.close();
        
        logger.logMore("Finished handling slice request", new Object[]{sUrl.url, sUrl.shortenedURL, sUrl.decompDelay});
	}

	@Override
	public String getURLPath() {
		return Start.localContext + "/submit";
	}

}
