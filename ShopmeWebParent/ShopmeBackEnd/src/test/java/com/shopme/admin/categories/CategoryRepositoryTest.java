package com.shopme.admin.categories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
     @Autowired
     private CategoryRepository repo;
     
     @Test 
     public void testCreateRootCategory() {
    	 Category category = new Category("Electronics");
    	 Category saveCategory = repo.save(category);
    	 
    	 assertThat(saveCategory.getId()).isGreaterThan(0);
     }
     
     @Test 
     public void testCreateSubCategory() {
    	 Category parent = new Category(1);
    	 Category subCategory = new Category("PC", parent);
    	 Category savedCategory = repo.save(subCategory);
    	 assertThat(savedCategory.getId()).isGreaterThan(0);
     }
     
     @Test
     public void testGetCategory() {
    	 Category category = repo.findById(3).get();
    	 Set<Category> children = category.getChildren();
			for(Category subCategory : children) {
				System.out.println( subCategory.getName());
				
			}
    	 
     }
     
     @Test
     public void testPrintCategories(){
 		Iterable<Category> categories	= repo.findAll();
 		for(Category category : categories) {
 			if(category.getParent() == null) {
                System.out.println(category.getName());
 				Set<Category> children = category.getChildren();
 				for(Category subCategory : children) {
 					System.out.println("--" + subCategory.getName());
 					
 					printChilden(subCategory,1);
 				}
 			}
 		}
 		
 	}

 	private void printChilden(Category parent, int subLevel) {
 		int newSubLevel = subLevel + 1;
 		Set<Category> children = parent.getChildren();
 		
 		for(Category subCategory : children) {
 			String name = "";
 			for(int i = 0;i<newSubLevel;i++) {
 				name += "--";
 			}
 			name +=subCategory;
 			System.out.println(name);
 		    printChilden(subCategory, newSubLevel);
 	}
  }
}
