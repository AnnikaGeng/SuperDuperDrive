package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    WebElement userName;

    @FindBy(id="inputPassword")
    WebElement passWord;

    @FindBy(id = "login-button")
    WebElement LoginBtn;

    @FindBy(id = "signup-success-msg")
    WebElement successMsg;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void Login(String username, String password) {
        userName.clear();
        userName.sendKeys(username);
        passWord.clear();
        passWord.sendKeys(password);
        LoginBtn.click();
    }

    public boolean loginOkMsg() {
        return successMsg.isDisplayed();
    }
}
