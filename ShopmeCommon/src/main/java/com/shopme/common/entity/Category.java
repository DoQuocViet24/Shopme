package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	@Column(length = 64, nullable = false, unique = true)
	private String alias;
	@Column(length = 128, nullable = false)
	private String image;
	
	private boolean enabled;
	@OneToOne
	@JoinColumn(name = "parent_id")
	private Category parent;
	@OneToMany(mappedBy = "parent")
	private Set<Category> children = new HashSet<>();
	@Transient
	private boolean hasChildren;
	public Category(int i) {
		this.id=id;
	}
	public Category(String name) {
		this.name = name;
		this.alias= name;
		this.image="default.png";
	}
	public Category(Integer id,String name) {
		this.name = name;
		this.id = id;
	}
	public Category(String name, Category parent) {
		this(name);
		this.parent = parent;
		
	}
	
	public static Category copy(Category  category) {
		Category copyCategory = new Category();
		copyCategory.setId(category.getId());
		copyCategory.setName(category.getName());
		copyCategory.setImage(category.getImage());
		copyCategory.setAlias(category.getAlias());
		copyCategory.setEnabled(category.isEnabled());
		copyCategory.setHasChildren(category.getChildren().size() > 0);
		return copyCategory;
		
	}
	
	public static Category copy(Category  category,String name) {
		Category copyCategory = Category.copy(category);
		copyCategory.setName(name);
		
		return copyCategory;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Transient
	public String getImagePath() {
		if(this.id == null) return "/images/image-thumbnail.png";
		return "/category-image/"+this.id+"/"+this.image;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
