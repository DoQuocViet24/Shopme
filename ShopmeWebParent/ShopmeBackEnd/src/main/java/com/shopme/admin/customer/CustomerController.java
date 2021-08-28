package com.shopme.admin.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.exception.UserNotFoundException;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage() {
		return "redirect:/customers/page/1?sortField=firstName&&sortDir=asc";
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model
			) {
		 service.listByPage(pageNum, helper);
			
		
		return "customers/customers";		
	}
	
	
	@PostMapping("/customers/save")
	public String saveUser(Customer customer, RedirectAttributes redirectAttributes){
		
	    service.save(customer);
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		
		return getRedirectURLtoAffectedCustomer(customer);
	}

	private String getRedirectURLtoAffectedCustomer(Customer customer) {
		String firstPartOfEmail = customer.getEmail().split("@")[0];
		return "redirect:/customers/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}
	
	
	@GetMapping("/customers/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = service.get(id);
			List<Country> listCountries = service.listAllCountries();
			
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
			return "customers/customer_form";
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) throws CustomerNotFoundException {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", 
					"The customer ID " + id + " has been deleted successfully");
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);			
			model.addAttribute("customer", customer);		
			
			return "customers/customer_detail_modal";
			
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/customers";
		}
	}	
	
	@GetMapping("/customers/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Customer> listCustomers = service.listAll();
		CustomerCsvExporter exporter = new CustomerCsvExporter();
		exporter.export(listCustomers, response);
	}
}
