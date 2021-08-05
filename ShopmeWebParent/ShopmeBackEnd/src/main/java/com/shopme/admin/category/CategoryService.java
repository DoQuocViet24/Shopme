package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repo;
	
	public List<Category> listAll(){
		List<Category> rootCategories = repo.findRootCategories();
		
		return listHierarchicalCategories(rootCategories) ;
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
		List<Category> hierarchicalCategories =  new ArrayList<>();
		
		for(Category rootCategory: rootCategories) {
			hierarchicalCategories.add(Category.copy(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for(Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copy(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories,subCategory,1);
			}
		}
		
		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0;i<newSubLevel;i++) {
				name += "--";
			}
			name +=subCategory.getName();
			hierarchicalCategories.add(Category.copy(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories,subCategory, newSubLevel);
		}
		
		
	}

	public Category save(Category category) {
		return repo.save(category);
	}
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB	= repo.findAll();
		for(Category category : categoriesInDB) {
			if(category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getId(),category.getName()));//
				Set<Category> children = category.getChildren();
				for(Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(new Category(subCategory.getId(),name));//
					
					listSubCategoriesUsedInForm(categoriesUsedInForm,subCategory,1);
				}
			}
		}
		
		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0;i<newSubLevel;i++) {
				name += "--";
			}
			name +=subCategory.getName();
			categoriesUsedInForm.add(new Category(subCategory.getId(),name));//
			listSubCategoriesUsedInForm(categoriesUsedInForm,subCategory, newSubLevel);
		}
		
	}
	
	 public Category get(Integer id) throws CategoryNotFoundException {
	    	try {
	    		return repo.findById(id).get();
	    	} catch(NoSuchElementException ex) {
	    		throw new CategoryNotFoundException("Could not find any category with ID: "+  id);
	    	}
	    }
	
}
