package tests;

import base.BaseTest;

import org.openqa.selenium.*;
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

import java.util.HashMap;

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

        HomePage homePage = new HomePage(driver);
        homePage.clickAccountDropdown();
        homePage.selectLoginOption();

        LoginPage loginPage = new LoginPage(driver);
        //loginPage.inputEmail(username);
        loginPage.inputEmail(hashMap.get("Username"));

        //loginPage.inputPassword(password);
        loginPage.inputPassword(hashMap.get("Password"));

        loginPage.clickLoginButton();

        if(hashMap.get("ExpectedResult").equals("Failure"))
        //if(expectedResult.equals("Failure"))
            Assert.assertEquals(loginPage.getInvalidLoginMessage(), "Warning: No match for E-Mail Address and/or Password.");
        else if(hashMap.get("ExpectedResult").equals("Success")) {
        //else if(expectedResult.equals("Success")) {
            AccountPage accountPage = new AccountPage(driver);
            Assert.assertTrue(accountPage.presenceOfEditAccountMessage());
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
}