package com.shopme.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Feedback;
import com.shopme.common.entity.product.Product;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

		@Query("SELECT f FROM Feedback f WHERE f.product.id = ?1 AND f.orderDetail.id = ?2")
		public Feedback getByProducIdAndDetailId(Integer productId, Integer orderDetailId);
}
