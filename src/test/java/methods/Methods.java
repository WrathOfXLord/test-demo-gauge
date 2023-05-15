package methods;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.thoughtworks.gauge.Step;

import driver.Driver;
import model.KeyModel;
import static org.assertj.core.api.Assertions.fail;

public class Methods extends Driver {
    public WebElement findElement(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    @Step("Wait for <key> element then find")
    public WebElement findElement(String key) {
        KeyModel keyModel = getElementByKey(key);
        WebElement element = findElement(keyModel.type);
        logger.info(keyModel.key + " - " + keyModel.value + ": element has been found.");
        return element;
    }

    @Step("Wait for <key> element and click") 
    public void click(String key) {
        findElement(key);
        logger.info("Clicked to " + key + " element.");
    }


    @Step({"V1: Verify <key> element or its childs contain <text> text",
        "V1: <key> elementi veya child elementlerinin <text> metnini icerdigini dogrula"})
    public void checkIfElementContainsTextDouble(String key, String text) {
        try {
            //case sensitive
            String xpath = "//*[contains(text(), \"" + text + "\")]";
            //bulursa info verecek bulamazsa fail atacak
            findElement(key).findElement(By.xpath(xpath));
            logger.info(key + " elementi '" + text + "' textini iceriyor.");
        } catch (NoSuchElementException e) {
            fail(key + " elementi '" + text + "' textini icermiyor !");
        }
    }

    @Step({"V2: Verify <key> element or its childs contain <text> text",
        "V2: <key> elementi veya child elementlerinin <text> metnini icerdigini dogrula"})
    public void checkIfElementContainsTextSingle(String key, String text) {
        try {
            //case sensitive
            String xpath = "//*[contains(text(), '" + text + "')]";
            //bulursa info verecek bulamazsa fail atacak
            findElement(key).findElement(By.xpath(xpath));
            logger.info(key + " elementi '" + text + "' textini iceriyor.");
        } catch (NoSuchElementException e) {
            fail(key + " elementi '" + text + "' textini icermiyor !");
        }
    }

    @Step({"Wait for <duration> milliseconds", "<duration> milisaniye bekle"})
    public void waitForMilliSeconds(long duration) {
        try {
            logger.info("Waiting for " + duration + " milliseconds.");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            //ignore
        }
    }

    @Step({"Wait for <duration> seconds", "<duration> saniye bekle"})
    public void waitForSeconds(long duration) {
        try {
            logger.info("Waiting for " + duration + " seconds.");
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}
