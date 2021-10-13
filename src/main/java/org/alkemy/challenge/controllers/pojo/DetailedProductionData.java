package org.alkemy.challenge.controllers.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;
import org.alkemy.challenge.entities.enumerations.Stars;

public class DetailedProductionData {
    private Integer id;
    private String type;
    private Photo image;
    private String title;
    private Date creation;
    private Stars stars;
    private List<ListCharacterData> cast = new ArrayList();
    private List<String> categories = new ArrayList();

    public DetailedProductionData() {
    }

    public DetailedProductionData(Integer id, String type, Photo image, String title, Date creation, Stars stars, List<ListCharacterData> cast, List<String> categories) {
        this.id = id;
        this.type = type;
        this.image = image;
        this.title = title;
        this.creation = creation;
        this.stars = stars;
        this.cast = cast;
        this.categories = categories;
    }

    public DetailedProductionData(Production p) {
        this.id = p.getId();
        this.type = p.getClass().getSimpleName();
        this.image = p.getImage();
        this.title = p.getTitle();
        this.creation = p.getCreation();
        this.stars = p.getStars();
        p.getCast().forEach(ac -> {
            this.cast.add(new ListCharacterData(ac));
        });
        p.getCategories().forEach(e -> {
            this.categories.add(e.getName());
        });
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<ListCharacterData> getCast() {
        return cast;
    }

    public void setCast(List<ListCharacterData> cast) {
        this.cast = cast;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    
    
    
}
