package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static java.lang.Thread.sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	private static WebDriver driver;

	private static String baseUrl;

	private String fname="Annika";

	private String lname="Geng";

	private String uname="bai";

	private String pass="123456";

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private CredentialService credentialService;

	private Logger logger = LoggerFactory.getLogger(CloudStorageApplicationTests.class);
	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException {
		//this.driver = new ChromeDriver();
		baseUrl = "http://localhost:" + this.port;
		sleep(1000);
	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		sleep(2000);
	}

	@Test
	@Order(1)
	/**
	 * test if an unauthorized user can get access to other page without login and signup
	 */
	public void getOtherPage() throws InterruptedException {

		logger.error("test security");

		driver.get(baseUrl+"/login");
		Assertions.assertEquals("Login", driver.getTitle());
		sleep(2000);

		driver.get(baseUrl+"/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
		sleep(2000);

		driver.get(baseUrl+"/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(2)
	/**
	 * test signup function
	 */
	public void signup() throws InterruptedException {
		logger.error("test sign up");
		driver.get(baseUrl+"/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(fname, lname, uname, pass);
		sleep(4000);
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(3)
	/**
	 * test if user login successfully, redirect to home
	 */
	public void login() throws InterruptedException {
		logger.error("test login");

//		driver.get(baseUrl+"/signup");
//		SignupPage signupPage = new SignupPage(driver);
//		signupPage.signup(fname, lname, uname, pass);
//		sleep(1000);

		driver.get(baseUrl+"/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.Login(uname, pass);
		sleep(1000);
		Assertions.assertEquals("Home", driver.getTitle());

		driver.get(baseUrl+"/home");
		HomePage homePage = new HomePage(driver);
		homePage.logoutClick();
		sleep(1000);
		Assertions.assertNotEquals("Home", driver.getTitle());

		driver.get(baseUrl+"/login");
		loginPage.Login(uname, pass);
		sleep(1000);
		Assertions.assertEquals("Home", driver.getTitle());
	}

	public void waitForVisibility(String id) {
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
	}

	@Test
	@Order(4)
	public void testNotes() throws Exception {
		logger.error("test notes");

//		driver.get(baseUrl+"/signup");
//		SignupPage signupPage = new SignupPage(driver);
//		signupPage.signup(fname, lname, uname, pass);
//		sleep(1000);
//
//		driver.get(baseUrl+"/login");
//		LoginPage loginPage = new LoginPage(driver);
//		loginPage.Login(uname, pass);
//		sleep(1000);


		driver.get(baseUrl+"/home");
		NotePage notePage = new NotePage(driver);

		notePage.clickNoteTab();
		sleep(1000);

		notePage.clickAddNoteBtn();
		sleep(500);

		notePage.addNote("new note", "this is a note");
		sleep(2000);

		notePage.clickNoteTab();
		sleep(1000);

		Assertions.assertEquals(notePage.getNoteTitleDisplay(), "new note");
		Assertions.assertEquals(notePage.getNoteDescDisplay(), "this is a note");
		sleep(3000);

		// verify edit
		notePage.clickNoteTab();
		sleep(1000);

		notePage.clickNoteEditBtn();
		notePage.changeNoteTitle("changed");
		notePage.changeNoteDesc("a changed content");
		notePage.clickSubmitBtn();
		sleep(1000);

		Assertions.assertEquals(notePage.getNoteTitleDisplay(), "changed");
		Assertions.assertEquals(notePage.getNoteDescDisplay(), "a changed content");
		sleep(3000);

		// verify delete
		notePage.clickNoteTab();
		sleep(1000);

		notePage.clickNoteDeleteBtn();

		sleep(3000);

		notePage.clickNoteTab();

		Assertions.assertEquals(0, notePage.getNoteEditBtns().size());
	}


	@Test
	@Order(5)
	public void testCredentials() throws Exception {
		logger.error("test credentials");

//		driver.get(baseUrl+"/signup");
//		SignupPage signupPage = new SignupPage(driver);
//		signupPage.signup(fname, lname, uname, pass);
//		sleep(1000);
//
//		driver.get(baseUrl+"/login");
//		LoginPage loginPage = new LoginPage(driver);
//		loginPage.Login(uname, pass);
//		sleep(1000);

		driver.get(baseUrl+"/home");

		CredentialPage credentialPage = new CredentialPage(driver);

		credentialPage.clickCrenTab();
		sleep(1000);

		// test add credentials
		//waitForVisibility(credentialPage.getAddCrenBtnId());
		credentialPage.clickAddCrenBtn();

		credentialPage.inputCrenUrl("www.bing.com");
		credentialPage.inputCrenUsername("annika");
		credentialPage.inputCrenPassword("123456");
		credentialPage.clickCrenSubmitBtn();
		sleep(2000);

		//waitForVisibility(credentialPage.getCrenTabId());
		credentialPage.clickCrenTab();
		sleep(1000);

		String displayUrl = credentialPage.getUrl(0);
		String displayUsername = credentialPage.getUsername(0);
		String displayPassword = credentialPage.getPass(0);

		String key = credentialService.getKeyById(1);
		displayPassword = encryptionService.decryptValue(displayPassword, key);

		Assertions.assertEquals(displayUrl, "www.bing.com");
		Assertions.assertEquals(displayUsername, "annika");
		Assertions.assertEquals(displayPassword, "123456");


		// test edit credential
		credentialPage.clickCrenTab();
		sleep(1000);

		credentialPage.clickEditBtn(0);
		sleep(1000);

		credentialPage.inputCrenUrl("www.google.com");
		credentialPage.inputCrenUsername("anni");
		credentialPage.inputCrenPassword("1234567");
		credentialPage.clickCrenSubmitBtn();
		sleep(2000);

		//waitForVisibility(credentialPage.getCrenTabId());
		credentialPage.clickCrenTab();
		sleep(1000);

		String changedUrl = credentialPage.getUrl(0);
		String changedUsername = credentialPage.getUsername(0);
		String changedPassword = credentialPage.getPass(0);

		String Key = credentialService.getKeyById(1);
		changedPassword = encryptionService.decryptValue(changedPassword, Key);

		Assertions.assertEquals(changedUrl, "www.google.com");
		Assertions.assertEquals(changedUsername, "anni");
		Assertions.assertEquals(changedPassword, "1234567");

		// verify delete btn
		credentialPage.clickCrenTab();
		sleep(1000);

		credentialPage.clickDeleteBtn(0);
		sleep(3000);

		credentialPage.clickCrenTab();
		sleep(1000);

		Assertions.assertEquals(0, credentialPage.getEditBtns().size());
		sleep(2000);
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
//	@Test
//	public void testRedirection() {
//		// Create a test account
//		doMockSignUp("Redirection","Test","RT","123");
//
//		// Check if we have been redirected to the log in page.
//		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
//	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
//	@Test
//	public void testBadUrl() {
//		// Create a test account
//		doMockSignUp("URL","Test","UT","123");
//		doLogIn("UT", "123");
//
//		// Try to access a random made-up URL.
//		driver.get("http://localhost:" + this.port + "/some-random-page");
//		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
//	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
//	@Test
//	public void testLargeUpload() {
//		// Create a test account
//		doMockSignUp("Large File","Test","LFT","123");
//		doLogIn("LFT", "123");
//
//		// Try to upload an arbitrary large file
//		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
//		String fileName = "upload5m.zip";
//
//		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
//		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
//		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());
//
//		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
//		uploadButton.click();
//		try {
//			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
//		} catch (org.openqa.selenium.TimeoutException e) {
//			System.out.println("Large File upload failed");
//		}
//		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
//
//	}



}
