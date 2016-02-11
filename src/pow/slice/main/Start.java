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
			String in = scan.nextLine();
			try {
				if(in.equals("stop")) {
					scan.close();
					stop();
				}
				else if(in.equals("reset")) {
					ArrayList<SlicedURL> urls = URLManager.getAll();
					synchronized (urls) {
						urls.clear();
					}
				}
				else if(in.equals("list")) {
					ArrayList<SlicedURL> urls = URLManager.getAll();
					synchronized (urls) {
						for(int i = 0; i < urls.size(); i++) {
							SlicedURL sUrl = urls.get(i);
							Logger.uLogger.logMore("short URL, URL, hours remaining", new Object[]{sUrl.shortenedURL, sUrl.url, (sUrl.decompDelay-(System.currentTimeMillis()-sUrl.creationTime))/(float)(1000*60*60)});
						}
					}
				}else if(in.equals("listinf")) {
					ArrayList<SlicedURL> urls = URLManager.getAll();
					synchronized (urls) {
						for(int i = 0; i < urls.size(); i++) {
							SlicedURL sUrl = urls.get(i);
							if(sUrl.decompDelay == Long.MAX_VALUE) Logger.uLogger.logMore("short URL, URL, hours remaining", new Object[]{sUrl.shortenedURL, sUrl.url, (sUrl.decompDelay-(System.currentTimeMillis()-sUrl.creationTime))/(float)(1000*60*60)});
						}
					}
				}else if(in.equals("save")) {
					URLManager.saveURLs();
				}else if(in.equals("load")) {
					URLManager.loadURLs();
				}
				else if(in.startsWith("remove")) {
					System.out.println(in);
					String url = in.substring(7);
					System.out.println(url);
					Logger.uLogger.log("Successful?", URLManager.removeURL(url));
				}
				else if(in.startsWith("sremove")) {
					String url = in.substring(8);
					Logger.uLogger.log("Successful?", URLManager.removeSlicedURL(url));
				}
				else {
					Logger.uLogger.log("Incorrect command.");
				}
			}catch(Exception e) {
				Logger.uLogger.error("Console command errored");
			}
		}
	}
	
	public static void stop() {
		Logger.uLogger.log("Stopping:");
		URLManager.stopMaintainer();
		URLManager.saveURLs();
		System.exit(0);
	}

}
