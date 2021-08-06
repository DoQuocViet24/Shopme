package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {
	@Autowired
	private CategoryRepository repo;
	private static final int ROOT_CATEGORY_PER_PAGE = 4;
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir){
		Sort sort =  Sort.by("name");
		
		if(sortDir.equals("desc")) {
			sort = sort.descending();
		}else {
			sort = sort.ascending();
		}
		Pageable pageable = PageRequest.of(pageNum-1,ROOT_CATEGORY_PER_PAGE,sort);
		
		Page<Category> pageCategories = repo.findRootCategories(pageable);
		List<Category> rootCategories  =pageCategories.getContent();
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		return listHierarchicalCategories(rootCategories,sortDir) ;
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories =  new ArrayList<>();
		
		for(Category rootCategory: rootCategories) {
			hierarchicalCategories.add(Category.copy(rootCategory));
			
			Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);
			
			for(Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copy(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories,subCategory,1,sortDir);
			}
		}
		
		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel, String sortDir) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);
		
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0;i<newSubLevel;i++) {
				name += "--";
			}
			name +=subCategory.getName();
			hierarchicalCategories.add(Category.copy(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories,subCategory, newSubLevel,sortDir);
		}
		
		
	}

	public Category save(Category category) {
		return repo.save(category);
	}
	//form
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>();
		Iterable<Category> categoriesInDB	= repo.findRootCategories(Sort.by("name").ascending());
		for(Category category : categoriesInDB) {
				categoriesUsedInForm.add(new Category(category.getId(),category.getName()));//
				Set<Category> children = sortSubCategories(category.getChildren());
				for(Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesUsedInForm.add(new Category(subCategory.getId(),name));//
					
					listSubCategoriesUsedInForm(categoriesUsedInForm,subCategory,1);
				}
		}
		
		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm,Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		
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
	//
	 public Category get(Integer id) throws CategoryNotFoundException {
	    	try {
	    		return repo.findById(id).get();
	    	} catch(NoSuchElementException ex) {
	    		throw new CategoryNotFoundException("Could not find any category with ID: "+  id);
	    	}
	    }
	 
	 public String checkUnique(Integer id, String name, String alias) {
		 boolean isCreateNew = (id == null || id == 0);
		 Category categoryByName = repo.findByName(name);
		 if(isCreateNew) {
			 if(categoryByName != null) {
				 return "DuplicateName";
			 }else {
				 Category categoryByAlias = repo.findByAlias(alias);
				 if(categoryByAlias != null) {
					 return "DuplicateAlias";
				 }
			 }
		 }else{
			if(categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryByAlias = repo.findByAlias(alias);
			if(categoryByAlias != null  && categoryByName.getId() != id) {
				 return "DuplicateAlias";
			 }
		 }
		 
		 return "OK";
	 }
	 private SortedSet<Category> sortSubCategories(Set<Category> children){
		 return sortSubCategories(children,"asc");
	 }
	 private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
		 SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				if(sortDir.equals("asc")) {
				 return o1.getName().compareTo(o2.getName());
				}else {
					 return o2.getName().compareTo(o1.getName());
				}
			}
			 
			 
		});
		 sortedChildren.addAll(children);
		 return sortedChildren;
	 }

	public void updateCategoryStatus(Integer id, boolean enabled) {
	      repo.updateEnabledStatus(id, enabled);	
	}

	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = repo.countById(id);
    	if(countById == null || countById ==0) {
    		throw new CategoryNotFoundException("Could not find any user with ID: "+  id);
    	}
		repo.deleteById(id);
	}
}
