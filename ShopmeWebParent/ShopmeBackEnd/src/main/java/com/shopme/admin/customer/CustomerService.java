package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.country.CountryRepository;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {
	
	public static final int CUSTOMERS_PER_PAGE = 10;
	@Autowired
	private CustomerRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CountryRepository countryRepo;
	
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	public List<Customer> listAll() {
		return (List<Customer>) repo.findAll(Sort.by("firstName").ascending());
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE , repo);
	}
	
	public void save(Customer customer) {
		 Customer customerInDB = repo.findById(customer.getId()).get();
		if (customer.getPassword().isEmpty()) {
			customer.setPassword(customerInDB.getPassword());
		} else {
			encodePassword(customer);
		}
	    customer.setEnabled(customerInDB.isEnabled());
	    customer.setCreatedTime(customerInDB.getCreatedTime());
	    customer.setVerificationCode(customerInDB.getVerificationCode());
	    customer.setAuthenticationType(customerInDB.getAuthenticationType());
	    customer.setResetPasswordToken(customerInDB.getResetPasswordToken());
	    repo.save(customer);
	}
	
	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer customerByEmail = repo.getCustomerByEmail(email);
		
		if (customerByEmail == null) return true;
		
		if (customerByEmail.getId() != id) {
				return false;
		}
		
		return true;
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
		}
		
		repo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}
}
