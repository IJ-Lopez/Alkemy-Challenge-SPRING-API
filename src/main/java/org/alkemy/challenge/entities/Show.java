package org.alkemy.challenge.entities;

import java.util.Date;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.alkemy.challenge.entities.enumerations.Stars;

@Entity
@DiscriminatorValue("show")
public class Show extends Production {

    public Show() {
    }

    public Show(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) {
        super(image, title, creation, stars, cast, categories);
    }
    
}
