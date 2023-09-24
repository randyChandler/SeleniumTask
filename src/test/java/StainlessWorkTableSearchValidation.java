import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StainlessWorkTableSearchValidation {

	public static void main(String[] args) {
		// Setup for Chrome, ChromeOptions, and Logging
		WebDriverManager.chromedriver().setup();
		Logger logger = Logger.getLogger("");
		Handler handler;
		try {
			handler = new FileHandler("selenium.xml");
			logger.addHandler(handler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

		driver.get("https://www.webstaurantstore.com/");
		assert driver.getCurrentUrl() == "https://www.webstaurantstore.com/";

		try {
			WebElement searchBar = driver.findElement(By.id("searchval"));
			searchBar.sendKeys("stainless work table");
			driver.findElement(By.cssSelector("#searchForm > div > button")).click();
			logger.info(driver.findElement(By.className("page-header search-title")).getText());
		} catch (Exception e) {
			logger.finer(e.getMessage());
		}

		// Get the array of elements returned (from the search as a whole), each item
		// title should contain "Table" <-- It is a title, so, the 'Table' should be capitalized correctly
		// Get total pagination
		WebElement totalPages = driver.findElement(By.id("paging"));
		String lastPage = totalPages.findElement(By.cssSelector("a[aria-label^='last page']")).getText();
		logger.info("There are " + lastPage + " pages of results");
		// Interesting fact: If you modify the url (i.e. page=10 or page=91) it will still work... why does it allow this? - and it does seem 91 is the true highest page.
		List<WebElement> productsReturned = null;
		for (int i = 1; i < Integer.parseInt(lastPage); i++) {
			WebElement pagination = driver.findElement(By.id("paging")); // Why here again? When we change pages, the element will be considered stale and we need a refresh
			logger.info("Getting products on page " + (i + 1));
			if (productsReturned != null) {
				productsReturned.clear();
			}
			productsReturned = driver.findElements(By.cssSelector("[data-testid = 'productBoxContainer']"));
			for (WebElement productElement : productsReturned) {
				String itemDescription = productElement.findElement(By.cssSelector("[data-testid = 'itemDescription']")).getText();
				assert itemDescription.contains("Table");
			}
			pagination.findElement(By.cssSelector("a[aria-label*='page " + (i + 1) + "']")).click();	
		}

		// add last item on the last page to the cart
		productsReturned = driver.findElements(By.cssSelector("[data-testid = 'productBoxContainer']")); // re-add everything because of stale element
		WebElement lastItem = productsReturned.get(productsReturned.size() - 1);
		logger.info("Adding to cart:  " + lastItem.findElement(By.cssSelector("input[data-testid='itemDescription']")));
		lastItem.findElement(By.cssSelector("input[data-testid='itemAddCart']")).click();
		
		
		
		
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
