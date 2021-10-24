package org.alkemy.challenge.controllers.pojo;

import java.util.ArrayList;
import java.util.List;
import org.alkemy.challenge.entities.AnimatedCharacter;
import org.alkemy.challenge.entities.Photo;

public class DetailedCharacterData {
    
    private int id;
    private Photo image;
    private String name;
    private Integer age;
    private Integer weight;
    private String lore;
    private List<ListProductionData> productions;

    public DetailedCharacterData() {
    }

    public DetailedCharacterData(Photo image, int id, String name, Integer age, Integer weight, String lore, List<ListProductionData> productions) {
        this.image = image;
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.lore = lore;
        this.productions = productions;
    }

    public DetailedCharacterData(AnimatedCharacter ac) {
        this.image = ac.getImage();
        this.id = ac.getId();
        this.name = ac.getName();
        this.age = ac.getAge();
        this.weight = ac.getWeight();
        this.lore = ac.getLore();
        this.productions = new ArrayList<>();
        ac.getAssociateProductions().forEach( p -> {
            productions.add(new ListProductionData(p));
        });
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public List<ListProductionData> getProductions() {
        return productions;
    }

    public void setProductions(List<ListProductionData> productions) {
        this.productions = productions;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
}
