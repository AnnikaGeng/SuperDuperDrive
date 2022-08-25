package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class NotePage {

    private WebDriver webDriver;
    @FindBy(id = "nav-notes-tab")
    WebElement noteTab;

    @FindBy(id = "add-note-button")
    WebElement addNoteBtn;

    @FindBy(id = "note-title")
    WebElement noteTitle;

    @FindBy(id = "note-description")
    WebElement noteDesc;

    @FindBy(id = "noteSubmit")
    WebElement noteSubBtn;

    @FindBy(id = "note-title-display")
    WebElement noteTitleDis;

    @FindBy(id = "note-description-display")
    WebElement noteDescDisp;

    @FindBy(id = "note-edit-btn")
    WebElement noteEdit;

    @FindBy(id = "note-delete-btn")
    WebElement noteDelete;

    public NotePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void clickNoteTab() { noteTab.click();}

    public void clickAddNoteBtn() { addNoteBtn.click();}

    public void addNote(String title, String desc) {
        //noteTitle.sendKeys(title);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
        //noteDesc.sendKeys(desc);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + desc + "';", this.noteDesc);
        //noteSubBtn.click();
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click()", this.noteSubBtn);
    }

    public String getNoteTitleDisplay() {
        return noteTitleDis.getAttribute("innerHTML");
    }

    public String getNoteDescDisplay() {
        return noteDescDisp.getAttribute("innerHTML");
    }

    public void clickNoteEditBtn() {
        noteEdit.click();
    }

    public void clickNoteDeleteBtn() {
        noteDelete.click();
    }

    public List<WebElement> getNoteEditBtns() {
        return webDriver.findElements(By.id("note-edit-btn"));
    }

    public void changeNoteTitle(String title) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
    }

    public void changeNoteDesc(String desc) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + desc + "';", this.noteDesc);
    }

    public void clickSubmitBtn() {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click()", this.noteSubBtn);
    }

}
