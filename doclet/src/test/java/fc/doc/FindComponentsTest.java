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
        return getSampleProjectSourceLocation();
    }

    private File getSampleProjectSourceLocation() {
        File file = new File(".").getAbsoluteFile();
        while (!(new File(file, "sample-project").exists()) 
                && file.getParentFile() != null) {
            file = file.getParentFile();
        }
        if (file != null) {
            return new File(file, "sample-project/src/main/java");
        } else {
            throw new IllegalStateException("Cannot find sample-project");
        }
    }
}
