package pow.slice.main;

public class ShortenedURL {
	public String url;
	public String shortenedURL;
	public long creationTime;
	public long deletionDelay;
	
	public ShortenedURL(String url, String shortenedURL, long deletionDelay) {
		this.url = url;
		this.shortenedURL = shortenedURL;
		this.creationTime = System.currentTimeMillis();
		this.deletionDelay = deletionDelay;
	}
}
