package org.alkemy.challenge.controllers.pojo;

import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;

public class ListCharacterData {
    
    private int id;
    private String name;
    private Photo image;

    public ListCharacterData() {
    }

    public ListCharacterData(Photo image, int id, String name) {
        this.image = image;
        this.id = id;
        this.name = name;
    }

    public ListCharacterData(AnimatedCharacter ac) {
        this.image = ac.getImage();
        this.id = ac.getId();
        this.name = ac.getName();
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photo getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}