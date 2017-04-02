package fc.doc.plugin.swagger;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;

import fc.doc.EasyDoclet;
import fc.doc.Sysdoclet;

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
        while (!(new File(file, "src/test/java").exists())
                && file.getParentFile() != null) {
            file = file.getParentFile();
        }
        if (file != null) {
            return new File(file, "src/test/java");
        } else {
            throw new IllegalStateException("Cannot find test code");
        }
    }
}
