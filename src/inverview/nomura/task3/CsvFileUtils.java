package inverview.nomura.task3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CsvFileUtils {
	
	private static final String CSV_SEPARATOR = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static  String csvLineFromEvent(Event event) {
        // TODO: implement
    	
    	if(event==null) {
    		return null;
    	}
    	
    	StringBuilder sb=new StringBuilder();
    	sb.append(event.getId())
    	  .append(CSV_SEPARATOR)
    	  .append(event.getName())
    	  .append(CSV_SEPARATOR)
    	  .append(event.getDescription())
    	  .append(CSV_SEPARATOR)
    	  .append(event.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,16))
    	  .append(CSV_SEPARATOR)
    	  .append(event.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0,16));
        return sb.toString();
    }

    public static Event eventFromCsvLine(String csvLine) {
        // TODO: implement
    	
    	if(csvLine==null) {
    		return null;
    	}
    	if(csvLine.indexOf(NEW_LINE_SEPARATOR)>0) {
    		csvLine=csvLine.substring(0,csvLine.indexOf(NEW_LINE_SEPARATOR));
    	}
    	String[] eventStrings = csvLine.split(CSV_SEPARATOR);
    	if(eventStrings.length!=5) {
    		return null;
    	}
    	 	
    	Event event = new Event(Long.valueOf(eventStrings[0]),
    							eventStrings[1],
    							eventStrings[2],
    							LocalDateTime.parse(eventStrings[3]+":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    							LocalDateTime.parse(eventStrings[4]+":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    	return event;
    }

}
