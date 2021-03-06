package Utilities;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;

/**
 * This class manages the driver, has all the actions and screenshots methods
 */
public class DriverManager {
	/** Create a boolean for later use */
	boolean confirm = false;
	public static WebDriver driver;
	public WebElement element;
	public Select select;

	public DriverManager() {
		super();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {

		this.driver = driver;
	}

	/** this method selects the locator and returns the respective web element */
	public WebElement elementCreator(Step Obj) {
		switch (Obj.getLocator().toLowerCase()) {
		case "name":
			element = driver.findElement(By.name(Obj.getValueLocator()));
			break;
		case "classname":
			element = driver.findElement(By.className(Obj.getValueLocator()));
			break;
		case "cssselector":
			element = driver.findElement(By.cssSelector(Obj.getValueLocator()));
			break;
		case "id":
			element = driver.findElement(By.id(Obj.getValueLocator()));
			break;
		case "xpath":
			element = driver.findElement(By.xpath(Obj.getValueLocator()));
			break;
		case "tagName":
			element = driver.findElement(By.tagName(Obj.getValueLocator()));
			break;
			
		}

		return element;

	}

	/**
	 * This method selects the locator and returns the respective web element for
	 * the drag n drop action
	 */
	public WebElement elementCreatorDrag(Step Obj) {
		switch (Obj.getDestinationLocator().toLowerCase()) {
		case "name":
			element = driver.findElement(By.name(Obj.getDestinationLocatorValue()));
			break;
		case "classname":
			element = driver.findElement(By.className(Obj.getDestinationLocatorValue()));
			break;
		case "cssselector":
			element = driver.findElement(By.cssSelector(Obj.getDestinationLocatorValue()));
			break;
		case "id":
			element = driver.findElement(By.id(Obj.getDestinationLocatorValue()));
			break;
		case "xpath":
			element = driver.findElement(By.xpath(Obj.getDestinationLocatorValue()));
			break;
		case "tagName":
			element = driver.findElement(By.tagName(Obj.getDestinationLocatorValue()));
			break;
			
		}

		return element;

	}

	/** This method Takes a screenshot */
	public static void takeSnapShot(Step Obj) throws Exception {
		if (Obj.getScreenshoot() == true) {
			/** Convert web driver object to TakeScreenshot */

			TakesScreenshot scrShot = ((TakesScreenshot) driver);

			/** Call getScreenshotAs method to create image file */

			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			/** Move image file to new destination */

			File DestFile = new File(
					"Screenshots/" + Obj.getTcName() + "/Step " + Obj.getStep() + " " + Obj.getAction() + ".png");
			FileUtils.copyFile(SrcFile, DestFile);
			System.out.print("Screenshot taken ");
		}
	}
	public void checkParameters(Step Obj) {
		Parameter parameterAux;
		ArrayList parameters=Obj.getParameters();
		try {
			for(int i=0;i<=parameters.size();i++) {
				parameterAux=(Parameter) parameters.get(i);
				if(Obj.getValueAction().equalsIgnoreCase(parameterAux.getName())) {
					Obj.setValueAction(parameterAux.getValue());
				}
			}
			}catch(Exception e) {
			System.out.println("bummer something went wrong executing parameters.");
			}
	}
	public void checkObjects(Step Obj) {
		Object objectAux;
		ArrayList objects=Obj.getObjects();
		try {
			for(int i=0;i<=objects.size();i++) {
				objectAux=(Object)objects.get(i);
				if(Obj.getLocator().equalsIgnoreCase(objectAux.getName())) {
					Obj.setLocator(objectAux.getLocator());
					Obj.setvLocator(objectAux.getValueLocator());
				}else if(Obj.getDestinationLocator().equalsIgnoreCase(objectAux.getName())) {
					Obj.setDestinationLocator(objectAux.getLocator());
					Obj.setDestinationLocatorValue(objectAux.getValueLocator());
				}
			}
			}catch(Exception e) {
			System.out.println("ups we are not using objects.");
			}
	}
	public void checkActions(Step Obj) {
		ArrayList<ArrayList> tclist = null;
		ReadXmls reader = new ReadXmls();
		Execute executer = new Execute();
		Step stepAux= new Step();
		try {
			tclist = reader.getTestCases("excel/ActionScripts.xlsx");
			for(int i=0;i<=tclist.size();i++) {
				stepAux=(Step) tclist.get(i).get(2);
				if(stepAux.getTcName().equalsIgnoreCase(Obj.getValueAction())) {
					try {
					executer.executetc(tclist.get(i));
					} catch (Exception e) {
						System.out.println("Error reading scipts");
						e.printStackTrace();
					}
			}
		}
		} catch (IOException e) {
			System.out.println("Error reading scipts");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method reads the action from the object step, and call the methods
	 * elementCreator to create web elements and execute them
	 */
	public void executeStep(Step Obj) throws Exception {
		checkParameters(Obj);
		checkObjects(Obj);
		
		WiniumManager winium = new WiniumManager();
		/** Create an object to read the local time */
		LocalDateTime locaTime = LocalDateTime.now();
		/** Create an object to format the time */
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		/** The time is saved in a local string */
		String formattedDate = locaTime.format(myFormatObj);
		/** The time is set in the time variant of the object */
		Obj.setTime(formattedDate);
		/** A auxiliary Web element is created for later use */
		WebElement auxElement = elementCreator(Obj);
		/** an integer is created for later use */
		/** Create an action for later use */
		Actions action = new Actions(driver);
		/** Create a random object for later use */
		Random random = new Random();
		int rand = 0;
		/** Here we read the action */
		switch (Obj.getAction().toLowerCase()) {
		/** Here we get a destination to navigate the driver whit */
		case "navigate":
			driver.navigate().to(Obj.getValueAction());
			break;
		/** The action quit quits the driver */
		case "quit":
			driver.quit();
			break;
		/** the action type sends keys to a web element */
		case "type":
			auxElement.sendKeys(Obj.getValueAction());
			break;			
		/** in this action the element makes a click */
		case "click":
			auxElement.click();
			break;
		/** this action cleans a field */
		case "clear":
			auxElement.clear();
			break;
		/** this action types an enter in a field */
		case "enter":
			auxElement.sendKeys(Keys.ENTER);
			break;
		/** this action selects an element by value */
		case "selectByValue":
			select = new Select(elementCreator(Obj));
			select.selectByValue(Obj.getValueAction());
			break;
		/** this action selects an element by index */
		case "selectByIndex":
			select = new Select(elementCreator(Obj));
			select.selectByIndex(Integer.parseInt(Obj.getValueAction()));
			break;
		/** this action closes an alert */
		case "alert":
			Thread.sleep(3000);
			driver.switchTo().alert().accept();
			break;
		/** this action confirms that a element exists or not */
		case "confirm":
			confirm = auxElement.isDisplayed();
			if (confirm == true) {
				System.out.println("The element exists");
			} else {
				System.out.println("The element does not exists");
				Obj.setPass(false);
			}
			break;
		/** this action compares the text of an element with the value action */
		case "compto":
			System.out.println("comparing...");
			if (Obj.valueAction.equals(auxElement.getText())) {
				confirm = true;
			} else {
				confirm = false;
			}
			if (confirm == true) {
				System.out.println("The element contains " + Obj.getValueAction());
			} else {
				System.out.println("The element does not contain " + Obj.getValueAction());
				Obj.setPass(false);
			}
			break;
		/** this action creates a random user */
		case "randuser":
			rand = random.nextInt(10000);
			System.out.println(rand);
			auxElement.sendKeys("Tunas" + rand);
			break;
		/** this action creates a random email */
		case "randemail":
			rand = random.nextInt(10000);
			auxElement.sendKeys("Tunas" + rand + "@gmail.com");
			break;
		/**
		 * this action creates a dragndrop element an moves one element to a destination
		 */
		case "dragndrop":
			WebElement to = elementCreatorDrag(Obj);
			Thread.sleep(2000);
		    action.dragAndDrop(auxElement, to).release().build().perform();
		    Robot robot = new Robot();
		    robot.keyPress(KeyEvent.VK_ESCAPE);
		    robot.keyRelease(KeyEvent.VK_ESCAPE);
			Thread.sleep(2000);
			break;
		/** this action opens the start date calendar */
		case "calendar":
			List<WebElement> allsvg = driver.findElements(By.cssSelector("svg"));
			for (WebElement ele : allsvg) {
				ele.click();
			}
			break;
		/** this action scrolls down the application */
		case "scroll":
			JavascriptExecutor js2 = (JavascriptExecutor) driver;
			js2.executeScript("window.scrollBy(0,1000)");
			break;
		/** action to refresh the web page */
		case "refresh1":
			driver.navigate().refresh();
			break;
		/** this action picks a photo */
		case "photopicker":
			winium.winiumManage(driver);
			break;
		case "scriptexe":
			try {
			System.out.println("executing script");
			checkActions(Obj);
			}catch(Exception e) {
				
			}
			break;
			
		}
		
		takeSnapShot(Obj);
	}

}
