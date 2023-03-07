package inverview.nomura.task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class CsvFileDao {
	
	public CsvFileDao() {}

    public List<Event> readAll() throws Exception {
        String csvFileName = PropertiesFileHelper.csvFileNameFromProperties();
        // TODO: implement
        
        List<Event> events=new LinkedList<>();
        //of is introduced in 11        
        Path file = Path.of(csvFileName);

        try(Stream<String> lines = Files.lines(file)){
            lines.forEach(line -> {
            	Event event = CsvFileUtils.eventFromCsvLine(line);
                if(event!=null){
            	    events.add(event);
                }
                
            });
        }
//        BufferedReader reader = null;
//        try {
//        	reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName))
//        		 );
//	        String csvRecord = null;
//	        
//	        while ((csvRecord = reader.readLine()) != null) {
//	        	events.add(CsvFileUtils.eventFromCsvLine(csvRecord));
//	        }
//        }
//        finally {
//        	if(reader!=null) {
//        		reader.close();
//        	}
//        	
//        }
        
        return events;
    }

    public void write(Event event) throws Exception{
        String csvFileName = PropertiesFileHelper.csvFileNameFromProperties();
        String csvLine = CsvFileUtils.csvLineFromEvent(event) + "\n";
        // TODO: implement
        try {
            Files.write(Paths.get(csvFileName), csvLine.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
        
//        FileWriter pw;
//		try {
//			pw = new FileWriter(csvFileName,true);
//			pw.append(csvLine);
//			pw.flush();
//			pw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }
    
    public Event read(long eventId) throws Exception{
    	List<Event> events = readAll();
        for(Event event: events) {
        	if(event.getId().equals(eventId)) {
        		return event;
        	}
        }
        return null;
    }

}

