package model;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyModel {
    private static Logger logger = LoggerFactory.getLogger(KeyModel.class);
    public String key, value;
    public By type;

    public KeyModel(String arr[]) {
        checkErrorState(arr);
        key = arr[0];
        value = arr[2];
        type = getType(Integer.parseInt(arr[1]));
    }

    private By getType(int type) {
        switch(type) {
            case 0: return By.id(value);
            case 1: return By.cssSelector(value);
            case 2: return By.xpath(value);
            default:
                logger.error("Invalid locator type were given !");
        }
        throw new IllegalArgumentException("Invalid locator type !");
    }


    private void checkErrorState(String arr[]) {
        if(arr.length != 3) {
            logger.error("Invalid argument/arguments were given !");
            logger.error("Arguments: [" + Arrays.stream(arr).collect(Collectors.joining(", ")) + "]");
            throw new IllegalStateException(String.format("Expected argument count: 3, given: %d", arr.length));
        }
    }
}
