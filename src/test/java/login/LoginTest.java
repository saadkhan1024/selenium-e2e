package login;

import base.BaseTest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.AccountPage;
import page.HomePage;
import page.LoginPage;
import util.DataUtil;
import util.MyXLSReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoginTest extends BaseTest {
    WebDriver driver;
    MyXLSReader excelReader;

    @Test(dataProvider = "dataSupplier")
    //public void testLogin(String runmode, String browser, String username, String password, String expectedResult) {
    public void testLogin(HashMap<String, String> hashMap) {
        if(!DataUtil.isRunnable(excelReader, "LoginTest", "Testcases") || hashMap.get("Runmode").equals("N"))
            throw new SkipException("Runmode is set to N, hence not executed");

        //driver = openBrowserAndApplication(browser);
        driver = openBrowserAndApplication(hashMap.get("Browser"));
        String window = driver.getWindowHandle();
        String tabText = driver.getTitle();
        System.out.println(window);
        System.out.println(tabText);

        HomePage homePage = new HomePage(driver);
        homePage.clickAccountDropdown();
        homePage.selectLoginOption();

        LoginPage loginPage = new LoginPage(driver);
        //loginPage.inputEmail(username);
        loginPage.inputEmail(hashMap.get("Username"));

        //loginPage.inputPassword(password);
        loginPage.inputPassword(hashMap.get("Password"));

        loginPage.clickLoginButton();
        /*driver.switchTo().newWindow(WindowType.TAB);
        driver.manage().addCookie(new Cookie());
        driver.manage().deleteCookieNamed();*/

        if(hashMap.get("ExpectedResult").equals("Failure"))
        //if(expectedResult.equals("Failure"))
            Assert.assertEquals(loginPage.getInvalidLoginMessage(), "Warning: No match for E-Mail Address and/or Password.");
        else if(hashMap.get("ExpectedResult").equals("Success")) {
        //else if(expectedResult.equals("Success")) {
            AccountPage accountPage = new AccountPage(driver);
            Assert.assertTrue(accountPage.presenceOfEditAccountMessage());
        }
        FirefoxOptions op = new FirefoxOptions();
        op.addArguments("--headless=true");
        Wait<WebDriver> wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        Wait<WebDriver> wait2 = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(2))
                .ignoring(ElementNotInteractableException.class);
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
                System.out.println(url + ": " + connection.getResponseMessage());

            if(connection.getResponseCode() == 404)
                System.out.println(url + ": " + connection.getResponseMessage());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DataProvider
    public Object[][] dataSupplier() {
        Object[][] excelData = null;

        try {
            excelReader = new MyXLSReader("src/test/resources/TestExcel.xlsx");
            excelData = DataUtil.getTestData(excelReader, "LoginTest", "Data");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return excelData;
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
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