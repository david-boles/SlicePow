package pow.slice.main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import space.davidboles.lib.ht.tp.FolderHttpHandler;
import space.davidboles.lib.ht.tp.HTTPServerSimpleManager;
import space.davidboles.lib.program.Logger;
import space.davidboles.lib.program.ProgramFs;

public class Start {

	static HTTPServerSimpleManager s;
	static String localContext = "/local";
	
	public static void main(String[] args) {
		//Logger.uLogger = new Logger(ProgramFs.getProgramFile("logs"), ProgramFs.getProgramFile("errors"));
		try {
			URLManager.loadURLs();
			URLManager.startMaintainer();
			s = new HTTPServerSimpleManager(2140);
			s.addHandler(new FolderHttpHandler(localContext, ProgramFs.getProgramFile("web")));
			s.addHandler(new SliceHandler());
			s.addHandler(new PowHandler());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scanner scan = new Scanner(System.in);
		while(!Thread.interrupted()) {
			String in = scan.next();
			if(in.equals("stop")) {
				scan.close();
				stop();
			}
			if(in.equals("reset")) {
				ArrayList<SlicedURL> urls = URLManager.getAll();
				synchronized (urls) {
					urls.clear();
					URLManager.saveURLs();
				}
			}
			if(in.equals("list")) {
				ArrayList<SlicedURL> urls = URLManager.getAll();
				synchronized (urls) {
					for(int i = 0; i < urls.size(); i++) {
						SlicedURL sUrl = urls.get(i);
						Logger.uLogger.logMore("URL, short URL, hours remaining", new Object[]{sUrl.url, sUrl.shortenedURL, (sUrl.decompDelay-(System.currentTimeMillis()-sUrl.creationTime))/(float)(1000*60*60)});
					}
				}
			}
		}
	}
	
	public static void stop() {
		Logger.uLogger.log("Stopping");
		URLManager.stopMaintainer();
		URLManager.saveURLs();
		System.exit(0);
	}

}
