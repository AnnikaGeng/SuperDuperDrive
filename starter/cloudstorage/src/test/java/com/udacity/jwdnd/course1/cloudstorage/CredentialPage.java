package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialPage {
    private WebDriver webDriver;

    @FindBy(id = "nav-credentials-tab")
    WebElement creTab;

    @FindBy(id = "add-credential")
    WebElement addCreBtn;

    @FindBy(id = "credential-url")
    WebElement crenUrl;

    @FindBy(id = "credential-username")
    WebElement crenUser;

    @FindBy(id = "credential-password")
    WebElement crenPass;

    @FindBy(id = "credentialSubmit")
    WebElement crenSubBtn;

    public CredentialPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getCrenTabId() { return "nav-credentials-tab"; }

    public String getAddCrenBtnId() {return "credentialSubmit";}

    public void clickCrenTab() { creTab.click(); }

    public void clickAddCrenBtn() { addCreBtn.click(); }

    public void inputCrenUrl(String url) {
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+url+"';",this.crenUrl);
    }

    public void inputCrenUsername(String username) {
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+username+"';",this.crenUser);
    }

    public void inputCrenPassword(String pass) {
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].value='"+pass+"';",this.crenPass);
    }

    public String getPasswordInModal() {
        return crenPass.getAttribute("value");
    }

    public void clickCrenSubmitBtn() {
        ((JavascriptExecutor)webDriver).executeScript("arguments[0].click();",this.crenSubBtn);
    }

    public String getUrl(int pos) {
        List<WebElement> urls = webDriver.findElements(By.id("credential-url-text"));
        return urls.get(pos).getAttribute("innerHTML");
    }

    public String getUsername(int pos) {
        List<WebElement> urls = webDriver.findElements(By.id("credential-username-text"));
        return urls.get(pos).getAttribute("innerHTML");
    }

    public String getPass(int pos) {
        List<WebElement> urls = webDriver.findElements(By.id("credential-password-text"));
        return urls.get(pos).getAttribute("innerHTML");
    }

    public void clickEditBtn(int pos) {
        List<WebElement> editBtns = webDriver.findElements(By.id("credential-edit-text"));
        editBtns.get(pos).click();
    }

    public void clickDeleteBtn(int pos) {
        List<WebElement> deleteBtns = webDriver.findElements(By.id("credential-delete-text"));
        deleteBtns.get(pos).click();
    }

    public List<WebElement> getEditBtns() {
        return webDriver.findElements(By.id("credential-edit-text"));
    }
}
