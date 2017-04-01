package fc.doc;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class FindComponentsTest {

	@Test
	public void skal_finne_relevante_komponenter_å_generere_for() throws Exception {
		EasyDoclet doclet = new EasyDoclet(getTestCodeLocation(), "fc.doc");
		Sysdoclet.start(doclet.getRootDoc());
	}

    private File getTestCodeLocation() throws URISyntaxException {
        URL location = TestDoc.class.getProtectionDomain().getCodeSource().getLocation();
	    String externalForm = location.toURI().getPath().replaceAll("target/classes/", "");
        return new File(externalForm, "src/main/java");
    }
}
