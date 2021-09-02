package com.shopme.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem extends IdBasedEntity{
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	@Transient
	private float shippingCost;
	@Getter
	@Setter
	private int quantity;
	
	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice()*quantity;
	}

	@Transient
	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
	
}
