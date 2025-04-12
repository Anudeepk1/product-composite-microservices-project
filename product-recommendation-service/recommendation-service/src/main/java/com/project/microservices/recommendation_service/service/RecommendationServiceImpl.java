package com.project.microservices.recommendation_service.service;

import com.library.common_service.dto.RecommendationDto;
import com.library.common_service.api.core.recommendation.RecommendationService;
import com.library.common_service.utils.core.http.ServiceUtil;
import com.library.common_service.utils.exceptions.InvalidInputException;
import com.project.microservices.recommendation_service.entity.Recommendation;
import com.project.microservices.recommendation_service.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private RecommendationRepository recRepo;
    private ServiceUtil serviceUtil;
    private RecommendationMapper mapper;

    @Autowired
    RecommendationServiceImpl(RecommendationRepository recRepo, ServiceUtil serviceUtil, RecommendationMapper mapper){
        this.recRepo = recRepo;
        this.serviceUtil = serviceUtil;
        this.mapper = mapper;

    }
    @Override
    public List<RecommendationDto> getRecommendations(int productID) {
        LOG.info("Fetching Recommendation details for productId:{}", productID);
        if(productID < 1) throw new InvalidInputException("Invalid ProductId: " + productID);
        List<Recommendation> recommendations = recRepo.findByProductId(productID);
        LOG.info("Fetched {} product-recommendations for productId:{}", recommendations.size(),
                productID);

        List<RecommendationDto> response = mapper.entityListToDtoList(recommendations);
        if(!response.isEmpty()){
            response.get(0).setServiceAddress(serviceUtil.getServiceAddress());
        }
        return response;
    }

    @Override
    public void createRecommendation(RecommendationDto body) {
        Recommendation recommendation = mapper.dtoToEntity(body);
        Recommendation newRec = recRepo.save(recommendation);
        LOG.info("product-recommendation of ID-{} saved for productId:{}",
                newRec.getRecommendationId(), body.getProductId());
        mapper.entityToDto(newRec);

    }

    @Override
    public void deleteRecommendation(Integer productId) {
        recRepo.deleteAll(recRepo.findByProductId(productId));
        LOG.info("product-recommendation deleted for productId:{}", productId);

    }
}
