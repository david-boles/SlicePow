package pow.slice.main;

public class SlicedURL {
	public String url;
	public String shortenedURL;
	public long creationTime;
	public long decompDelay;
	
	public SlicedURL(String url, String shortenedURL, long decompDelay) {
		this.url = url;
		this.shortenedURL = shortenedURL;
		this.creationTime = System.currentTimeMillis();
		this.decompDelay = decompDelay;
	}
}
