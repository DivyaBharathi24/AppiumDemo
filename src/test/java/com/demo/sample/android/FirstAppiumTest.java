package com.demo.sample.android;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.sample.BasePage;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class FirstAppiumTest
        extends BasePage {
    private AppiumDriver driver;
    public enum  SwipeElementDirection{UP,DOWN,LEFT,RIGHT};
    
    @Test
    public void runMessagesTest() {
        try {
            driver.findElement(By.id("com.android.store.apps"))
                  .click();
            WebElement travelRail = driver.findElement(By.id("com.android.TravelandLocal"));
            WebElement categoriesTab = driver.findElement(By.id("com.android.Categories"));
            WebElement contentView = driver.findElement(By.xpath(""));
            WebElement indigoFlightApp = driver.findElement(By.xpath(""));
            this.elementSwipeWhileNotFound(contentView,categoriesTab, SwipeElementDirection.DOWN, true);
            WebElement games = driver.findElement(By.xpath(""));
            Assert.assertTrue(games.isSelected(),"Validating if Games section is selected by default");
            List<WebElement> webElements = driver.findElements(By.xpath(""));
            boolean isTravelFound = false;
            for (WebElement webElement : webElements) {
				if(webElement.getText().contains("Travel & Local")) {
					isTravelFound = true;
				}
			}
            Assert.assertEquals(isTravelFound, false, "Validating if Travel & Local is not present in the Games section");
            driver.findElement(By.id("com.android.store.apps")).click();
            this.pageSwipeWhileNotFound(travelRail, SwipeElementDirection.DOWN, true);
            Assert.assertTrue(this.waitForElementTobeVisible(indigoFlightApp),"Validating if Indigo flight app is present in Travel & Local section");
        } catch (Exception e) {
            System.out.println("Agree button not seen");
        }
        driver.findElement(AppiumBy.accessibilityId("Start chat"))
              .click();
        driver.findElement(AppiumBy.accessibilityId("Switch between entering text and numbers"))
              .click();
        driver.findElement(AppiumBy.accessibilityId("com.google.android.apps.messaging:id/recipient_text_view"))
              .sendKeys("anand");
        waitFor(5);
       
    }

    private void waitFor(int numberOfSeconds) {
        try {
            System.out.println("Sleep for " + numberOfSeconds);
            Thread.sleep(numberOfSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    
    
}
