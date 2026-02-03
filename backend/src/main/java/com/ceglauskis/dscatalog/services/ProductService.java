package com.ceglauskis.dscatalog.services;

import com.ceglauskis.dscatalog.dto.CategoryDTO;
import com.ceglauskis.dscatalog.dto.ProductDTO;
import com.ceglauskis.dscatalog.entities.Category;
import com.ceglauskis.dscatalog.entities.Product;
import com.ceglauskis.dscatalog.repositories.ProductRepository;
import com.ceglauskis.dscatalog.services.exceptions.DatabaseException;
import com.ceglauskis.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> page = repository.findAll(pageRequest);
        return page.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity);
    }

    @Transactional(readOnly = true)
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setImgUrl(dto.getImgUrl());
        return new ProductDTO(entity);
    }

    @Transactional(readOnly = true)
    public ProductDTO update(Long id, ProductDTO dto) {
        try{
            Product entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity.setPrice(dto.getPrice());
            entity.setDescription(dto.getDescription());
            entity.setImgUrl(dto.getImgUrl());
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional(readOnly = true)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
