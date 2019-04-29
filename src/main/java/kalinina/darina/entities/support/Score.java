package kalinina.darina.entities.support;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class Score implements Serializable {
    private int score;
}
