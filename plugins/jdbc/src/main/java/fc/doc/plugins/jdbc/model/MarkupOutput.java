package fc.doc.plugins.jdbc.model;

import io.github.swagger2markup.markup.builder.MarkupDocBuilder;

public interface MarkupOutput {

    void apply(int sectionLevel, MarkupDocBuilder doc);
}
