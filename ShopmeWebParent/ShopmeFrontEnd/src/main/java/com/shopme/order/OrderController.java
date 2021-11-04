package com.shopme.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Feedback;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.product.Product;
import com.shopme.customer.CustomerService;
import com.shopme.feedback.FeedbackRepository;
import com.shopme.feedback.FeedbackService;

@Controller
public class OrderController {
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	@Autowired private FeedbackService feedbackService;
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		return listOrdersByPage(model, request, 1, "orderTime", "desc", null);
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listOrdersByPage(Model model, HttpServletRequest request,
						@PathVariable(name = "pageNum") int pageNum,
						String sortField, String sortDir, String orderKeyword
			) {
		Customer customer = getAuthenticatedCustomer(request);
		
		Page<Order> page = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
		List<Order> listOrders = page.getContent();
		long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
		long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("listOrders", listOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("orderKeyword", orderKeyword);
		model.addAttribute("moduleURL", "/orders");
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		return "orders/orders_customer";		
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(Model model,
			@PathVariable(name = "id") Integer id, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		
		Order order = orderService.getOrder(id, customer);		
		model.addAttribute("order", order);
		
		return "orders/order_details_modal";
	}	
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}	
	
	@GetMapping("/orders/feedback/{id}")
	public String viewFeedbackOrder(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request){
		Customer customer = getAuthenticatedCustomer(request);
		Order order = orderService.getOrder(id, customer);
		model.addAttribute("order", order);
		return "feedback/feedback_order";
	}
	
	@PostMapping("/orders/feedback/save")
	public String saveFeedbackOrder(HttpServletRequest request, Feedback feedback){
		Integer productId = Integer.parseInt(request.getParameter("product_id"));
		Integer orderDetailId = Integer.parseInt(request.getParameter("orderDetail_id"));
		int orderId = Integer.parseInt(request.getParameter("order_id"));
		String message = request.getParameter("feedbackMes");
		Feedback feedbackDB =  feedbackService.getFeedback(productId, orderDetailId);
		if(feedbackDB == null) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setId(orderDetailId);
			Product product = new Product();
			product.setId(productId);
			feedback.setProduct(product);
			feedback.setOrderDetail(orderDetail);
			feedback.setMessage(message);
			feedbackService.saveFeedback(feedback);
		}else {
			feedbackDB.setMessage(message);
			feedbackService.saveFeedback(feedbackDB);
		}
		
		return "redirect:/orders/feedback/"+orderId;
	}
}

