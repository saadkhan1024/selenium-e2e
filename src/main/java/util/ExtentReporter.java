package util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ExtentReporter {
    public static ExtentReports getExtentReport() {
        String extentReportFilePath = System.getProperty("user.dir") + "/reports/extentreport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(extentReportFilePath);
        sparkReporter.config().setReportName("Test Automation Results");
        sparkReporter.config().setDocumentTitle("Test Automation");

        ExtentReports extentReport = new ExtentReports();
        extentReport.attachReporter(sparkReporter);
        extentReport.setSystemInfo("Selenium Version", "4.17.0");
        extentReport.setSystemInfo("Operating System", "Mac OS Sonoma");
        extentReport.setSystemInfo("Author", "Saad");

        return extentReport;
    }
}