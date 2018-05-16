package Tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FreeCRMTest {
	WebDriver driver;
	ExtentReports extent;
	ExtentTest extentTest;
	@BeforeTest
	public void setUpExtent()
	{
		extent=new ExtentReports(System.getProperty("user.dir")+"\\test-output1\\ExtentReport.html",true);
		extent.addSystemInfo("UserName : ","Love");
		extent.addSystemInfo("Environment ","Automation test");
	}
	@AfterTest
	public void afterTest()
	{
		extent.flush();
		extent.close();
	}
	public static String getScreenshot(WebDriver driver,String screenshotname) 
	{
		String date=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		String dest="D:\\Naveen Automation\\java Practical\\ExtentScreenshot\\"+screenshotname+date+".png";
		//String dest=System.getProperty("user.dir")+":\\FailedTestScreenshot\\"+screenshotname+date+".png";
		File fdest=new File(dest);
		try {
			FileHandler.copy(src, fdest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dest;
	}
	@BeforeMethod
	public void setUp()
	{System.setProperty("webdriver.gecko.driver","D:\\Love_Testing\\Senium_Naveen\\gecko_Driver\\geckodriver-v0.20.1-win64\\geckodriver.exe");
		driver=new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.freecrm.com/index.html");
	}
	@Test	
	public void titleTest()
	{
		extentTest=extent.startTest("testtitle");
		String title=driver.getTitle();
		Assert.assertEquals(title, "RM software in the cloud powers sales and customer service");
	}
	@AfterMethod
	public void tearDown(ITestResult it) 
	{
		if(it.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL, "Test Fail :"+it.getName());
			extentTest.log(LogStatus.FAIL,"Test Fail :"+it.getThrowable());
			String sspath=FreeCRMTest.getScreenshot(driver, it.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(sspath));
		}
		else if(it.getStatus()==ITestResult.SKIP)	
		{
			extentTest.log(LogStatus.SKIP, "Test skip is :"+it.getName());
		}
		else if(it.getStatus()==ITestResult.SUCCESS)
		{
			extentTest.log(LogStatus.PASS, "Test case Passed :"+it.getName());
		} 
		extent.endTest(extentTest);
		driver.quit();
		
	}
}