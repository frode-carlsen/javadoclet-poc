package fc.doc.plugins;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;

import fc.doc.api.Model;
import fc.doc.api.Plugin;
import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.PageBreakLocations;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.models.Info;
import io.swagger.models.Swagger;

public class SwaggerPlugin implements Plugin {

    private static final String DEFAULT_SWAGGER_JSON = "swagger.json";
    private static final String DEFAULT_OUTPUTDIR = "./target/swagger/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Swagger swagger = new Swagger();

    public SwaggerPlugin() {
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.writerWithDefaultPrettyPrinter();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    @Override
    public boolean accept(ClassDoc cls) {
        if (cls.isOrdinaryClass()) {
            AnnotationDesc[] annotations = cls.annotations();
            for (AnnotationDesc ad : annotations) {
                AnnotationTypeDoc annotationTypeDoc = ad.annotationType();
                String qualifiedTypeName = annotationTypeDoc.qualifiedTypeName();

                if (isJaxRsApi(qualifiedTypeName)) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean isJaxRsApi(String qualifiedTypeName) {
        return Objects.equals(Path.class.getName(), qualifiedTypeName);
    }

    @Override
    public void process(Model model) throws Exception {
        Stream<Model.Entry> entries = model.getEntries(this);
        Set<Class<?>> classes = entries.map(e -> e.getTargetClass()).collect(Collectors.toSet());

        DefaultReaderConfig readerConfig = new DefaultReaderConfig();
        readerConfig.setScanAllResources(true);
        Reader reader = new Reader(swagger, readerConfig);
        Info info = new Info();
        info.setTitle("REST API Sysdoc");
        swagger.setInfo(info);
        reader.read(classes);

        String swaggerJson = objectMapper.writeValueAsString(swagger);

        File to = new File(DEFAULT_OUTPUTDIR);
        to.mkdirs();
        File outputFile = new File(to, DEFAULT_SWAGGER_JSON);
        Files.write(outputFile.toPath(), Arrays.asList(swaggerJson), Charset.forName("UTF8"));

        writeSwaggerDoc(swaggerJson, outputFile, to);

    }

    private void writeSwaggerDoc(String swaggerJson, File swaggerFile, File outputDir) {
        Swagger2MarkupConfig swagger2MarkupConfig = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withInterDocumentCrossReferences()
                .withPageBreaks(Arrays.asList(PageBreakLocations.BEFORE_DEFINITION, PageBreakLocations.BEFORE_OPERATION))
//                .withSeparatedDefinitions()
//                .withSeparatedOperations()
                .build();
        Swagger2MarkupConverter converter = Swagger2MarkupConverter.from(swaggerFile.toURI())
                .withConfig(swagger2MarkupConfig).build();
        converter.toFolder(outputDir.toPath());
    }

}
