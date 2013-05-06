package com.up1.demo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class TestTaradLogin {

	WebDriver driver = null;

	@Before
	public void init() {
		File file = new File("D:\\Somkiat\\research\\test\\selenium\\IEDriverServer_Win32_2.32.3\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private void handleAlert() {
		try {
			driver.switchTo().alert().accept();
		} catch (NoAlertPresentException e) {

		}
	}

	@Test
	public void loginSuccess() {
		driver.get("https://member.tarad.com/");
		handleAlert();
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("42112121");
		driver.findElement(By.className("table_normal")).submit();
		handleAlert();

		assertEquals(driver.getTitle(), "จัดการข้อมูลสมาชิก - ระบบสมาชิกตลาดดอทคอม");
		assertEquals(driver.findElement(By.className("text-red-rakuten")).getText(), "somkiat_pui");

		driver.findElement(By.linkText("ออกจากระบบ")).click();
		driver.switchTo().alert().accept();

	}

	@Test
	public void loginFailureWithWrongPassword() {
		driver.get("https://member.tarad.com/");
		handleAlert();
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("xxxxx");
		driver.findElement(By.className("table_normal")).submit();
		handleAlert();
		assertEquals(driver.getTitle(), "เข้าสู่ระบบ - ระบบสมาชิกตลาดดอทคอม");
		assertEquals(driver.findElement(By.className("box-return-message")).getText(), "รหัสผ่านไม่ถูกต้อง กรุณากรอกรหัสผ่านใหม่");
	}

	@Test
	public void loginFailureWithInvalidPassword() {
		driver.get("https://member.tarad.com/");
		handleAlert();
		driver.findElement(By.id("username")).sendKeys("somkiat_pui");
		driver.findElement(By.id("password")).sendKeys("xxx");
		driver.findElement(By.className("table_normal")).submit();
		handleAlert();

		assertEquals(driver.getTitle(), "เข้าสู่ระบบ - ระบบสมาชิกตลาดดอทคอม");
		assertEquals(driver.findElement(By.className("box-return-message")).getText(), "กรุณากรอกข้อมูลให้ครบ");
	}

	@Test
	public void loginFailureWithUserNotFound() {
		driver.get("https://member.tarad.com/");
		handleAlert();
		driver.findElement(By.id("username")).sendKeys("somkiat_pui1234");
		driver.findElement(By.id("password")).sendKeys("xxxxx");
		driver.findElement(By.className("table_normal")).submit();
		handleAlert();

		assertEquals(driver.getTitle(), "เข้าสู่ระบบ - ระบบสมาชิกตลาดดอทคอม");
		assertEquals(driver.findElement(By.className("box-return-message")).getText(), "ไม่มีชื่อล๊อกอินนี้อยู่ในระบบ กรุณากรอกชื่อล๊อกอินใหม่");
	}

	@After
	public void destroy() {
		driver.quit();
	}

}
