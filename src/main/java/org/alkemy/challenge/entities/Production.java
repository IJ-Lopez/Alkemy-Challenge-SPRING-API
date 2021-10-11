package org.alkemy.challenge.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creation;

    @Enumerated(EnumType.ORDINAL)
    private Stars stars;

    @ManyToMany
    @JoinTable(
            name = "PRODUCTION_ANIMATEDCHARACTER",
            joinColumns = @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ANIMATEDCHARACTER_ID", referencedColumnName = "ID")
    )
    private List<AnimatedCharacter> cast  = new ArrayList();

    @ManyToMany(mappedBy = "productions")
    private List<Category> categories  = new ArrayList();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date upload = new Date();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date shutdown;

    public Production() {
    }

    public Production(Photo image, String title, Date creation, Stars stars, List<AnimatedCharacter> cast, List<Category> categories) {
        this.image = image;
        this.title = title;
        this.creation = creation;
        this.stars = stars;
        this.cast = cast;
        this.categories = categories;
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

    public List<AnimatedCharacter> getCast() {
        return cast;
    }

    public void setCast(List<AnimatedCharacter> cast) {
        this.cast = cast;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
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

}
