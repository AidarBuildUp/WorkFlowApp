package domain;

import java.util.Date;
import java.util.List;

public class Task {
    private String subject;
    private Employee author;
    private List<Employee> executors;
    private Date deadLine;
    private boolean controlled;
    private boolean executed;
    private String text;
}
