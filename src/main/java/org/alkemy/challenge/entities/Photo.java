package org.alkemy.challenge.entities;

import java.io.IOException;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String mime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    public Photo() {
    }

    public Photo(MultipartFile file) throws ServiceException {
        this.name = file.getName();
        this.mime = file.getContentType();
        try {
            this.content = file.getBytes();
        } catch (IOException ex) {
            throw new ServiceException();
        }
    }

    public Photo(String name, String mime, byte[] content) {
        this.name = name;
        this.mime = mime;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
