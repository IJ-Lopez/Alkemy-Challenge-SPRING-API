package org.alkemy.challenge.controllers.pojo;

import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Photo;

public class ListCategoryData {
    private Photo image;
    private int id;
    private String name;

    public ListCategoryData() {
    }

    public ListCategoryData(Photo image, int id, String name) {
        this.image = image;
        this.id = id;
        this.name = name;
    }
    
    public ListCategoryData(Category c){
        this.image = c.getImage();
        this.id = c.getId();
        this.name = c.getName();
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
