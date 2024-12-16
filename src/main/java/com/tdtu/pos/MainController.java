package com.tdtu.pos;

import com.tdtu.pos.entity.User;
import com.tdtu.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

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

        // Pass user data to the view
        model.addAttribute("user", user);

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
}
