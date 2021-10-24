package org.alkemy.challenge.controllers.pojo;

import java.util.Date;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.entities.Production;


public class ListProductionData {
    private Integer id;
    private String title;
    private Photo image;
    private Date creation;
    private String type;

    public ListProductionData() {
    }

    public ListProductionData(Integer id, Photo image, String title, Date creation, String type) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.creation = creation;
        this.type = type;
    }

    public ListProductionData(Production p) {
        this.id = p.getId();
        this.image = p.getImage();
        this.title = p.getTitle();
        this.creation = p.getCreation();
        this.type = p.getClass().getSimpleName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
