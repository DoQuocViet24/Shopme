package com.shopme.common.entity.product;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.entity.IdBasedEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_images")
public class ProductImage extends IdBasedEntity{

	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductImage(String name, Product product) {
		this.name = name;
		this.product = product;
	}

	public ProductImage(Integer id ,String name, Product product) {
		this.name = name;
		this.product = product;
		this.id = id;
	}
	
	@Transient
	public String getImagePath() {
		return "/product-images/" + product.getId() + "/extras/" + this.name;
	}
	
}
