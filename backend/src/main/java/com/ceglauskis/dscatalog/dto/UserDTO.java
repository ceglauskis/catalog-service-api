package com.ceglauskis.dscatalog.dto;

import com.ceglauskis.dscatalog.entities.Role;
import com.ceglauskis.dscatalog.entities.User;
import com.ceglauskis.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDTO {

    private Long id;
    @NotBlank(message = "Required field")
    private String firstName;
    private String lastName;
    @Email(message = "Please send a valid email address")
    private String email;

    private List<RoleDTO> roles = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
    }

    public UserDTO(User entity, Set<Role> roles){
        this(entity);
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }
}
