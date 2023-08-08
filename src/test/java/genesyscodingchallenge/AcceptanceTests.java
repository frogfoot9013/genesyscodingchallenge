package genesyscodingchallenge;

import static org.junit.jupiter.api.Assertions.*;


import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.TimeoutException;


class AcceptanceTests {

	@Test
	public void AcceptanceTesting() throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
		driver.manage().window().setSize(new Dimension(1280, 900));
		driver.get("https://www.ryanair.com");
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				  .withTimeout(Duration.ofSeconds(6))
				  .pollingEvery(Duration.ofSeconds(1))
				  .ignoring(NoSuchElementException.class);

		// get title of webpage, verify page has loaded by presence of this title
		String title = driver.getTitle();
		assertEquals("Official Ryanair website | Cheap flights from Ireland | Ryanair", title);
		System.out.println("Main page loaded.");
		
		// click through GDPR prompt
		WebElement gdprButton = driver.findElement(By.className("cookie-popup-with-overlay__button"));
		gdprButton.click();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
		
		// check that trip is a return trip
		WebElement returnTripType = driver.findElement(By.cssSelector("[aria-label='Return trip']"));
		assertTrue(returnTripType != null);
		WebElement returnIcon = returnTripType.findElement(By.tagName("icon"));
		String returnIconClass = returnIcon.getAttribute("class");
		if (returnIconClass != "trip-type__icon trip-type__icon--selected") {
			returnTripType.click();
		}
		returnIcon = returnTripType.findElement(By.tagName("icon"));
		returnIconClass = returnIcon.getAttribute("class");
		assertEquals(returnIconClass, "trip-type__icon trip-type__icon--selected");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		// select input departure
		WebElement departureInput = driver.findElement(By.id("input-button__departure"));
		departureInput.click();
		departureInput.clear(); // assume default departure is incorrect and clear
		// input departure point
		departureInput.sendKeys("Dublin");
		String departureText = departureInput.getAttribute("value");
		assertEquals(departureText, "Dublin");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		// select input destination
		WebElement destinationInput = driver.findElement(By.id("input-button__destination"));
		destinationInput.click();
		destinationInput.sendKeys("Barcelona");
		String destinationText = destinationInput.getAttribute("value");
		assertEquals(destinationText, "Barcelona");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		// option to select specific airport
		
		WebElement fswDestinationContainer = wait.until(WebDriver -> {
			return driver.findElement(By.tagName("fsw-destination-container"));
		});
		assertTrue(fswDestinationContainer != null);
		WebElement destinationAirport = driver.findElement(By.cssSelector("[data-id='BCN']"));
		assertTrue(destinationAirport != null);
		destinationAirport.click();

		
		// select departure date
		WebElement departureDateElement = wait.until(WebDriver -> {
				return driver.findElement(By.cssSelector("[data-ref='input-button__dates-from']"));
		});
		assertTrue(departureDateElement != null);
		
		departureDateElement.click();

		WebElement datePickerDeparture = wait.until(WebDriver -> {
				return driver.findElement(By.cssSelector("[data-ref='fsw-datepicker-container__from']"));
		});
		assertTrue(datePickerDeparture != null);

		
		WebElement monthToggleDeparture = wait.until(WebDriver -> {
				return driver.findElement(By.className("m-toggle__scrollable-items"));
		});
		assertTrue(monthToggleDeparture != null);
		WebElement monthDeparture = wait.until(WebDriver ->{
				return driver.findElement(By.xpath("//div[@class='m-toggle__month' and @data-id='Nov']" ));
		});
		assertTrue(monthDeparture != null);
		monthDeparture.click();
		monthDeparture.click();
		
		WebElement calendarDeparture = driver.findElement(By.cssSelector(".datepicker__calendar.datepicker__calendar--left"));
		WebElement monthVerify = calendarDeparture.findElement(By.tagName("div"));
		String monthText = monthVerify.getText();
		assertEquals(monthText, "November 2023");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		
		
