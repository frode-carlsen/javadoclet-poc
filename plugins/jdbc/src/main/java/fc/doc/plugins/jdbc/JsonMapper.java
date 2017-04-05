package fc.doc.plugins.jdbc;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fc.doc.plugins.jdbc.model.JdbcModel;

public class JsonMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonMapper() {
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    public void writeTo(OutputStream ous, JdbcModel model) throws IOException {
        objectMapper.writer(new DefaultPrettyPrinter()).writeValue(ous, model);
    }

}
