package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesInZipTest {

    private final ClassLoader cl = FilesInZipTest.class.getClassLoader();

    private ZipInputStream openZipStream() {
        InputStream stream = cl.getResourceAsStream("home_work_files/output.zip");
        return new ZipInputStream(stream);
    }

    private boolean isFileInZip(String fileName, ZipInputStream zipInputStream) throws Exception {
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (entry.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("В zip файле есть нужный csv")
    void csvInZipTest() throws Exception {
        try (ZipInputStream zip = openZipStream()) {
            Assertions.assertTrue(isFileInZip("output.csv", zip));
            Reader reader = new InputStreamReader(zip);
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> content = csvReader.readAll();
            Assertions.assertEquals(10, content.size());


            Assertions.assertArrayEquals(new String[]{"Анна", "Иванова", "123-456-7890", "anna@example.com"}, content.get(0));
            Assertions.assertArrayEquals(new String[]{"Петр", "Петров", "987-654-3210", "petr@example.com"}, content.get(1));
            Assertions.assertArrayEquals(new String[]{"Елена", "Сидорова", "555-123-4567", "elena@example.com"}, content.get(2));
            Assertions.assertArrayEquals(new String[]{"Иван", "Смирнов", "111-222-3333", "ivan@example.com"}, content.get(3));
            Assertions.assertArrayEquals(new String[]{"Мария", "Кузнецова", "444-555-6666", "maria@example.com"}, content.get(4));
            Assertions.assertArrayEquals(new String[]{"Алексей", "Попов", "777-888-9999", "alex@example.com"}, content.get(5));
        }
    }

    @Test
    @DisplayName("В zip файле есть нужный pdf")
    void pdfInZipTest() throws Exception {
        try (ZipInputStream zip = openZipStream()) {
            Assertions.assertTrue(isFileInZip("output.pdf", zip));
            PDF pdf = new PDF(zip);
            Assertions.assertTrue(pdf.text.contains("Файлы PDF (Portable Document Format)"));
        }
    }

    @Test
    @DisplayName("В zip файле есть нужный xls")
    void xslInZipTest() throws Exception {
        try (ZipInputStream zip = openZipStream()) {
            Assertions.assertTrue(isFileInZip("output.xls", zip));
            XLS xls = new XLS(zip);
            Assertions.assertEquals(2587, xls.excel.getSheetAt(0).getRow(3).getCell(7).getNumericCellValue());
        }
    }
}
