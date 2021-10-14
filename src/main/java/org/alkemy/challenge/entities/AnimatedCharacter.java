package org.alkemy.challenge.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AnimatedCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    private Photo image;

    private String name;
    private Integer age;
    private Integer weight;
    private String lore;

    @ManyToMany(mappedBy = "cast")
    private Set<Production> associateProductions = new HashSet();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date upload = new Date();
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date shutdown;

    public AnimatedCharacter() {
    }

    public AnimatedCharacter(Photo image, String name, Integer age, Integer weight, String lore) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.lore = lore;
    }

//    public AnimatedCharacter(Photo image, String name, Integer age, Integer weight, String lore, Set<Production> productions) {
//        this.image = image;
//        this.name = name;
//        this.age = age;
//        this.weight = weight;
//        this.lore = lore;
//        this.associateProductions = productions;
//    }

    public Integer getId() {
        return id;
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Set<Production> getAssociateProductions() {
        return associateProductions;
    }

    public void setAssociateProductions(Set<Production> associateProductions) {
        this.associateProductions = associateProductions;
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

    public Date getUpload() {
        return upload;
    }

    public void setUpload(Date upload) {
        this.upload = upload;
    }
    
}
