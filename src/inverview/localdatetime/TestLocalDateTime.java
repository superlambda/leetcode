package inverview.localdatetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestLocalDateTime {

	public static void main(String[] args) {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("now: " + now);

		// Current timestamp in UTC
		LocalDateTime utcTimestamp = LocalDateTime.now(ZoneId.of("UTC"));
		System.out.println("utcTimestamp: " + utcTimestamp);

		// Nanoseconds precision
		LocalDateTime localDateTime1 = LocalDateTime.of(2019, 03, 28, 14, 33, 48, 640000);
		System.out.println("localDateTime1: " + localDateTime1);

		// Using Month Enum
		LocalDateTime localDateTime2 = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33, 48, 000000);
		System.out.println("localDateTime2: " + localDateTime2);

		// Seconds level precision
		LocalDateTime localDateTime3 = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33, 48);
		System.out.println("localDateTime3: " + localDateTime3);

		// Minutes level precision
		LocalDateTime localDateTime4 = LocalDateTime.of(2019, Month.MARCH, 28, 14, 33);
		System.out.println("localDateTime4: " + localDateTime4);
		
		//local date + local time
		LocalDate date = LocalDate.of(2109, 03, 28);
		LocalTime time = LocalTime.of(10, 34);	
		LocalDateTime localDateTime5 = LocalDateTime.of(date, time);
		System.out.println("localDateTime5: " + localDateTime5);
		
		
		//1 - default time pattern
		String time2 = "2019-03-27T10:15:30";
		LocalDateTime localTimeObj = LocalDateTime.parse(time2);
		System.out.println("localTimeObj: " + localTimeObj);
		//2 - specific date time pattern
		//Without setting  Locale.US there is java.time.format.DateTimeParseException, another fix is 
		//change AM to 上午 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a", Locale.US);
		String time3 = "2019-03-27 10:15:30 AM";
		LocalDateTime localTimeObj2 = LocalDateTime.parse(time3, formatter);
		System.out.println("localTimeObj2: " + localTimeObj2);
		
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
		LocalDateTime now2 = LocalDateTime.now();
		String dateTimeString = now2.format(formatter);
		System.out.println("dateTimeString: " + dateTimeString);
		
		now = LocalDateTime.now();
		System.out.println("now: " + now);
		localDateTime1 = now.plusHours(3);	
		System.out.println("3 hours later: " + localDateTime1);
		localDateTime2 = now.minusMinutes(3);
		System.out.println("3 minutes earliar: " + localDateTime2);
		localDateTime2 = now.plusYears(1);
		System.out.println("Next year same time: " + localDateTime2);
		localDateTime2 = now.minusYears(1);
		System.out.println("Last year same time: " + localDateTime2);


	}

}
