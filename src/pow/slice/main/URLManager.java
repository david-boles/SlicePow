package pow.slice.main;

import java.util.ArrayList;

import space.davidboles.lib.program.Logger;

public class URLManager {
	public static Logger logger = Logger.uLogger;
	private static ArrayList<ShortenedURL> urls = new ArrayList<ShortenedURL>();
	private static Thread t = null;
	
	public static void addURL(ShortenedURL url) {
		synchronized(urls) {
			urls.add(url);
		}
	}
	
	public static ShortenedURL getURL(String shortenedURL) {
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(shortenedURL.equals(urls.get(i).shortenedURL)) {
					return urls.get(i);
				}
			}
		}
		return null;
	}
	
	public static void removeURL(String shortenedURL) {
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(shortenedURL.equals(urls.get(i).shortenedURL)) {
					urls.remove(i);
				}
			}
		}
	}
	
	public static void startMaintainer() {
		if(t==null || !t.isAlive()){
			t = new Thread(new Runnable(){
				@Override
				public void run() {
					while(true) {
						synchronized (urls) {
							for(int i = 0; i < urls.size(); i++) {
								ShortenedURL sUrl = urls.get(i);
								
								if(System.currentTimeMillis()-sUrl.creationTime > sUrl.deletionDelay){
									removeURL(sUrl.shortenedURL);
									logger.log("Removed", sUrl.shortenedURL);
								}
								
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {}
							}
						}
					}
				}
			});
			t.start();
		}
	}
}
