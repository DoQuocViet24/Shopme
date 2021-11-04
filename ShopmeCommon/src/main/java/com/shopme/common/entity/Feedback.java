package com.shopme.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="feedbacks")
public class Feedback extends IdBasedEntity {

    @OneToOne
    @JoinColumn(name = "orderDetail_id")
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

//    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<FeedbackImage> images = new HashSet<>();

    private String message;

    private int rating;

    private Date createdAt;
    private Date updateAt;
}
