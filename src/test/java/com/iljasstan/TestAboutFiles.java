package com.iljasstan;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class TestAboutFiles {
    @BeforeEach
    void beforeEach() {
        Configuration.startMaximized = true;
    }
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

    @Test
    void downloadExcel() throws Exception {
        String name = "Nereida";
        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("file_example_XLS_50.xls")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(5).getCell(1).getStringCellValue()
                            .equals(name));
        }
    }

    @Test
    void workWithCSV() throws Exception {
        URL url = getClass().getClassLoader().getResource("simple.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));
        List<String[]> strings = reader.readAll();
        assertThat(strings).contains(
                new String[] {"string1", "value1"},
                new String[] {"string2", "value2"},
                new String[] {"string3", "value3"}
        );
    }

    @Test
    void workWithZip() throws Exception {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("zipzip.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipInputStream entry;

        }
    }
}