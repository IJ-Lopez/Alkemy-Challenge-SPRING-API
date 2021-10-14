package org.alkemy.challenge.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.alkemy.challenge.entities.Photo;
import org.alkemy.challenge.repositories.PhotoRepository;
import org.alkemy.challenge.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    @Transactional
    public Photo create(Photo photo) throws ServiceException {
        if (validation(photo)) {
            return photoRepo.save(photo);
        }
        throw new ServiceException("Photo already exists");
    }

    @Transactional
    public Photo create(MultipartFile file) throws ServiceException {
        Photo photo = new Photo(file);
        return create(photo);
    }

    @Transactional
    public Photo createIfNotExists(Photo photo) throws ServiceException {
        if (photo != null) {
            if (validation(photo)) {
                return photoRepo.save(photo);
            }
        }
        return photo;
    }
    
    @Transactional
    public Photo createIfNotExists(MultipartFile file) throws ServiceException {
        Photo photo = new Photo(file);
        return createIfNotExists(photo);
    }

    public List<Photo> getAll() {
        return photoRepo.findAll();
    }

    public Photo get(int id) throws ServiceException {
        Optional<Photo> opt = photoRepo.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new ServiceException("Photo not found");
        }
    }

    public List<Photo> getByName(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Photo name cannot be null");
        }
        return photoRepo.findByNameIgnoreCase(name);
    }

    public List<Photo> getByNameLike(String name) throws ServiceException {
        if (name == null) {
            throw new ServiceException("Photo name cannot be null");
        }
        return photoRepo.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Photo update(int id, MultipartFile file) throws ServiceException {
        validation(new Photo(file));
        Optional<Photo> opt = photoRepo.findById(id);
        if (opt.isPresent()) {
            Photo photo = opt.get();
            photo.setMime(file.getContentType());
            photo.setName(file.getName());
            try {
                photo.setContent(file.getBytes());
            } catch (IOException ex) {
                throw new ServiceException(ex.getMessage());
            }
            return photoRepo.save(photo);
        } else {
            throw new ServiceException("Photo not found");
        }
    }

    @Transactional
    public Photo update(int id, Photo updatedPhoto) throws ServiceException {
        validation(updatedPhoto);
        Optional<Photo> opt = photoRepo.findById(id);
        if (opt.isPresent()) {
            Photo photo = opt.get();
            photo.setMime(updatedPhoto.getMime());
            photo.setName(updatedPhoto.getName());
            photo.setContent(updatedPhoto.getContent());
            return photoRepo.save(photo);
        } else {
            throw new ServiceException("Photo not found");
        }
    }

    @Transactional
    public Photo update(Photo updatedPhoto) throws ServiceException {
        validation(updatedPhoto);
        if (updatedPhoto.getId() == null) {
            throw new ServiceException("Photo ID cannot be null");
        }
        Optional<Photo> opt = photoRepo.findById(updatedPhoto.getId());
        if (opt.isPresent()) {
            Photo photo = opt.get();
            photo.setMime(updatedPhoto.getMime());
            photo.setName(updatedPhoto.getName());
            photo.setContent(updatedPhoto.getContent());
            return photoRepo.save(photo);
        } else {
            throw new ServiceException("Photo not found");
        }
    }

    private boolean validation(Photo photo) throws ServiceException {
        if (photo == null) {
            throw new ServiceException("Photo cannot be null");
        }
        if (photo.getName() == null || photo.getName().isEmpty()) {
            throw new ServiceException("Photo name cannot be null");
        }
        if (photo.getMime() == null) {
            throw new ServiceException("Photo content type cannot be null");
        }
        if (photo.getContent() == null) {
            throw new ServiceException("Photo content cannot be null");
        }
        if ((photo.getId() != null) && (get(photo.getId()) != null)) {
            if (get(photo.getId()) != photo) {
                throw new ServiceException("Photo ID already exists");
            }
            return false;
        }
        return true;
    }

//    Photo checkPhoto(Photo photo) throws ServiceException {
//        if (photo != null) {
//            if ((photo.getId() == null) || get(photo.getId()) == null) {
//                photo = create(photo);
//            } else if (get(photo.getId()) != photo) {
//                throw new ServiceException("Photo ID already exists");
//            }
//        }
//        return photo;
//    }
    public ResponseEntity<byte[]> getResponseEntity(Photo photo) {
        if (photo == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        byte[] content = photo.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity(content, headers, HttpStatus.OK);
    }

}
