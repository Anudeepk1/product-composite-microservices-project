package com.project.microservices.review_service.service;

import com.library.common_service.api.core.review.ReviewService;
import com.library.common_service.dto.ReviewDto;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.InvalidInputException;
import com.project.microservices.review_service.entity.Review;
import com.project.microservices.review_service.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static java.util.logging.Level.FINE;

@RestController
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepo;
    private final ServiceUtil serviceUtil;
    private final ReviewMapper mapper;
    private final Scheduler jdbcScheduler;

    @Autowired
    ReviewServiceImpl(ReviewRepository reviewRepo, ServiceUtil serviceUtil, ReviewMapper mapper, Scheduler jdbcScheduler) {
        this.reviewRepo = reviewRepo;
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;
        this.jdbcScheduler = jdbcScheduler;
    }

    @Override
    public Flux<ReviewDto> getReviews(int productId) {
        LOG.info("Fetching review details for productId:{}", productId);
        if (productId < 1) throw new InvalidInputException("Invalid ProductId: " + productId);

        return Mono.fromCallable(() -> this.internalGetReview(productId))
                .flatMapMany(Flux::fromIterable)
                .log(LOG.getName(), FINE)
                .subscribeOn(jdbcScheduler);
    }

    private List<ReviewDto> internalGetReview(int productId) {
        List<Review> reviews = reviewRepo.findByProductId(productId);
        LOG.info("Product-review details {} fetched successfully", reviews.size());
        List<ReviewDto> list = mapper.entityListToDtoList(reviews);
        list.forEach(l -> l.setServiceAddress(serviceUtil.getServiceAddress()));
        return list;
    }

    @Override
    public Mono<ReviewDto> createReview(ReviewDto body) {
        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }
        return Mono.fromCallable(() -> this.internalCreateReview(body))
                .subscribeOn(jdbcScheduler);
    }

    private ReviewDto internalCreateReview(ReviewDto body) {
        try {
            Review review = mapper.dtoToEntity(body);
            Review newReview = reviewRepo.save(review);
            LOG.info("Product-review of ID-{}, saved successfully of productId:{}",
                    newReview.getReviewId(), body.getProductId());
            return mapper.entityToDto(newReview);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
        }
    }

    @Override
    public Mono<Void> deleteReview(Integer productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return Mono.fromRunnable(() -> this.internalDeleteReview(productId))
                .subscribeOn(jdbcScheduler).then();

    }

    private void internalDeleteReview(Integer productId) {
        LOG.info("product-reviews deleting for productId:{}", productId);
        reviewRepo.deleteAll(reviewRepo.findByProductId(productId));
    }
}
