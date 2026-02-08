package com.ceglauskis.dscatalog.services;

import com.ceglauskis.dscatalog.dto.ProductDTO;
import com.ceglauskis.dscatalog.entities.Category;
import com.ceglauskis.dscatalog.entities.Product;
import com.ceglauskis.dscatalog.repositories.CategoryRepository;
import com.ceglauskis.dscatalog.repositories.ProductRepository;
import com.ceglauskis.dscatalog.services.exceptions.DatabaseException;
import com.ceglauskis.dscatalog.services.exceptions.ResourceNotFoundException;
import com.ceglauskis.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId = 1L;
    private long nonExistingId = 2L;
    private long dependentId = 3L;
    private Category category = new Category(2L, "Eletrônicos");
    private Product product = Factory.createProduct();
    private PageImpl<Product> page = new PageImpl<>(List.of(product));
    private ProductDTO dto = Factory.createProductDTO();

    @Test
    public void findAllPagedShouldReturnPage(){
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdIsValid(){
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getId(), result.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldResourceNotFoundExceptionWhenIsDoesNotExist(){
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductWhenIdIsValid(){
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(categoryRepository.getReferenceById(category.getId())).thenReturn(category);

        ProductDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).save(product);
    }

    @Test
    public void updateShouldEntityNotFoundExceptionWhenIdIsDoesNotExist(){
        Mockito.doThrow(EntityNotFoundException.class).when(repository).getReferenceById(nonExistingId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, dto);
        });
    }

    @Test
    public void deleteShouldDatabaseExceptionWhenDependentId(){
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }
}
