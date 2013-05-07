package com.up1.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestTaradSuperSaleGame {

	private static final String SLOTGAME_URL = "http://www.tarad.com/supersale/game";

	WebDriver driver = null;

	public void init() {
		String PROXY = "devproxy1.ext.corp:8000";
		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
		proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.PROXY, proxy);

		driver = new FirefoxDriver(cap);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	public void destroy() {
		driver.close();
	}

	@Test
	public void loadGameWithGuest() {
		init();
		driver.get(SLOTGAME_URL);
		assertEquals(driver.getTitle(), "[DEV ENV] ซื้อ ขายสินค้าออนไลน์ ร้านค้าออนไลน์ มั่นใจได้ของชัวร์ : TARAD.com");
		assertTrue(driver.findElement(By.id("username")).isDisplayed());
		assertTrue(driver.findElement(By.id("password")).isDisplayed());
		assertTrue(driver.findElement(By.id("button-login")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("ลืมรหัสผ่าน")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("สมัครสมาชิก")).isDisplayed());
		destroy();
	}

	private ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = driver.findElement(by);
				return element.isDisplayed() ? element : null;
			}
		};
	}

	@Test
	public void loginFailureEmptyUsernameandPassword() {
		init();
		driver.get(SLOTGAME_URL);
		// driver.findElement(By.id("button-login")).click();
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('div#button-login').click();", driver.findElement(By.id("button-login")));

		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.id("login_err_msg")));

		assertTrue(element.isDisplayed());
		assertEquals(element.getText(), "* คุณกรอกชื่อล๊อกอิน หรือรหัสผ่านไม่ถูกต้อง");
		destroy();
	}

	@Test
	public void loginFailureUsernameNotFound() {
		init();
		driver.get(SLOTGAME_URL);
		driver.findElement(By.id("username")).sendKeys("somkiat_puixxx");
		driver.findElement(By.id("password")).sendKeys("xxxx");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('div#button-login').click();", driver.findElement(By.id("button-login")));

		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.id("login_err_msg")));

		assertTrue(element.isDisplayed());
		assertEquals(element.getText(), "* คุณกรอกชื่อล๊อกอิน หรือรหัสผ่านไม่ถูกต้อง");

		destroy();
	}

	@Test
	public void loginFailureWithWrongPassword() {
		init();
		driver.get(SLOTGAME_URL);
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("xxxx");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('div#button-login').click();", driver.findElement(By.id("button-login")));

		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.id("login_err_msg")));

		assertTrue(element.isDisplayed());
		assertEquals(element.getText(), "* คุณกรอกชื่อล๊อกอิน หรือรหัสผ่านไม่ถูกต้อง");

		destroy();
	}

	@Test
	public void loginSuccess() {
		init();
		driver.get(SLOTGAME_URL);
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("42112121");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('div#button-login').click();", driver.findElement(By.id("button-login")));
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.id("spinButton")));
		assertTrue(element.isDisplayed());

		destroy();
	}
	
	@Test
	public void loginSuccessWithPlayGame() {
		init();
		driver.get(SLOTGAME_URL);
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("42112121");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('div#button-login').click();", driver.findElement(By.id("button-login")));
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.id("spinButton")));
		assertTrue(element.isDisplayed());
		
		executor.executeScript("$('div#spinButton').click();", driver.findElement(By.id("spinButton")));

		destroy();
	}

}
