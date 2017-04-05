package fc.doc.plugins.jdbc;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import fc.doc.plugins.jdbc.model.JdbcModel;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.MarkupDocBuilders;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

public class AsciidocMapper {

    public void writeTo(Path path, JdbcModel model) {
        MarkupDocBuilder documentBuilder = MarkupDocBuilders.documentBuilder(MarkupLanguage.ASCIIDOC);
        
        model.apply(0, documentBuilder);
        
        documentBuilder.writeToFile(path, Charset.forName("UTF8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
