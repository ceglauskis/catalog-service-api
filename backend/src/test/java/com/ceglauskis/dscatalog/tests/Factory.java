package com.ceglauskis.dscatalog.tests;

import com.ceglauskis.dscatalog.dto.ProductDTO;
import com.ceglauskis.dscatalog.dto.users.UserDTO;
import com.ceglauskis.dscatalog.entities.Category;
import com.ceglauskis.dscatalog.entities.Product;
import com.ceglauskis.dscatalog.entities.Role;
import com.ceglauskis.dscatalog.entities.User;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static User createUser(){
        User user = new User(1L, "Maikon", "Ceglaukis", "maikoceglauskis@gmail.com", "Reway.1703");
        user.getRoles().add(new Role(1L, "Admin"));
        return user;
    }

    public static UserDTO createUserDTO(){
        User user = createUser();
        return new UserDTO(user, user.getRoles());
    }
}
