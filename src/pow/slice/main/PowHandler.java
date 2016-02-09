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
		request = request.toLowerCase();
		
		SlicedURL sUrl = URLManager.getURLFromShort(request);

		int delay = 5000;
		if(sUrl == null) {
			sUrl = new SlicedURL("www.slice.pw" + Start.localContext + "/slice.html", "slice", 0);
			delay=0;
		}
		
		//Fix local links
		String url = sUrl.url;
		if(!sUrl.url.startsWith("http")) url = "http://" + sUrl.url;
		
		String pow = ProgramFs.loadString(ProgramFs.getProgramFile("web/pow.html"));
		pow = pow.replaceAll("!URL!", url);
		pow = pow.replaceAll("!DELAY!", Integer.toString(delay));
		
		h.sendResponseHeaders(200, pow.length());
        OutputStream os = h.getResponseBody();
        os.write(pow.getBytes());
        os.close();
        
        logger.logMore("Finished handling pow request", new Object[]{sUrl.shortenedURL, sUrl.url, sUrl.decompDelay});
	}

	@Override
	public String getURLPath() {
		return "/";
	}

}
