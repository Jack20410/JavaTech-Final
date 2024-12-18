package com.tdtu.pos;

import com.tdtu.pos.entity.User;
import com.tdtu.pos.repository.ProductRepository;
import com.tdtu.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


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

}
