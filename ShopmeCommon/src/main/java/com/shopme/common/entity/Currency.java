package com.shopme.common.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency extends IdBasedEntity{
	
	@Column(nullable = false, length = 45)
	private String name;
	@Column(nullable = false, length = 3)
	private String symbol;
	@Column(nullable = false, length = 4)
	private String code;
	public Currency(String name, String symbol, String code) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.code = code;
	}
	@Override
	public String toString() {
		return name + "-" + code + "-" + symbol;
	}
	
	
}
