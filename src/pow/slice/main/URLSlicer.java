package pow.slice.main;

import java.util.Random;

public class URLSlicer {
	static final String allowed = "1234567890qwertyuiopasdfghjklzxcvbnm";
	static Random random = new Random();
	
	public static SlicedURL parseURL(String url) {
		url = url.toLowerCase();
		
		long decompD = 1000*60*60*24*14;
		String custom = "";
		
		if(url.startsWith("#c")) {
			url = url.substring(2, url.length());
			custom = url.substring(0, url.indexOf('#'));
			url = url.substring(url.indexOf('#')+1, url.length());
			
			System.out.println(custom);
			System.out.println(url);
			
			//TODO custom part of allowed
		}
		
		if(url.startsWith("#i")) {
			url = url.substring(2, url.length());
			decompD = Long.MAX_VALUE;
		}
		
		SlicedURL sUrl = URLManager.getURLFromFull(url);//TODO Reset vars
		if(sUrl == null && custom.equals("")) sUrl = sliceURLRandom(url, decompD);
		else if(sUrl == null && !custom.equals("")) sUrl = sliceURLCustom(url, custom, decompD);
		else {
			synchronized (sUrl) {
				sUrl.creationTime = System.currentTimeMillis();
				if(sUrl.decompDelay < decompD)sUrl.decompDelay = decompD;
			}
		}
		return sUrl;
	}
	
	static SlicedURL sliceURLCustom(String url, String custom, long delay) {
		SlicedURL sU;
		
		synchronized(URLManager.urls) {
			while(URLManager.getURLFromShort(custom) != null) {
				custom+=randomString(1);
			}
			
			sU = new SlicedURL(url, custom, delay);
			URLManager.addURL(sU);
		}

		return sU;
	}
	
	static SlicedURL sliceURLRandom(String url, long delay) {
		SlicedURL sU = new SlicedURL(url, null, delay);//Add sURL name
		
		synchronized(URLManager.urls) {
			sU.shortenedURL = getUniqueRandomSlice(url);
			URLManager.addURL(sU);
		}
		
		return sU;
	}
	
	
	static String getUniqueRandomSlice(String url) {
		String out = randomString(2);
		
		synchronized(URLManager.urls) {
			while(URLManager.getURLFromShort(out) != null) {
				out+=randomString(1);
			}
		}
		
		//TODO add functional thang
		return out;
	}

	static String randomString( int len ){
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( allowed.charAt( random.nextInt(allowed.length()) ) );
		return sb.toString();
	}
}
