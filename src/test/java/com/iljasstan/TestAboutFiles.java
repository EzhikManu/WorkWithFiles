package com.iljasstan;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.io.*;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class TestAboutFiles {
    @Test
    void downloadFile() throws IOException {
        open("https://github.com/selenide/selenide/blob/master/README.md");
        File download = $("#raw-url").download();
        String result;
        try (InputStream is = new FileInputStream(download)) {
            result = new String(is.readAllBytes());
        }
        assertThat(result).contains("Selenide = UI Testing Framework powered by Selenium WebDriver");
    }

    @Test
    void uploadFile() {
        open("https://the-internet.herokuapp.com/upload");
        $("#file-upload").uploadFromClasspath("exe.txt");
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(Condition.text("exe.txt"));
    }

    @Test
    void downloadPDFTest() throws IOException {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File download = $(byText("PDF download")).download();
        PDF parsed = new PDF(download);
        assertThat(parsed.author).contains("Marc Philipp");
    }
}