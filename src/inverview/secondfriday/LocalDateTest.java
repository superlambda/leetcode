package inverview.secondfriday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class LocalDateTest {

	public static void main(String[] args) {
        LocalDate secondFriday = LocalDate.now().plusMonths(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		System.out.println("Second Friday on : " + secondFriday);

	}

}
