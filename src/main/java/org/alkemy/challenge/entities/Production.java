package org.alkemy.challenge.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.alkemy.challenge.entities.enumerations.Stars;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "production_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Photo image;

    private String title;

    @Temporal(value = TemporalType.DATE)
    private Date creation;

    @Enumerated(EnumType.ORDINAL)
    private Stars stars;

    @ManyToMany
    @JoinTable(
            name = "PRODUCTION_ANIMATEDCHARACTER",
            joinColumns = @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ANIMATEDCHARACTER_ID", referencedColumnName = "ID")
    )
    private Set<AnimatedCharacter> cast  = new HashSet();

    @ManyToMany
    @JoinTable(
            name = "PRODUCTION_CATEGORY",
            joinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID")
    )
    private Set<Category> categories  = new HashSet();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date upload = new Date();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date shutdown;

    public Production() {
    }

    public Production(Photo image, String title, Date creation, Stars stars, Set<AnimatedCharacter> cast, Set<Category> categories) {
        this.image = image;
        this.title = title;
        this.creation = creation;
        this.stars = stars;
        this.cast = cast;
        this.categories = categories;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Stars getStars() {
        return stars;
    }

    public void setStars(Stars stars) {
        this.stars = stars;
    }

    public Set<AnimatedCharacter> getCast() {
        return cast;
    }

    public void setCast(Set<AnimatedCharacter> cast) {
        this.cast = cast;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    
    public Date getUpload() {
        return upload;
    }

    public void setUpload(Date upload) {
        this.upload = upload;
    }

    public boolean isDown() {
        return shutdown != null;
    }

    public Date getShutdown() {
        return shutdown;
    }

    public void setShutdown(Date shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.image);
        hash = 67 * hash + Objects.hashCode(this.title);
        hash = 67 * hash + Objects.hashCode(this.creation);
        hash = 67 * hash + Objects.hashCode(this.stars);
        hash = 67 * hash + Objects.hashCode(this.cast);
        hash = 67 * hash + Objects.hashCode(this.categories);
        hash = 67 * hash + Objects.hashCode(this.upload);
        hash = 67 * hash + Objects.hashCode(this.shutdown);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Production other = (Production) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
            return false;
        }
        if (!Objects.equals(this.creation, other.creation)) {
            return false;
        }
        if (this.stars != other.stars) {
            return false;
        }
        if (!Objects.equals(this.cast, other.cast)) {
            return false;
        }
        if (!Objects.equals(this.categories, other.categories)) {
            return false;
        }
        if (!Objects.equals(this.upload, other.upload)) {
            return false;
        }
        if (!Objects.equals(this.shutdown, other.shutdown)) {
            return false;
        }
        return true;
    }
    
    

}
