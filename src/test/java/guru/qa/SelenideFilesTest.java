package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SelenideFilesTest {

    static {
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Configuration.browserCapabilities = capabilities;
    }

    @Test
    void downloadFileTest() throws IOException {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloaded = $("[data-testid=raw-button]").download();
        try (InputStream is = new FileInputStream(downloaded)) {
            byte[] bytes = is.readAllBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            Assertions.assertTrue(content.contains("This repository is the home of _JUnit 5_."));
        }
    }

    @Test
    void uploadFileTest() {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("cat.png");
        $(".qq-file-name").shouldHave(Condition.text("cat.png"));
    }

    @Test
    void downloadPdfFileTest() throws IOException {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloaded = $("a[href*='junit-user-guide-5.10.0.pdf']").download();
        PDF pdf = new PDF(downloaded);
        Assertions.assertEquals("JUnit 5 User Guide", pdf.title);
    }

    @Test
    void downloadXlsFileTest() throws IOException {
        open("https://excelvba.ru/programmes/Teachers?ysclid=lfcu77j9j9951587711");
        File downloaded = $("a[href*='teachers.xls']").download();
        XLS xls = new XLS(downloaded);
        Assertions.assertEquals("1. Общие положения",
                xls.excel.getSheetAt(0).getRow(1).getCell(4).getStringCellValue());
    }
}
