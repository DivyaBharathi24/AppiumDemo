package com.demo.sample;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.demo.sample.android.FirstAppiumTest.SwipeElementDirection;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class BasePage {
    private static AppiumDriverLocalService localAppiumServer;
    private AppiumDriver driver;

    
    @BeforeSuite
    public void beforeAll() {
        System.out.println(String.format("Start local Appium server"));
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        serviceBuilder.usingAnyFreePort();
        localAppiumServer = AppiumDriverLocalService.buildService(serviceBuilder);

        localAppiumServer.start();
        System.out.println(String.format("Appium server started on url: '%s'",
                                         localAppiumServer.getUrl()
                                                          .toString()));
    }

    @AfterSuite
    public void afterSuite() {
        if (null != localAppiumServer) {
            System.out.println(String.format("Stopping the local Appium server running on: '%s'",
                                             localAppiumServer.getUrl()
                                                              .toString()));
            localAppiumServer.stop();
            System.out.println("Is Appium server running? " + localAppiumServer.isRunning());
        }
    }
    
    @BeforeMethod
    private AppiumDriver createAppiumDriver(URL appiumServerUrl, String udid) {

        UiAutomator2Options capabilities = new UiAutomator2Options();

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Samsung M42");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.google.android.apps.messaging");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.google.android.apps.messaging.ui.ConversationListActivity");
        capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
        capabilities.setCapability(MobileCapabilityType.UDID, udid);

        AppiumDriver appiumDriver = new AppiumDriver(appiumServerUrl, capabilities);
        return appiumDriver;
    }
 
    @AfterMethod
    private void closeDrivers () {
    	 if (null != driver) {
             System.out.println("Close the driver");
             driver.quit();
         }
    }
   

    public URL getAppiumServerUrl() {
        System.out.println("Appium server url: " + localAppiumServer.getUrl());
        return localAppiumServer.getUrl();
    }
    
    
	public boolean pageSwipeWhileNotFound(WebElement element, SwipeElementDirection direction, boolean click) {
		int maxCount = 10;
		int count = 0;
		boolean isfound = false;
		try {
			while (!(isfound = this.waitForElementTobeVisible(element)) && count < maxCount) {
				count++;
				this.swipe(direction);
			}
			if (isfound) {
				if (click) {
					element.click();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean elementSwipeWhileNotFound(WebElement contentView, WebElement element, SwipeElementDirection direction, boolean click){
		int count = 0;
		int maxCount = 20;
		boolean isfound = false;
		try {
			if (!this.waitForElementTobeVisible(contentView)) {
				System.out.println("\tContentView was not found.");
				return false;
			}
			while (!(isfound = this.waitForElementTobeVisible(element)) && count < maxCount) {
				count++;
				this.swipeElement(contentView, direction);
			}
			if (isfound) {
				if (click) {
					element.click();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int getHeightOfScreen() {
		System.out.println("Getting height for the screen ");
		try {
			return driver.manage().window().getSize().getHeight();
			} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getWidthOfScreen() {
		System.out.println("Getting width for the screen ");
		try {
			return driver.manage().window().getSize().getWidth();
			} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void swipe(SwipeElementDirection direction){
		try {
				int screenHeight = this.getHeightOfScreen();
				int screenWidth = this.getWidthOfScreen();
				int startX = 0;
				int startY = 0;
				int endX = 0;
				int endY = 0;
				switch (direction) {
				case DOWN:
					startY = (int)(screenHeight * .60);
					startX = (int)(screenWidth * .5);
					endY = (int)(screenHeight * .1);
					endX = (int)(screenWidth * .5);
					break;
				case UP:
					startY = (int)(screenHeight * .30);
					startX = (int)(screenWidth * .5);
					endY = (int)(screenHeight * .8);
					endX = (int)(screenWidth * .5);
					break;
				case LEFT:
					startY = (int)(screenHeight * .5);
					startX = (int)(screenWidth * .8);
					endY = (int)(screenHeight * .5);
					endX = (int)(screenWidth * .2);
					break;
				case RIGHT:
					startY = (int)(screenHeight * .5);
					startX = (int)(screenWidth * .2);
					endY = (int)(screenHeight * .5);
					endX = (int)(screenWidth * .8);
					break;
				default:
					break;
				}
				
				PointOption pointOption=new PointOption<>();
				WaitOptions waitOptions=new WaitOptions();
				TouchAction perform = new TouchAction((PerformsTouchActions) driver)
                .press(pointOption.withCoordinates(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(3)))
                .moveTo(pointOption.withCoordinates(endX, endY)).release().perform();
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void swipeElement(WebElement element,SwipeElementDirection direction){
		try {
			int screenHeight =  element.getSize().getHeight();
			int screenWidth = element.getSize().getWidth();
				int startX = 0;
				int startY = 0;
				int endX = 0;
				int endY = 0;
				switch (direction) {
				case DOWN:
					startY = (int)(screenHeight * .60);
					startX = (int)(screenWidth * .5);
					endY = (int)(screenHeight * .1);
					endX = (int)(screenWidth * .5);
					break;
				case UP:
					startY = (int)(screenHeight * .30);
					startX = (int)(screenWidth * .5);
					endY = (int)(screenHeight * .8);
					endX = (int)(screenWidth * .5);
					break;
				case LEFT:
					startY = (int)(screenHeight * .5);
					startX = (int)(screenWidth * .8);
					endY = (int)(screenHeight * .5);
					endX = (int)(screenWidth * .2);
					break;
				case RIGHT:
					startY = (int)(screenHeight * .5);
					startX = (int)(screenWidth * .2);
					endY = (int)(screenHeight * .5);
					endX = (int)(screenWidth * .8);
					break;
				default:
					break;
				}
				
				ElementOption elementOption=new ElementOption();
				WaitOptions waitOptions=new WaitOptions();
				TouchAction perform = new TouchAction((PerformsTouchActions) driver)
                .press(elementOption.element(element).withCoordinates(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(3)))
                .moveTo(elementOption.element(element).withCoordinates(endX, endY)).release().perform();
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean waitForElementTobeVisible(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			wait.until(ExpectedConditions.visibilityOfElementLocated((By) element));
			System.out.println("Element Found");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
}
