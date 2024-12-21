package com.tdtu.pos.controller;

import com.tdtu.pos.entity.Customer;
import com.tdtu.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Ensures view rendering
@RequestMapping("/salesperson") // Base path for salesperson views
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // View all customers
    @GetMapping("/view-customers")
    public String viewCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("newCustomer", new Customer());
        return "salesperson/view-customers";
    }

    // Add a new customer
//    @PostMapping("/save-customer")
//    public String saveCustomer(@ModelAttribute("newCustomer") Customer customer) {
//      customerService.saveCustomer(customer);
//        return "redirect:/salesperson/view-customers";
//    }

//    // Edit an existing customer
//    @PostMapping("/update-customer")
//    public String updateCustomer(@ModelAttribute("customer") Customer updatedCustomer) {
//        customerService.updateCustomer(updatedCustomer);
//        return "redirect:/salesperson/view-customers";
//    }
//
//    // Delete a customer by ID
//    @GetMapping("/delete-customer/{id}")
//    public String deleteCustomer(@PathVariable("id") int id) {
//        customerService.deleteCustomer(id);
//        return "redirect:/salesperson/view-customers";
//    }

}
