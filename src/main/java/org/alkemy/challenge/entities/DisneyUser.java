package org.alkemy.challenge.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class DisneyUser {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(unique = true)
    private String email;
    private String password;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date upload = new Date();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date shutdown;

    public DisneyUser() {
    }

    public DisneyUser(String email, String password) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Date getUpload() {
        return upload;
    }

    public void setUpload(Date upload) {
        this.upload = upload;
    }

    public Date getShutdown() {
        return shutdown;
    }

    public void setShutdown(Date shutdown) {
        this.shutdown = shutdown;
    }
    
    public boolean isDown(){
        return shutdown != null;
    }
    
}
