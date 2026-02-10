package com.ceglauskis.dscatalog.resources;

import com.ceglauskis.dscatalog.dto.ProductDTO;
import com.ceglauskis.dscatalog.services.ProductService;
import com.ceglauskis.dscatalog.services.exceptions.DatabaseException;
import com.ceglauskis.dscatalog.services.exceptions.ResourceNotFoundException;
import com.ceglauskis.dscatalog.tests.Factory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService service;

    private Long existingId = 1L;
    private Long nonExistingId = 2L;
    private Long dependentId = 3L;

    private ProductDTO dto = Factory.createProductDTO();

    private PageImpl<ProductDTO> page = new PageImpl<>(List.of(dto));

    @Test
    public void findAllShouldReturnPage() throws Exception{
        when(service.findAllPaged(any())).thenReturn(page);

        ResultActions result = mvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception{

        when(service.findById(existingId)).thenReturn(dto);

        ResultActions result = mvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundExceptionWhenIdDoesNotExist() throws Exception{

        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions result = mvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnCreatedAndProductDTO() throws Exception{
        when(service.insert(any())).thenReturn(dto);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mvc.perform(post("/products")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        when(service.update(eq(existingId), any())).thenReturn(dto);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result =
                mvc.perform(put("/products/{id}", nonExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShould1_(){

        doThrow(DataIntegrityViolationException.class).when(service).delete(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() throws Exception{

        doNothing().when(service).delete(existingId);

        ResultActions result =
                mvc.perform(delete("/products/{id}", existingId));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnResourceNotFoundExceptionWhenIdDoesNotExit() throws Exception{

        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);

        ResultActions result =
                mvc.perform(delete("/products/{id}", nonExistingId));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnDatabaseExceptionWhenIdIsDependent() throws Exception{

        doThrow(DatabaseException.class).when(service).delete(dependentId);

        ResultActions result =
                mvc.perform(delete("/products/{id}", dependentId));

        result.andExpect(status().isBadRequest());
    }











}
