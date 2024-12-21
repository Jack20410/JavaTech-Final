package com.tdtu.pos;

import com.tdtu.pos.DTO.PurchaseRequest;
import com.tdtu.pos.entity.*;
import com.tdtu.pos.repository.ProductRepository;
import com.tdtu.pos.repository.UserRepository;
import com.tdtu.pos.service.CustomerService;
import com.tdtu.pos.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InvoiceService invoiceService;


    // Root path redirects to the login page
    @GetMapping("")
    public String index() {
        return "login"; // Serve login.html
    }

    // Manager dashboard route
    @GetMapping("/manager/index")
    public String managerDashboard(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        // Fetch logged-in user details
        String email = currentUser.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Fetch total products count
        long productCount = productRepository.count();

        // Fetch total employees with ROLE_SALESPERSON
        long employeeCount = userRepository.countByRole(User.Role.ROLE_SALESPERSON);

        // Pass user data to the view
        model.addAttribute("user", user);
        model.addAttribute("productCount", productCount);
        model.addAttribute("employeeCount", employeeCount);

        return "manager/index"; // Thymeleaf template name
    }

    //Salesperson dashboard route
    @GetMapping("/salesperson/index")
    public String salespersonDashboard(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        // Fetch logged-in user details
        String email = currentUser.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // Pass user data to the view
        model.addAttribute("user", user);

        return "salesperson/index"; // Thymeleaf template name
    }

    @GetMapping("/sales")
    public String processSales() {
        return "salesperson/sales"; // Thymeleaf template path for sales.html
    }

    // Salesperson: View Products for Sales
    @PreAuthorize("hasRole('SALESPERSON')")
    @GetMapping("/salesperson/sales")
    public String viewProductsForSales(Model model) {
        // Fetch all products grouped by category
        List<String> categories = productRepository.findDistinctCategories();
        Map<String, List<Product>> productsByCategory = new HashMap<>();
        for (String category : categories) {
            List<Product> products = productRepository.findByCategoryOrderByNameAsc(category);
            productsByCategory.put(category, products);
        }

        // Add to model
        model.addAttribute("productsByCategory", productsByCategory);
        return "salesperson/sales"; // Path to salesperson's sales page
    }

    @GetMapping("/manager/employees")
    public String manageEmployees(Model model) {
        // Fetch all users with ROLE_SALESPERSON
        List<User> salespersons = userRepository.findByRole(User.Role.ROLE_SALESPERSON);
        model.addAttribute("salespersons", salespersons);
        model.addAttribute("userForm", new User());
        return "manager/employees"; // Return the Thymeleaf template
    }

    @PostMapping("/manager/employees")
    public String addEmployee(@ModelAttribute("userForm") User user, Model model) {
        user.setRole(User.Role.ROLE_SALESPERSON); // Ensure the correct role
        user.setActive(true); // Default to active status
        userRepository.save(user); // Save user to the database
        return "redirect:/manager/employees"; // Redirect to the employees listing
    }

    @PostMapping("/manager/employees/edit")
    public String editEmployee(@ModelAttribute("userForm") User updatedUser, Model model) {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setActive(updatedUser.getActive());
        // Update only if role or avatar is necessary
        userRepository.save(user); // Save the updated user
        return "redirect:/manager/employees"; // Redirect to the employees listing
    }

    @PreAuthorize("hasRole('SALESPERSON')")
    @PostMapping("/sales/purchase")
    @ResponseBody
    public ResponseEntity<?> savePurchase(@RequestBody PurchaseRequest request) {
        Customer customer = customerService.saveCustomer(request.getCustomerName(), request.getCustomerPhone());
        List<InvoiceItem> items = request.getItems().stream().map(item -> {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProduct(new Product(Math.toIntExact(item.getProductId())));
            invoiceItem.setQuantity(item.getQuantity());
            invoiceItem.setPrice(item.getPrice());
            invoiceItem.setTotal(item.getTotal());
            return invoiceItem;
        }).collect(Collectors.toList());

        Invoice invoice = invoiceService.saveInvoice(
                customer,
                request.getTotalPrice(),
                request.getPaymentMethod(),
                request.getCashReceived(),
                request.getChangeGiven(),
                items
        );

        return ResponseEntity.ok(invoice);

    }
}