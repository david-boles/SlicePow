package pow.slice.main;
import java.io.IOException;

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
			s = new HTTPServerSimpleManager(2140);
			s.addHandler(new FolderHttpHandler(localContext, ProgramFs.getProgramFile("web")));
			s.addHandler(new SliceHandler());
			s.addHandler(new PowHandler());
			URLManager.startMaintainer();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
