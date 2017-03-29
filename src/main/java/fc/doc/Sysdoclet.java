package fc.doc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

public class Sysdoclet {
	public static boolean start(RootDoc root) {
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; ++i) {
			System.out.println(classes[i]);
		}
		return true;
	}	
}
