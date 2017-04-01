package fc.doc.api;

import com.sun.javadoc.ClassDoc;

public interface Plugin {

    public boolean accept(ClassDoc classDoc);

    void process(Model model) throws Exception;
}
