import java.io.IOException;

import space.davidboles.lib.ht.tp.FolderHttpHandler;
import space.davidboles.lib.ht.tp.HTTPServerSimpleManager;
import space.davidboles.lib.program.ProgramFs;

public class Start {

	static HTTPServerSimpleManager s;
	
	public static void main(String[] args) {
		try {
			s = new HTTPServerSimpleManager(Integer.parseInt(args[0]));
			s.addHandler(new FolderHttpHandler("/local", ProgramFs.getProgramFile("web")));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
