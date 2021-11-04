package com.shopme.feedback;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Feedback;

@Service
@Transactional
public class FeedbackService {
    public static final int FEEDBACK_PER_PAGE = 10;

    @Autowired
    private FeedbackRepository repository;
//
//    public void listFeedbackByProduct(Product product, int pageNum, PagingAndSortingHelper helper){
//        Pageable pageable= helper.createPageable(FEEDBACK_PER_PAGE,pageNum);
//
//        // Page<Feedback> page = repository.findAllByProduct(product,pageable);
//        // helper.updateModelAttributes(pageNum,page);
//    }

//     public List<Feedback> findFeedbackByOrder(int orderId){
//         return repository.findFeedbackByOrderId(orderId);
//     }

    public Feedback getFeedback(Integer productId, Integer orderDetailId) {
    	return repository.getByProducIdAndDetailId(productId, orderDetailId);
    }
 

    public Feedback saveFeedback(Feedback feedback){
        if(feedback.getId()==null){
            feedback.setCreatedAt(new Date());
        }
        feedback.setUpdateAt(new Date());
        feedback.setRating(0);
        return repository.save(feedback);
    }
}