		WebElement calendarDepartureBody = calendarDeparture.findElement(By.tagName("calendar-body"));
		assertTrue(calendarDepartureBody != null);
		// first date, 5 November
		WebElement calendarDepartureDate = calendarDepartureBody.findElement(By.cssSelector("[data-id='2023-11-05']"));
		String departureDateVal = calendarDepartureDate.getText();
		assertEquals(departureDateVal, "5");
		calendarDepartureDate.click();
		
		WebElement departureDateDesc = departureDateElement.findElement(By.cssSelector("[data-ref='input-button__display-value']"));
		String departureDateDescText = departureDateDesc.getText();
		assertEquals(departureDateDescText, "Sun, 5 Nov");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		
		// select return date
		WebElement returnDateElement = driver.findElement(By.cssSelector("[data-ref='input-button__dates-to']"));
		assertTrue(returnDateElement != null);
		returnDateElement.click();
		returnDateElement.click();

		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		
		WebElement datePickerReturn = driver.findElement(By.cssSelector("[data-ref='fsw-datepicker-container__to']"));
		assertTrue(datePickerReturn != null);
		
		WebElement calendarReturn = driver.findElement(By.cssSelector(".datepicker__calendar.ng-star-inserted"));
		assertTrue(calendarReturn != null);
		WebElement monthVerifyReturn = calendarReturn.findElement(By.tagName("div"));
		String monthTextReturn = monthVerifyReturn.getText();
		assertEquals(monthTextReturn, "December 2023");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		WebElement calendarReturnBody = calendarReturn.findElement(By.tagName("calendar-body"));
		assertTrue(calendarReturnBody != null);
		
		WebElement calendarReturnDate = calendarReturnBody.findElement(By.cssSelector("[data-id='2023-12-07']"));
		String returnDateVal = calendarReturnDate.getText();
		assertEquals(returnDateVal, "7");
		calendarReturnDate.click();
		
		WebElement returnDateDesc = returnDateElement.findElement(By.cssSelector("[data-ref='input-button__display-value']"));
		String returnDateDescText = returnDateDesc.getText();
		assertEquals(returnDateDescText, "Thu, 7 Dec");
		
		
		// select passenger numbers
		WebElement passengersButton = driver.findElement(By.cssSelector("[data-ref='input-button__passengers']"));
		assertTrue(passengersButton != null);
		passengersButton.click();
		passengersButton.click();
		WebElement passengersPickerAdults = driver.findElement(By.cssSelector("[data-ref='passengers-picker__adults']"));
		assertTrue(passengersPickerAdults != null);
		WebElement passengersSelector = passengersPickerAdults.findElement(By.className("counter"));
		passengersSelector = passengersSelector.findElement(By.cssSelector("[data-ref='counter.counter__increment']"));
		assertTrue(passengersSelector != null);
		passengersSelector.click();
		WebElement passengerAmount = passengersButton.findElement(By.cssSelector("[data-ref='input-button__display-value']"));
		String amtText = passengerAmount.getText();
		assertEquals(amtText, "2 Adults");
				
		// check that search button exists
		WebElement searchButton = driver.findElement(By.cssSelector(".flight-search-widget__start-search.ng-tns-c2080360900-3.ry-button--gradient-yellow"));
		assertTrue(searchButton != null);
		searchButton.click();
		System.out.println("Searching for flights.");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

		WebElement flightDetailsContainer = driver.findElement(By.tagName("flights-trip-details-container"));
		assertTrue(flightDetailsContainer != null);
		
		// select outbound flights
		WebElement outboundFlights = driver.findElement(By.xpath("//journey-container[@data-ref='outbound']"));
		assertTrue(outboundFlights != null);
		
		WebElement flightCardOut = outboundFlights.findElement(By.tagName("flight-card-new"));
		assertTrue(flightCardOut != null);
		
		// select button
		WebElement selectButtonOut = flightCardOut.findElement(By.className("flight-card-summary__select-btn"));
		String selectButtonOutText = selectButtonOut.getText();
		assertEquals(selectButtonOutText, "Select");
		selectButtonOut.click();
		System.out.println("Outbound flight selected.");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		
		
