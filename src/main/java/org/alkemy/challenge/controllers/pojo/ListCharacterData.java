package org.alkemy.challenge.controllers.pojo;

import org.alkemy.challenge.entities.Photo;
import org.springframework.http.ResponseEntity;

public class ListCharacterData {
    
    private ResponseEntity<byte[]> image;
    private int id;
    private String name;

    public ListCharacterData() {
    }

    public ListCharacterData(ResponseEntity<byte[]> image, int id, String name) {
        this.image = image;
        this.id = id;
        this.name = name;
    }

    public ResponseEntity<byte[]> getImage() {
        return image;
    }

    public void setImage(ResponseEntity<byte[]> image) {
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
