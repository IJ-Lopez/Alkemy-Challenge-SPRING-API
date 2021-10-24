package org.alkemy.challenge.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
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
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("mime")
    private String mime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonProperty("content")
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.mime);
        hash = 37 * hash + Arrays.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Photo other = (Photo) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.mime, other.mime)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }
    
    

}
