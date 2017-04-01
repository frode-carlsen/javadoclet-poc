package fc.doc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.sun.javadoc.ClassDoc;

public class Model {

    private List<Entry> entries = new ArrayList<>();

    public static class Entry {
        private Class<?> cls;
        private Plugin plugin;
        private ClassDoc classDoc;

        public Entry(ClassDoc classDoc, Plugin plugin) {
            this.classDoc = classDoc;
            this.plugin = plugin;
        }

        public Class<?> getTargetClass() {
            if(cls==null){
                try {
                    cls = Class.forName(classDoc.qualifiedTypeName());
                } catch (ClassNotFoundException e) {
                   throw new IllegalStateException("Missing class", e);
                }
            }
            return cls;
        }

        public Plugin getPlugin() {
            return plugin;
        }

        public ClassDoc getClassDoc() {
            return classDoc;
        }
    }

    public void add(ClassDoc classDoc, Plugin plugin) {
        entries.add(new Entry(classDoc, plugin));
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public Stream<Entry> getEntries(Plugin plugin) {
        return entries.stream().filter( e -> e.getPlugin() == plugin);
    }
}
