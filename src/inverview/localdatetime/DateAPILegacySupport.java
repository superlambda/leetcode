package inverview.localdatetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateAPILegacySupport {

	public static void main(String[] args) {
		// Date to Instant
		Instant timestamp = new Date().toInstant();
		// Now we can convert Instant to LocalDateTime or other similar classes
		LocalDateTime date = LocalDateTime.ofInstant(timestamp, ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
		System.out.println("Date = " + date);

		// Calendar to Instant
		Instant time = Calendar.getInstance().toInstant();
		System.out.println("Instant time: " + time);
		// TimeZone to ZoneId
		ZoneId defaultZone = TimeZone.getDefault().toZoneId();
		System.out.println(defaultZone);

		// ZonedDateTime from specific Calendar
		ZonedDateTime gregorianCalendarDateTime = new GregorianCalendar().toZonedDateTime();
		System.out.println("ZonedDateTime from specific Calendar: " + gregorianCalendarDateTime);

		// Date API to Legacy classes
		Date dt = Date.from(Instant.now());
		System.out.println("Legacy Date: " + dt);

		TimeZone tz = TimeZone.getTimeZone(defaultZone);
		System.out.println("Legacy TimeZone: " + tz);

		GregorianCalendar gc = GregorianCalendar.from(gregorianCalendarDateTime);
		System.out.println("Legacy GregorianCalendar: " + gc);
		//As you can see that legacy TimeZone and GregorianCalendar classes toString() 
		//methods are too verbose and not user friendly.

	}

}
