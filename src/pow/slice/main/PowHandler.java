package pow.slice.main;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import space.davidboles.lib.ht.tp.ContextualHttpHandler;
import space.davidboles.lib.program.Logger;
import space.davidboles.lib.program.ProgramFs;

public class PowHandler extends ContextualHttpHandler {

	Logger logger = Logger.uLogger;
	
	@Override
	public void handle(HttpExchange h) throws IOException {
		
		String request = h.getRequestURI().getPath().substring(1);
	
		SlicedURL sUrl = URLManager.getURLFromShort(request);
		if(sUrl == null) sUrl = new SlicedURL("www.slice.pw" + Start.localContext + "/slice.html", "slice", 0);
		
		//Fix local links
		int delay = 7500;
		if(!sUrl.url.startsWith("http")) {
			sUrl.url = "http://" + sUrl.url;
			delay = 0;
		}
		
		String pow = ProgramFs.loadString(ProgramFs.getProgramFile("web/pow.html"));
		pow = pow.replaceAll("!URL!", sUrl.url);
		pow = pow.replaceAll("!DELAY!", Integer.toString(delay));
		
		h.sendResponseHeaders(200, pow.length());
        OutputStream os = h.getResponseBody();
        os.write(pow.getBytes());
        os.close();
        
        logger.logMore("Finished handling pow request", new Object[]{sUrl.url, sUrl.shortenedURL, sUrl.decompDelay});
	}

	@Override
	public String getURLPath() {
		return "/";
	}

}
