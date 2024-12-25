package com.tdtu.pos;

import com.tdtu.pos.entity.*;
import com.tdtu.pos.repository.*;
import com.tdtu.pos.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // To order test execution
public class CRUDTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserService userService;

    // ---- USER CRUD TESTS ----
    // ---- RUN THIS TEST FIRST BEFORE LOG IN ----
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFullName("Admin");
        user.setEmail("admin@admin.com");
        user.setPassword("admin");
        user.setAvatar("images/avatar/avatar.png");
        user.setRole(User.Role.ROLE_MANAGER);
        user.setActive(true);
        userService.saveUser(user);

        User savedUser = user;
    }

    @Test
    public void testReadUser() {
        Optional<User> user = userRepository.findById(1);
        assertTrue(user.isPresent());
        assertEquals("John Doe", user.get().getFullName());
    }

    @Test
    public void testUpdateUser() {
        Optional<User> userOptional = userRepository.findById(1);
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        user.setFullName("John Updated");
        User updatedUser = userRepository.save(user);

        assertEquals("John Updated", updatedUser.getFullName());
    }

    @Test
    public void testDeleteUser() {
        int userID = 7;
        userRepository.deleteById(userID);
        Optional<User> optionalUser = userRepository.findById(userID);
        assertFalse(optionalUser.isPresent());
    }

    // ---- CUSTOMER CRUD TESTS ----
    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer.setPhoneNumber("1234567890");


        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Jane Doe", savedCustomer.getName());
    }
    // ---- PRODUCT CRUD TESTS ----
    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setName("Burger Chicken Special");
        product.setCategory("burgers");
        product.setRetailPrice(70000);
        product.setQuantity(10);
        product.setImagePath("images/food_images/burgers/specialchick.png");
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct.getId());
        assertTrue(savedProduct.isAvailable());
    }

    @Test
    public void testReadProduct() {
        Optional<Product> product = productRepository.findById(2);
        assertTrue(product.isPresent());
        assertEquals("Coke", product.get().getName());
    }

    @Test
    public void testUpdateProduct() {
        // Fetch the product by ID (assume product with ID 1 exists)
        Optional<Product> productOptional = productRepository.findById(5);
        assertTrue(productOptional.isPresent());

        // Update the product details
        Product product = productOptional.get();
        product.setImagePath("images/food_images/burgers/chickbg.png");

        // Save the updated product
        Product updatedProduct = productRepository.save(product);
    }

    @Test
    public void testDeleteProduct() {
        productRepository.deleteById(1);
        Optional<Product> product = productRepository.findById(1);
        assertFalse(product.isPresent());
    }
}
