package com.shopme.admin.user;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository repo;
    
    @Test
    public void testCreatRestRoles() {
    	Role roleAdmin = new Role("Admin","manage everything");
    	Role roleSalesperson = new Role("Salesperson","manage product price,"
    			+ " customer, shipping, orders and sales report");
    	Role roleEditor = new Role("Editor","manage categories, brands, "
    			+ "products, articles and menus");
    	Role roleShipper = new Role("Shipper","view product, view orders and "
    			+ "update order satatus");
    	Role roleAssistant = new Role("Assistant","manage questions and reviews");
    	repo.saveAll(List.of(roleAdmin,roleSalesperson,roleEditor,roleShipper,roleAssistant));
    }
    
}