		// select inbound flight
		WebElement inboundFlights = driver.findElement(By.xpath("//journey-container[@data-ref='inbound']"));
		assertTrue(inboundFlights != null);
		
		WebElement flightCardIn = inboundFlights.findElement(By.tagName("flight-card-new"));
		assertTrue(flightCardIn != null);
		
		// select button
		WebElement selectButtonIn = flightCardIn.findElement(By.className("flight-card-summary__select-btn"));
		String selectButtonInText = selectButtonIn.getText();
		assertEquals(selectButtonInText, "Select");
		selectButtonIn.click();
		System.out.println("Inbound Flight selected");

		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		
		// fare selector container - OLD - Ryanair seems to have changed this layout as of 2nd August
		try {
			WebDriverWait waitNew = new WebDriverWait(driver, Duration.ofMillis(5000));
			waitNew.until(ExpectedConditions.presenceOfElementLocated(By.tagName("fare-table-new-layout-container")));
			WebElement fareSelectorContainerNew = driver.findElement(By.tagName("fare-table-new-layout-container"));
			assertTrue(fareSelectorContainerNew != null);
	
			WebElement regularFareButton = driver.findElement(By.cssSelector("[data-e2e='fare-card-regular']"));
			assertTrue(regularFareButton != null);
			String btntxt = regularFareButton.getText();
			System.out.println(btntxt);
			
			regularFareButton.click();
		}
		catch (TimeoutException e) {
			try {
				WebDriverWait waitOld = new WebDriverWait(driver, Duration.ofMillis(5000));
				waitOld.until(ExpectedConditions.presenceOfElementLocated(By.tagName("fare-selector-container")));
				WebElement fareSelectorContainer = driver.findElement(By.tagName("fare-selector-container"));
				assertTrue(fareSelectorContainer != null);
				
				WebElement fareSelectorSpinner = driver.findElement(By.cssSelector(".fare-table-spinner.grid.grid-cols-4.ry-spinner--large.ry-spinner--cover"));
				assertTrue(fareSelectorSpinner != null);
				
				WebElement regularFare = fareSelectorSpinner.findElement(By.xpath("//fare-card//div[@data-e2e='fare-card--regular']"));
				assertTrue(regularFare != null);
				
				WebElement regularFareButton = regularFare.findElement(By.tagName("button"));
				assertTrue(regularFareButton != null);
				
				WebDriverWait waitFare = new WebDriverWait(driver, Duration.ofMillis(9000));
				waitFare.until(ExpectedConditions.elementToBeClickable(regularFareButton));
				
				regularFareButton.click();
				}
			catch( TimeoutException f) {
				
			}
		}

		System.out.println("Fare selected");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		
		
