package fc.doc.api;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;

public class PluginHelper {

    public static String getQualifiedTypeName(AnnotationDesc ad) {
        if (ad.annotationType() != null) {
            AnnotationTypeDoc annotationTypeDoc = ad.annotationType();
            return annotationTypeDoc.name();
        }
        return null;
    }

}
