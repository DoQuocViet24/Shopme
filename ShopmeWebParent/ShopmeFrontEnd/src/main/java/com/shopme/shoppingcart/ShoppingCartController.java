package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller
public class ShoppingCartController {

	@Autowired 
	private ShoppingCartService cartService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AddressService addressService;
	@Autowired 
	private ShippingRateService shippingRateService;
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> listCartItems = cartService.listCartItems(customer);
		float estimatedTotal = 0.0F;
		for(CartItem item : listCartItems) {
			estimatedTotal += item.getSubtotal();
		}
		
		Address defaultaddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		boolean usePrimaryAddressAsDefault = false;
		
		if(defaultaddress != null) {
			shippingRate =  shippingRateService.getShippingRateForAddress(defaultaddress);
		}else {
			usePrimaryAddressAsDefault = true;
			shippingRate = shippingRateService.getShippingRateForCustomer(customer);
		}
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("listCartItems", listCartItems);
		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request){
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}
}
