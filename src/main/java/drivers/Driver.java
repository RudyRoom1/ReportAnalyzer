package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Driver {

    private static WebDriver driver;
    private static final String driverName = "chrome";

    public static WebDriver getDriver() {
        if (driver == null) {
            switch (driverName) {
                case "chrome":
                    WebDriverManager.chromedriver().version("79.0.3945.36").setup();
                    driver = new ChromeDriver();
                    break;
                case "ghost":
                    WebDriverManager.phantomjs().setup();
                    driver = new PhantomJSDriver();
            }
        }
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        driver.manage().window().maximize();
        return driver;
    }

    public static void exitDriver() {
        driver.quit();
        driver = null;
    }
}