		// login panel
		WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		loginWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("ry-login-touchpoint")));
		WebElement loginPanel = driver.findElement(By.tagName("ry-login-touchpoint"));
		assertTrue(loginPanel != null);
		String loginPanelClass = loginPanel.getAttribute("class");
		assertEquals(loginPanelClass, "expanded");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		System.out.println("Login panel selected");


		
		// check that passengers panel is greyed out
		WebElement passengerPanel = loginPanel.findElement(By.xpath("..//..//div[contains(@class, 'form-outer-wrapper') and contains(@class, 'ng-star-inserted')]//div"));
		String passengerPanelClass = passengerPanel.getAttribute("class");
		assertEquals(passengerPanelClass, "form-wrapper form-wrapper--disabled");
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		System.out.println("Passengers panel is greyed out");

		
		// log in later, passengers panel should no longer be greyed out
		WebDriverWait loginLaterWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		loginLaterWait.until(ExpectedConditions.presenceOfElementLocated(By.className("login-touchpoint__login-later")));
		WebElement loginLaterButton = driver.findElement(By.className("login-touchpoint__login-later"));
		assertTrue(loginPanel != null);
		String loginLaterButtonText = loginLaterButton.getText();
		assertEquals(loginLaterButtonText, "Log in later");
		loginLaterButton.click();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		String passengerPanelClassUpdated = passengerPanel.getAttribute("class");
		assertEquals(passengerPanelClassUpdated, "form-wrapper");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		// enter passenger details
		List<WebElement> passengerContainers = driver.findElements(By.tagName("pax-passenger-container"));
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		int containerNum = passengerContainers.size();
		assertEquals(containerNum, 2);
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		
		// passenger 1
		WebElement p1 = passengerContainers.get(0);
		passengerDetails(p1, 0, "Jane", "Smith", driver);
		
		// passenger 2
		WebElement p2 = passengerContainers.get(1);
		passengerDetails(p2, 1, "Joe", "Smith", driver);
		p2.click();

		System.out.println("Passenger details entered");
		
		WebElement continueFlex = driver.findElement(By.className("continue-flow"));
		
		WebElement continueButton = continueFlex.findElement(By.tagName("button"));
		assertTrue(continueButton != null);
		continueButton.click();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));
		WebElement seatPreferenceTitle = driver.findElement(By.cssSelector(".seats-container__page-title.h3"));
		assertTrue(seatPreferenceTitle != null);
		
		WebElement seatMap = driver.findElement(By.tagName("seat-map"));
		assertTrue(seatMap != null);
		
		// Seats 
		WebElement seat1Depart = wait.until(WebDriver -> {
			return driver.findElement(By.id("seat-19B"));
		});
		assertTrue(seat1Depart != null);
		WebDriverWait waitSeat1Depart = new WebDriverWait(driver, Duration.ofMillis(9000));
		waitSeat1Depart.until(ExpectedConditions.elementToBeClickable(seat1Depart));
		seat1Depart.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));
		WebElement seat2Depart = wait.until(WebDriver -> {
			return driver.findElement(By.id("seat-19C"));
		});
		assertTrue(seat2Depart != null);
		WebDriverWait waitSeat2Depart = new WebDriverWait(driver, Duration.ofMillis(9000));
		waitSeat2Depart.until(ExpectedConditions.elementToBeClickable(seat2Depart));
		seat2Depart.click();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));
		WebElement carouselTable = driver.findElement(By.className("passenger-carousel__table"));
		assertTrue(carouselTable != null);
		
		List<WebElement> seats = driver.findElements(By.cssSelector(".seat__seat.body-l-lg.seat__seat--occupied"));
		assertTrue(seats != null);
		WebElement seat1 = seats.get(0).findElement(By.tagName("div"));
		String seat1Val = seat1.getText();
		assertEquals(seat1Val, "19B");
		
		WebElement seat2 = seats.get(1).findElement(By.tagName("div"));
		String seat2Val = seat2.getText();
		assertEquals(seat2Val, "19C");
		System.out.println("Outbound flight seats selected.");
		
		WebElement nextButton = driver.findElement(By.className("passenger-carousel__button-next"));
		assertTrue(nextButton != null);
		String oldText = nextButton.getText();
		nextButton.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		// return flight
		WebElement seatMapReturn = driver.findElement(By.tagName("seat-map"));
		assertTrue(seatMapReturn != null);
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		WebDriverWait waitButton = new WebDriverWait(driver, Duration.ofSeconds(10));
		waitButton.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.className("passenger-carousel__button-next"), oldText)));
		
		// seats
		WebElement seat1Return = wait.until(WebDriver -> { 
				return driver.findElement(By.id("seat-21B"));
		});
		assertTrue(seat1Return != null);
		WebDriverWait waitSeat1Return = new WebDriverWait(driver, Duration.ofMillis(9000));
		waitSeat1Return.until(ExpectedConditions.elementToBeClickable(seat1Return));
		seat1Return.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));
		WebElement seat2Return = wait.until(WebDriver -> {
			return driver.findElement(By.id("seat-21C"));
		});
		assertTrue(seat2Return != null);
		WebDriverWait waitSeat2Return = new WebDriverWait(driver, Duration.ofMillis(9000));
		waitSeat2Return.until(ExpectedConditions.elementToBeClickable(seat2Return));
		seat2Return.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));

		List<WebElement> seatsReturn = driver.findElements(By.cssSelector(".seat__seat.body-l-lg.seat__seat--occupied"));
		WebElement seat3 = seatsReturn.get(1).findElement(By.tagName("div"));
		String seat3Val = seat3.getText();
		assertEquals(seat3Val, "21B");
		
		WebElement seat4 = seatsReturn.get(3).findElement(By.tagName("div"));
		String seat4Val = seat4.getText();
		assertEquals(seat4Val, "21C");
		System.out.println("Inbound flight seats selected.");
		
		nextButton.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(9000));
		
		WebElement dismissFastTrack = driver.findElement(By.cssSelector("[data-ref='enhanced-takeover-beta-desktop__dismiss-cta']"));
		assertTrue(dismissFastTrack != null);
		dismissFastTrack.click();
		
		WebElement checkinBagsLoaded = driver.findElement(By.cssSelector("[data-ref='check-in-bags']"));
		assertTrue(checkinBagsLoaded != null);

		System.out.println("Check-in page loaded");
		
		driver.quit();

		
		
	}
	// inputEl is the WebElement, title is 0 for Mr, 1 for Mrs, 2 for Ms, forename and surname are strings 
	void passengerDetails(WebElement inputEl, int title, String forename, String surname, WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));
		WebDriverWait formWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		formWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("pax-details-form")));
		WebElement pForm = inputEl.findElement(By.tagName("pax-details-form"));
		String pTagType = pForm.getTagName();
		assertEquals(pTagType, "pax-details-form");
		
		// the dropdown first
		WebDriverWait buttonWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		buttonWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("button")));
		WebElement pDropdown = pForm.findElement(By.tagName("button"));
		assertTrue(pDropdown != null);
		pDropdown.click();		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(6000));

		//get items
		WebDriverWait itemsWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		itemsWait.until(ExpectedConditions.presenceOfElementLocated(By.className("dropdown__menu-items")));
		WebElement pDropdownItems = pForm.findElement(By.className("dropdown__menu-items"));
		assertTrue(pDropdownItems != null);
		WebDriverWait titleOptionsWait = new WebDriverWait(driver, Duration.ofMillis(9000));
		titleOptionsWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("ry-dropdown-item")));
		List<WebElement> pTitleOptions = pDropdownItems.findElements(By.tagName("ry-dropdown-item"));
		WebElement pTitle = pTitleOptions.get(title);
		assertTrue(pTitle != null);
		pTitle.click();
		
		WebElement pDropdownTitle = pDropdown.findElement(By.tagName("span"));
		assertTrue (pDropdownTitle != null);
		String pDropdownText = pDropdownTitle.getText();
		String compString = "";
		switch(title) {
			case 0:
				compString = "Mr";
				break;
			case 1:
				compString = "Mrs";
				break;
			case 2:
				compString = "Ms";
				break;
			default:
				compString = "Ms";
				break;
		}
		assertEquals(pDropdownText, compString);
		
		// first name
		//WebElement pFirstName = pForm.findElement(By.id("form.passengers.ADT-0.name"));
		WebElement pFirstName = pForm.findElement(By.cssSelector("[data-ref='pax-details__name']"));
		assertTrue(pFirstName != null);
		pFirstName.click();
		WebElement fName = pFirstName.findElement(By.tagName("input"));
		fName.sendKeys(forename);
		String pName = fName.getAttribute("value");
		assertEquals(pName, forename);
		
		// surname
		//WebElement pSurname = pForm.findElement(By.id("form.passengers.ADT-0.surname"));
		WebElement pSurname = pForm.findElement(By.cssSelector("[data-ref='pax-details__surname']"));
		assertTrue(pSurname != null);
		pSurname.click();
		WebElement sName = pSurname.findElement(By.tagName("input"));
		sName.sendKeys(surname);
		String pSurnameText = sName.getAttribute("value");
		assertEquals(pSurnameText, surname);
	}
}
