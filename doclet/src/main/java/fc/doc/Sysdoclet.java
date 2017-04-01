package fc.doc;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import fc.doc.api.Model;
import fc.doc.api.Plugin;

public class Sysdoclet {

    public static boolean start(RootDoc root) throws Exception {

        // Parse others
        List<Plugin> plugins = getPlugins();

        ClassDoc[] classes = root.classes();
        Model model = new Model();

        discoverClasses(plugins, classes, model);

        for (Plugin pl : plugins) {
            pl.process(model);
        }
        return true;
    }

    private static void discoverClasses(List<Plugin> plugins, ClassDoc[] classes, Model model) {
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc classDoc = classes[i];
            for (Plugin pl : plugins) {
                if (pl.accept(classDoc)) {
                    model.add(classDoc, pl);
                }
            }
        }
    }

    private static List<Plugin> getPlugins() {
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        return StreamSupport.stream(loader.spliterator(), false).collect(Collectors.toList());
    }
}
