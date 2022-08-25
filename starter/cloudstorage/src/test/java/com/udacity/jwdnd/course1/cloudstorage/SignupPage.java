package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id="inputFirstName")
    WebElement firstName;

    @FindBy(id="inputLastName")
    WebElement lastName;

    @FindBy(id="inputUsername")
    WebElement userName;

    @FindBy(id="inputPassword")
    WebElement passWord;

    @FindBy(id="buttonSignUp")
    WebElement signupButton;

    @FindBy(id="success-msg")
    WebElement signupOkMsg;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void signup(String fname, String lname, String username, String password) {
        firstName.clear();
        firstName.sendKeys(fname);
        lastName.clear();
        lastName.sendKeys(lname);
        userName.clear();
        userName.sendKeys(username);
        passWord.clear();
        passWord.sendKeys(password);
        signupButton.click();
    }

    public boolean signUpMsgDisplay() {
        return signupOkMsg.isDisplayed();
    }
}














