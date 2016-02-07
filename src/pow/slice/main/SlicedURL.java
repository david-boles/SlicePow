package pow.slice.main;

import java.io.Serializable;

public class SlicedURL implements Serializable {
	private static final long serialVersionUID = 5591900112116791172L;
	public String url;
	public String shortenedURL;
	public long creationTime;
	public long decompDelay;
	
	public SlicedURL() {
		
	}
	
	public SlicedURL(String url, String shortenedURL, long decompDelay) {
		this.url = url;
		this.shortenedURL = shortenedURL;
		this.creationTime = System.currentTimeMillis();
		this.decompDelay = decompDelay;
	}
}
