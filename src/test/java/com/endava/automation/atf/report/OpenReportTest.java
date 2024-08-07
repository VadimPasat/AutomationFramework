package com.endava.automation.atf.report;

import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OpenReportTest {

    @Test
    public void openReport() {
        try {
            String url = "target/test-output/HtmlReport/ExtentHtml.html";
            File htmlFile = new File(url);
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

