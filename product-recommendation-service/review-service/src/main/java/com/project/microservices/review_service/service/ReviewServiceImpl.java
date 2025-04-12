package com.project.microservices.review_service.service;

import com.library.common_service.dto.ReviewDto;
import com.library.common_service.api.core.review.ReviewService;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.InvalidInputException;
import com.mysql.cj.log.Log;
import com.project.microservices.review_service.entity.Review;
import com.project.microservices.review_service.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private ReviewRepository reviewRepo;
    private ServiceUtil serviceUtil;
    private ReviewMapper mapper;

    @Autowired
    ReviewServiceImpl(ReviewRepository reviewRepo, ServiceUtil serviceUtil, ReviewMapper mapper){
        this.reviewRepo = reviewRepo;
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;

    }

    @Override
    public List<ReviewDto> getReview(int productId) {
        LOG.info("Fetching review details for productId:{}", productId);
        if(productId < 1) throw new InvalidInputException("Invalid ProductId: " + productId);
        List<Review> reviews = reviewRepo.findByProductId(productId);
        LOG.info("Product-review details {} fetched successfully", reviews.size());
        List<ReviewDto> response = mapper.entityListToDtoList(reviews);
        if(!response.isEmpty()){
            response.get(0).setServiceAddress(serviceUtil.getServiceAddress());
        }
        return response;
    }

    @Override
    public void createReview(ReviewDto body) {
        Review review = mapper.dtoToEntity(body);
        Review newReview = reviewRepo.save(review);
        LOG.info("Product-review of ID-{}, saved successfully of productId:{}",
                newReview.getReviewId(), body.getProductId());
        mapper.entityToDto(newReview);
    }

    @Override
    public void deleteReview(Integer productId) {
        reviewRepo.deleteAll(reviewRepo.findByProductId(productId));
        LOG.info("product-reviews deleted successfully of productId:{}", productId);
    }
}
