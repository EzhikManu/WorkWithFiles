package com.iljasstan;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestAboutFilesHW {
    @BeforeEach
    void beforeEach() {
        Configuration.startMaximized = true;
    }

    @Test
    @DisplayName("Проверка файла .txt")
    void assertionTxtFile() throws Exception {
        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("exe.txt")) {
           String text = new String(stream.readAllBytes());
           assertThat(text).contains("hello!");
        }
    }

    @Test
    @DisplayName("Проверка файла .pdf")
    void assertionPDF() throws IOException {
        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("Roman-Savin.pdf")) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.numberOfPages > 10);
        }
    }

    @Test
    @DisplayName("Проверка файла Excel")
    void downloadExcel() throws Exception {
        String name = "Nereida";
        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("file_example_XLS_50.xls")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(5).getCell(1).getStringCellValue()
                            .equals(name));
        }
    }

    @Test
    @DisplayName("Проверка архива ZIP")
    void workWithZip() throws Exception {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream("src//test//resources//zipzip.zip"))) {
            ZipEntry entry;
            String name;
            long size;
            ArrayList<String> list = new ArrayList<>();
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                size = entry.getSize();
                assertThat(size).isLessThanOrEqualTo(50); // проверяю, что каждый файл меньше 50 байт
                list.add(name);
            }
            assertThat(list).contains("ogo.jar"); // проверяю, что в архиве есть файл "ogo.jar"
        }
    }

    @Test
    @DisplayName("Проверка файла .doc")
    void workWithDoc() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("simpledoc.docx")) {
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            assertThat(paragraphs.get(0).getText()).contains("Java is the #1 programming language");
        }
    }
}