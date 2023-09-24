import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StainlessWorkTableSearchValidation {
	
	public static void main(String[] args) {
		
		WebDriverManager.chromedriver().setup();
        Logger logger = Logger.getLogger("");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		
		driver.get("https://www.webstaurantstore.com/");
		assert driver.getCurrentUrl() == "https://www.webstaurantstore.com/";
		
		try {
			WebElement searchBar = driver.findElement(By.id("searchval"));
			searchBar.sendKeys("stainless work table");
			driver.findElement(By.cssSelector("#searchForm > div > button")).click();
			logger.info(driver.findElement(By.cssSelector("#react_0HMTQIVE2QAP7 > div > div.search__wrap > h1")).getText());
		} catch (Exception e) {
			logger.finer(e.getMessage());
		}
		
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		driver.close();
		driver.quit();
		
	}
}
