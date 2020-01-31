package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import workWithFile.FileWork;

public class JenkinsPage extends AbstractPage{

    @FindBy(xpath = "//input[@name='j_username']")
    private WebElement loginField;

    @FindBy(xpath = "//input[@name='j_password']")
    private WebElement passwordField;

    @FindBy(id = "yui-gen1-button")
    private WebElement loginButton;

    public void makeAuthorisation(String login, String password) {
        driver.navigate().to(FileWork.readProperty("jenkins.page","config.properties"));
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
