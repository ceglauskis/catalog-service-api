package com.ceglauskis.dscatalog.services;

import com.ceglauskis.dscatalog.dto.RoleDTO;
import com.ceglauskis.dscatalog.entities.Role;
import com.ceglauskis.dscatalog.repositories.RoleRepository;
import com.ceglauskis.dscatalog.services.exceptions.DatabaseException;
import com.ceglauskis.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Transactional(readOnly = true)
    public Page<RoleDTO> findAllPaged(Pageable pageable) {
        Page<Role> page = repository.findAll(pageable);
        return page.map(x -> new RoleDTO(x));
    }

    @Transactional(readOnly = true)
    public RoleDTO findById(Long id) {
        Optional<Role> obj = repository.findById(id);
        Role entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new RoleDTO(entity);
    }

    @Transactional
    public RoleDTO insert(RoleDTO dto) {
        Role entity = new Role();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new RoleDTO(entity);
    }

    @Transactional
    public RoleDTO update(Long id, RoleDTO dto) {
        try{
            Role entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new RoleDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
        try{
            repository.deleteById(id);
        }catch (EntityNotFoundException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(RoleDTO dto, Role entity) {
        entity.setAuthority(dto.getAuthority());
    }


}
