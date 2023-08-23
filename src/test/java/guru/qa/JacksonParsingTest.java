package guru.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.StudentModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JacksonParsingTest {

    private ClassLoader cl = JacksonParsingTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка содержимого JSON")
    void jsonParsingTest() throws Exception {
        try (InputStream stream = cl.getResourceAsStream("home_work_files/student.json");
             Reader reader = new InputStreamReader(stream)) {
            ObjectMapper mapper = new ObjectMapper();
            StudentModel studentModel = mapper.readValue(reader, StudentModel.class);
            Assertions.assertEquals("01", studentModel.getId());
            Assertions.assertEquals("Tom", studentModel.getFirstName());
            Assertions.assertEquals("Tilman", studentModel.getLastName());
            Assertions.assertEquals("english", studentModel.getSubject().getNameSubject());
            Assertions.assertEquals("90", studentModel.getSubject().getExamPoint());
        }
    }
}
