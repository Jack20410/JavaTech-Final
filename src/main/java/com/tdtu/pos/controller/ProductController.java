package com.tdtu.pos.controller;

import com.tdtu.pos.entity.Product;
import com.tdtu.pos.entity.User;
import com.tdtu.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager/products")
@PreAuthorize("hasRole('MANAGER')")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/manager/products")
    public String showProductsPage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        // Add user object to the model
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", currentUser);

        return "manager/products/index";
    }

    @GetMapping
    public String getAllProducts(@RequestParam(value = "category", required = false) String category, Model model) {
        if (category != null && !category.isEmpty() && !"All".equalsIgnoreCase(category)) {
            model.addAttribute("products", productService.getProductsByCategory(category));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "manager/products/index";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "manager/products/add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        try {
            // Check if the file is not empty
            if (!imageFile.isEmpty()) {
                // Define the relative directory based on the product category
                String categoryPath = "images/food_images/" + product.getCategory();
                String fileName = imageFile.getOriginalFilename();

                // Resolve the absolute path to the static directory
                Path staticDirectory = Paths.get("src/main/resources/static", categoryPath);
                Files.createDirectories(staticDirectory); // Ensure directory exists

                // Resolve file path and save the image
                Path filePath = staticDirectory.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Save the relative path to the database (for display in HTML)
                product.setImagePath(categoryPath + "/" + fileName);
            }

            // Save the product to the database
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error saving product image: " + e.getMessage());
        }

        return "redirect:/manager/products";
    }

    @GetMapping("/edit")
    public String showEditProductForm(@RequestParam("id") int id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "manager/products/edit";
        }
        return "redirect:/manager/products";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable int id, @ModelAttribute Product product,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> existingProductOpt = productService.getProductById(id);

            if (!existingProductOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Product not found");
                return "redirect:/manager/products";
            }

            Product existingProduct = existingProductOpt.get();

            if (!imageFile.isEmpty()) {
                String categoryPath = "images/food_images/" + product.getCategory();
                String fileName = imageFile.getOriginalFilename();
                Path imagePath = Paths.get("src/main/resources/static", categoryPath, fileName);

                // Ensure directories exist
                Files.createDirectories(imagePath.getParent());
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(categoryPath + "/" + fileName);
            } else {
                // Keep existing image path if no new file is uploaded
                product.setImagePath(existingProduct.getImagePath());
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }

        return "redirect:/manager/products";
    }

    // Delete product using path variable
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProductById(id);
        return "redirect:/manager/products";
    }

    // Salesperson: View Products Only
    @PreAuthorize("hasRole('SALESPERSON')")
    @GetMapping("/sales")
    public String viewProductsForSales(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "salesperson/sales"; // Salesperson's sales page
    }

}
