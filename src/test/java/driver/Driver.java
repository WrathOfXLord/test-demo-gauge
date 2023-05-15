package driver;

import com.thoughtworks.gauge.AfterSuite;
import com.thoughtworks.gauge.BeforeSuite;

import model.KeyModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Driver {

    // Holds the WebDriver instance
    protected static WebDriver webDriver;
    protected static DesiredCapabilities desiredCapabilities;
    protected static Logger logger;
    protected static FluentWait<WebDriver> wait;
    protected static HashMap<String, KeyModel> keyMap;
    protected final static boolean header = true;

    // Initialize a webDriver instance of required browser
    // Since this does not have a significance in the application's business domain, the BeforeSuite hook is used to instantiate the webDriver
    @BeforeSuite
    public static void initializeDriver(){
        webDriver = DriverFactory.getDriver();
        logger = LoggerFactory.getLogger(Driver.class);
        initWait();
        boolean locatorsLoaded = mapCsv("./src/test/resources/csv/keys.csv", ",", header);
        if(!locatorsLoaded) {
            throw new IllegalStateException("Locators have not been loaded! Exiting...");
        }
        logger.info("Driver and logger have been initialized.");
    }

    private static void initWait() {
        wait = new FluentWait<WebDriver>(webDriver)
                        .withTimeout(Duration.ofSeconds(60))
                        .pollingEvery(Duration.ofSeconds(1))
                        .ignoring(NoSuchElementException.class);
    }

    private static boolean mapCsv(String file, String delimiter, boolean header) {
        Scanner reader;
        try {
            reader = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            logger.error("Source file is not found!");
            return false;
        }
        String line;
        keyMap = new HashMap<String, KeyModel>();

        // for now only for human reading only
        if(header) {
            reader.nextLine();
        }
        while(reader.hasNextLine()) {
            line = reader.nextLine().trim();
            if(line.equalsIgnoreCase("")) {
                continue;
            }
            String values[] = Arrays.stream(line.split(delimiter)).map(e -> e.trim()).toArray(String[]::new);
            keyMap.put(values[0], new KeyModel(values));
        }
        reader.close();
        logger.info("Locators have been loaded.");
        return true;
    }

    protected KeyModel getElementByKey(String key) {
        return keyMap.get(key);
    }

    // Close the webDriver instance
    @AfterSuite
    public static void closeDriver(){
        webDriver.quit();
    }

}
