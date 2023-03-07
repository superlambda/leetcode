package inverview.nomura.task3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;

class CsvFileTest {

	@Test
	void test() {
		CsvFileDao dao= new CsvFileDao();
		try {
			List<Event> events = dao.readAll();
			for(Event event: events) {
				System.out.println(event.getId()+"-" 
						+ event.getName() 
						+ "-" + event.getDescription()
						+ "-" + event.getStart()
						+ "-" + event.getEnd());
			}
			
			Event event = new Event((long)4,
									"Peter",
									"Test",
									LocalDateTime.parse("2022-03-03T08:10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
									LocalDateTime.parse("2022-03-04T20:10:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			dao.write(event);
			event = null;
			event = dao.read(4);
			System.out.println(event.getId()+"-" 
					+ event.getName() 
					+ "-" + event.getDescription()
					+ "-" + event.getStart()
					+ "-" + event.getEnd());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
