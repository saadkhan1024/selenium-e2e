package experiments;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Set;

public class SeleniumTest {
    WebDriver driver;
    public void syntaxTest() {
        driver = new ChromeDriver();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.manage().addCookie(new Cookie("name", "value"));
        driver.manage().deleteCookieNamed("name");

        FirefoxOptions op = new FirefoxOptions();
        op.addArguments("--headless=true");
        driver = new FirefoxDriver(op);

        Wait<WebDriver> wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        Wait<WebDriver> wait2 = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(ElementNotInteractableException.class);

        driver.findElement(RelativeLocator.with(By.tagName("input")).above(By.id("tests")));
        driver.findElement(RelativeLocator.with(By.tagName("input")).below(By.id("tests")));
        driver.findElement(RelativeLocator.with(By.tagName("input")).toLeftOf(By.id("tests")));
        driver.findElement(RelativeLocator.with(By.tagName("input")).toRightOf(By.id("tests")));

        Set<String> windows = driver.getWindowHandles();
        String[] windowsArr = new String[windows.size()];
        int index = 0;

        for(String window : windows) {
            windowsArr[index] = window;
            index++;
        }

        driver.switchTo().window(windowsArr[2]);
        driver.switchTo().window(windowsArr[4]);
        driver.switchTo().window(windowsArr[1]);
        driver.close();

    }

    public static void getScreenshot(WebDriver driver) throws IOException {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotFilePath = System.getProperty("user.dir") + "/failurescreenshots/screenshot.png";
        FileUtils.copyFile(source, new File(screenshotFilePath));
    }

//    public void multipart() {
//        given()
//                .contentType("multipart/form-data")
//                .multiPart("File1", new File(System.getProperty("user.dir") + "/test1.png"))
//                .multiPart("File2", new File(System.getProperty("user.dir") + "/test2.png"))
//                .when()
//                .post("/upload")
//                .then()
//                .statusCode(201);
//    }

    @Test
    public void verifyLink() {
        String url = "https://tutorialsninja.com/demo/";

        try {
            URL urlpath = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlpath.openConnection();
            connection.setConnectTimeout(2000);
            connection.connect();

            if(connection.getResponseCode() == 200)
                Assert.assertEquals(connection.getResponseMessage(), "OK");
            if(connection.getResponseCode() == 404)
                Assert.assertEquals(connection.getResponseMessage(), "Not Found");
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DataProvider
    public Object[][] getExcelData() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/test/resources/TestExcel.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workbook.getSheet("Data");
        int rowCount = sheet.getLastRowNum();
        int cellCount = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[rowCount][cellCount];

        for(int r = 0; r < rowCount; r++) {
            XSSFRow row = sheet.getRow(r+2);
            for(int c = 0; c < cellCount; c++) {
                XSSFCell cell = row.getCell(c);
                if(cell.getCellType() == Cell.CELL_TYPE_STRING)
                    data[r][c] = cell.getStringCellValue();
                else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                    data[r][c] = String.valueOf(cell.getNumericCellValue());
                else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
                    data[r][c] = String.valueOf(cell.getBooleanCellValue());
            }
        }
        return data;
    }
}
