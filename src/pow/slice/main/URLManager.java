package pow.slice.main;

import java.util.ArrayList;
import java.util.Random;

import space.davidboles.lib.program.Logger;

public class URLManager {
	public static Logger logger = Logger.uLogger;
	public static ArrayList<SlicedURL> urls = new ArrayList<SlicedURL>();//TODO Make private
	private static Thread t = null;
	
	public static void addURL(SlicedURL url) {
		synchronized(urls) {
			urls.add(url);
		}
	}
	
	public static SlicedURL getURLFromShort(String shortenedURL) {
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(shortenedURL.equals(urls.get(i).shortenedURL)) {
					return urls.get(i);
				}
			}
		}
		return null;
	}
	
	public static SlicedURL getURLFromFull(String fullURL) {
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(fullURL.equals(urls.get(i).url)) {
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
	
	public static void startMaintainer() {//TODO Test working
		if(t==null || !t.isAlive()){
			t = new Thread(new Runnable(){
				@Override
				public void run() {
					while(true) {
						synchronized (urls) {
							for(int i = 0; i < urls.size(); i++) {
								SlicedURL sUrl = urls.get(i);
								
								if(System.currentTimeMillis()-sUrl.creationTime > sUrl.decompDelay){
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
	
	public static SlicedURL sliceURL(String url, long delay) {
		SlicedURL sU = new SlicedURL(url, null, delay);//Add sURL name
		
		synchronized(urls) {
			sU.shortenedURL = getUniqueSlice(url);
			addURL(sU);
			for(int i = 0; i < urls.size(); i++) {
				System.out.println(urls.get(i).shortenedURL);
			}
		}
		
		return sU;
	}
	
	
	private static String getUniqueSlice(String url) {
		String out = randomString(4);
		
		synchronized(urls) {
			while(getURLFromShort(out) != null) {
				out+=randomString(1);
			}
		}
		
		//TODO add functional thang
		return out;
	}
	
	static final String chars = "23456789qwertyuipasdfghjkzxcvbnmQWERTYUIPASDFGHJKLZXCVBNM";
	static Random rnd = new Random();

	private static String randomString( int len ){
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( chars.charAt( rnd.nextInt(chars.length()) ) );
		return sb.toString();
	}
}
