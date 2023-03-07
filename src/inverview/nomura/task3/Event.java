package inverview.nomura.task3;

import java.time.LocalDateTime;

public class Event {
	private Long id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    
    
	public Event(Long id, String name, String description, LocalDateTime start, LocalDateTime end) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
    
    

}
