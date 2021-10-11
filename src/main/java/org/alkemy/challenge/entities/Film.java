package org.alkemy.challenge.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.alkemy.challenge.entities.enumerations.Stars;

@Entity
@DiscriminatorValue("film")
public class Film extends Production {

    public Film() {
    }

    public Film(Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) {
        super(image, title, creation, stars, cast, categories);
    }
    
}
