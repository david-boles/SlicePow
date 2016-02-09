package pow.slice.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import space.davidboles.lib.program.Logger;
import space.davidboles.lib.program.ProgramFs;

public class URLManager {
	public static Logger logger = Logger.uLogger;
	static ArrayList<SlicedURL> urls = new ArrayList<SlicedURL>();//TODO Make private
	private static Thread t = null;
	public static File urlFolder = ProgramFs.getProgramFile("urls");
	
	public static ArrayList<SlicedURL> getAll() {
		synchronized (urls) {
			return urls;
		}
	}
	
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
	
	public static boolean removeSlicedURL(String shortenedURL) {
		boolean removed = false;
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(shortenedURL.equals(urls.get(i).shortenedURL)) {
					urls.remove(i);
					removed = true;
				}
			}
		}
		return removed;
	}
	
	public static boolean removeURL(String URL) {
		boolean removed = false;
		synchronized (urls) {
			for(int i = 0; i < urls.size(); i++) {
				if(URL.equals(urls.get(i).url)) {
					urls.remove(i);
					removed = true;
				}
			}
		}
		return removed;
	}
	
	public static void startMaintainer() {//TODO Test working
		if(t==null || !t.isAlive()){
			t = new Thread(new Runnable(){
				@Override
				public void run() {
					logger.log("URLManager maintainer thread started!");
					boolean stopped = false;
					while(!stopped) {
						synchronized (urls) {synchronized (logger) {
							Logger.uLogger.log("Current shortened URLs:");
							for(int i = 0; i < urls.size(); i++) {
								SlicedURL sUrl = urls.get(i);
								
								if(System.currentTimeMillis()-sUrl.creationTime > sUrl.decompDelay){
									removeSlicedURL(sUrl.shortenedURL);
									logger.log("Removed", sUrl.shortenedURL);
								}
								
								
								logger.logMore("Checked following, info and remaining hours", new Object[]{sUrl.url, sUrl.shortenedURL, (sUrl.decompDelay-(System.currentTimeMillis()-sUrl.creationTime))/(float)(1000*60*60)});
								
							}
							
						}}

						if (!t.isInterrupted())saveURLs();
						
						try {
							Thread.sleep(1000*60*5);
						} catch (InterruptedException e) {
							stopped = true;
							logger.error("Maintainer interrupted");
						}
						
					}
				}
			});
			t.start();
		}
	}
	
	public static void stopMaintainer() {
		t.interrupt();
	}
	
		
	public static void loadURLs() {//TODO Broken
		synchronized (urls) {
			if(urls.size() == 0) {
				try {
					/*System.out.println("Loading URLs...");
					System.out.println((SlicedURL[])ProgramFs.loadObject(ProgramFs.getProgramFile("urls")));
					urls = new ArrayList<SlicedURL>(Arrays.asList((SlicedURL[])ProgramFs.loadObject(ProgramFs.getProgramFile("urls"))));*/
					
					logger.log("Loading URLs...");
					if(urlFolder.isDirectory() || urlFolder.mkdirs()) {
						File[] sUrlF = urlFolder.listFiles();
						
						for(int i = 0; i < sUrlF.length; i++) {
							FileInputStream saveFile = new FileInputStream(sUrlF[i]);
							ObjectInputStream restore = new ObjectInputStream(saveFile);
							
							String url = (String) restore.readObject();
							String shortenedURL = (String) restore.readObject();
							long creationTime = restore.readLong();
							long decompDelay = restore.readLong();
							restore.close();
							
							SlicedURL loaded = new SlicedURL();
							loaded.url = url;
							loaded.shortenedURL = shortenedURL;
							loaded.creationTime = creationTime;
							loaded.decompDelay = decompDelay;
							
							URLManager.urls.add(loaded);
							logger.logMore("Finished loading", new Object[]{url, shortenedURL, creationTime, decompDelay});
							
						}
						
					}else {
						logger.error("Loading failed due missing URL folder.");
					}
					
				}catch(Exception e) {logger.exception("URL Loading", e);}
			}else {
				logger.errorMore("Loading failed due already populated url list", urls.toArray());
			}
		}
	}
	
	public static void saveURLs() {//TODO Broken
		if(!Thread.interrupted()) {
			synchronized (urls) {
				try {
					logger.log("Saving URLs...");
					
					if (emptyCreateURLFolder()) {
						for(int i = 0; i < urls.size(); i++) {
							FileOutputStream saveFile = new FileOutputStream(new File(urlFolder.getCanonicalPath()+"/" + urls.get(i).shortenedURL));
							ObjectOutputStream save = new ObjectOutputStream(saveFile);
							
							SlicedURL out = urls.get(i);
							save.writeObject(out.url);
							save.writeObject(out.shortenedURL);
							save.writeLong(out.creationTime);
							save.writeLong(out.decompDelay);
							
							save.close();
						}
					}
					
					logger.log("Save complete!");
				}catch(Exception e) {logger.exception("URL Saving", e);}
				
			}
		}else {
			logger.error("Saving failed, thread interrupted");
		}
	}
	
	private static boolean emptyCreateURLFolder() {
		try {
			if(urlFolder.isDirectory() || urlFolder.mkdirs()) {
				File[] sUrlF = urlFolder.listFiles();
				
				for(int i = 0; i < sUrlF.length; i++) {
					sUrlF[i].delete();
				}
				
				return true;
			}else {
				logger.error("Folder creation/ emptying failed due to missing URL folder.");
			}
		}catch(Exception e) {logger.exception("Folder creation/ emptying", e);}
		
		return false;
	}
}
