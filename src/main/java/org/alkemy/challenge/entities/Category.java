package org.alkemy.challenge.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne
    private Photo image;

    @ManyToMany
    @JoinTable(
            name = "CATEGORY_PRODUCTION",
            joinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID")
    )
    private List<Production> productions = new ArrayList();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date upload = new Date();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date shutdown;

    public Category() {
    }

    public Category(String name, Photo image, List<Production> productions) {
        this.name = name;
        this.image = image;
        this.productions = productions;
    }

    public Integer getId() {
        return id;
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

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
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
