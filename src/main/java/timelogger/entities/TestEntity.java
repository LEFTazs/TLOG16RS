package timelogger.entities;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class TestEntity {
    String text;
    @Id int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
