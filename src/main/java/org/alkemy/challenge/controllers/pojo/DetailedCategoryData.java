package org.alkemy.challenge.controllers.pojo;

import java.util.ArrayList;
import java.util.List;
import org.alkemy.challenge.entities.Category;
import org.alkemy.challenge.entities.Photo;

public class DetailedCategoryData {
    private int id;
    private String name;
    private Photo image;
    private List<ListProductionData> productions;

    public DetailedCategoryData() {
    }

    public DetailedCategoryData(Category c) {
        this.id = c.getId();
        this.name = c.getName();
        this.image = c.getImage();
        this.productions = new ArrayList<>();
        c.getProductions().forEach(p -> {
            productions.add(new ListProductionData(p));
        });
    }
    
    public DetailedCategoryData(int id, String name, Photo image, List<ListProductionData> productions) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.productions = productions;
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

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public List<ListProductionData> getProductions() {
        return productions;
    }

    public void setProductions(List<ListProductionData> productions) {
        this.productions = productions;
    }
    
    
}
