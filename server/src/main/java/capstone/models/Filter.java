package capstone.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Filter {
    private String type;
    private int players;
    private int size;
    private Timestamp start;
}
