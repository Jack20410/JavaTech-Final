package com.tdtu.pos;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.entity.Product;
import com.tdtu.pos.entity.User;
import com.tdtu.pos.repository.CustomerRepository;
import com.tdtu.pos.repository.ProductRepository;
import com.tdtu.pos.repository.UserRepository;
import com.tdtu.pos.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

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
    private UserService userService;

    // ---- USER CRUD TESTS ----
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFullName("admin");
        user.setEmail("admin@admin.com");
        user.setPassword("admin");
        user.setAvatar("images/avatar/avatar.png");// Raw password
        user.setRole(User.Role.ROLE_MANAGER);
        user.setActive(true);

        userService.saveUser(user);

        User savedUser = user;
        assertNotNull(savedUser.getId());
         // Ensure password is hashed
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
        customer.setFullName("Jane Doe");
        customer.setPhoneNumber("1234567890");
        customer.setAddress("123 Main St");

        Customer savedCustomer = customerRepository.save(customer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Jane Doe", savedCustomer.getFullName());
    }

    @Test
    public void testReadCustomer() {
        Optional<Customer> customer = customerRepository.findById(1);
        assertTrue(customer.isPresent());
        assertEquals("Jane Doe", customer.get().getFullName());
    }

    @Test
    public void testUpdateCustomer() {
        Optional<Customer> customerOptional = customerRepository.findById(1);
        assertTrue(customerOptional.isPresent());

        Customer customer = customerOptional.get();
        customer.setFullName("Jane Updated");
        Customer updatedCustomer = customerRepository.save(customer);

        assertEquals("Jane Updated", updatedCustomer.getFullName());
    }

    @Test
    public void testDeleteCustomer() {
        customerRepository.deleteById(1);
        Optional<Customer> customer = customerRepository.findById(1);
        assertFalse(customer.isPresent());
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
//        product.setName("Burger");
//        product.setCategory("Burgers");
//        product.setRetailPrice(70000);
//        product.setAvailable(true); // Change availability

        // Save the updated product
        Product updatedProduct = productRepository.save(product);

        // Assertions to ensure updates are applied
//        assertEquals("Burger", updatedProduct.getName());
//        assertEquals("Burgers", updatedProduct.getCategory());
//        assertEquals(70000, updatedProduct.getRetailPrice());
//        assertTrue(updatedProduct.isAvailable()); // Ensure the availability status is updated
    }

    @Test
    public void testDeleteProduct() {
        productRepository.deleteById(1);
        Optional<Product> product = productRepository.findById(1);
        assertFalse(product.isPresent());
    }

    @Test
    public void testListProducts() {
        // Retrieve all products
        List<Product> products = productRepository.findAll();

        // Assert that the list is not empty
        assertNotNull(products);
        assertFalse(products.isEmpty());

        // Print out all products
        System.out.println("List of Products:");
        for (Product product : products) {
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Price: " + product.getRetailPrice());
            System.out.println("Available: " + product.isAvailable());
            System.out.println("Image Path: " + product.getImagePath());
            System.out.println("--------------------");
        }
    }
}
