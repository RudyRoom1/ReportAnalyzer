package pages;

import drivers.Driver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class AbstractPage {
    public static WebDriver driver;

    AbstractPage(){
        this.driver = Driver.getDriver();
        PageFactory.initElements(driver,this);
    }
}
