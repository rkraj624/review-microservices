package com.review;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.dtos.ReviewWithCompanyDTO;
import com.review.entity.Company;
import com.review.entity.Review;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private static String BASIC_URL="http://COMPANY-SERVICE:8080/companies/";
    private ReviewRepository reviewRepository;
    private RestTemplate restTemplate;

    public ReviewService(ReviewRepository reviewRepository, RestTemplate restTemplate) {
        this.reviewRepository = reviewRepository;
        this.restTemplate = restTemplate;
    }

    
    public List<Review> getAllReviews(String companyId) {
        return reviewRepository.findByCompanyId(companyId);

    }

    
    public boolean addReview(Review review) {
        try {
            Company company = getCompany(review.getCompanyId());
            if(company != null){
                review.setCompanyId(company.getId());
                reviewRepository.save(review);
                return true;
            }
            return false;
        }catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    
    public Review getReview(String companyId, String  reviewId) {
       return reviewRepository.findByCompanyId(companyId).stream().filter(review -> review.getId().equalsIgnoreCase(reviewId)).findFirst().orElse(null);
    }

    
    public boolean updateReview(String companyId, String reviewId, Review review) {
       try{
           Company companyById = getCompany(companyId);
           if(companyById != null){
               review.setCompanyId(companyById.getId());
               review.setId(reviewId);
               reviewRepository.save(review);
               return true;
           }
           return false;
       }catch (Exception exception){
           exception.printStackTrace();
           return false;
       }
    }

    
    public boolean deleteReview(String companyId, String reviewId) {
        try{
            Company company = getCompany(companyId);
            boolean review = reviewRepository.existsById(reviewId);
            if(company != null && review){
                Review reviewById = reviewRepository.findById(reviewId).orElse(null);
                company.getReviews().remove(reviewById);
                updateCompany(companyId, company);
                reviewRepository.deleteById(reviewId);
                return true;
            }
            return false;
        }catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    private boolean updateCompany(String companyId, Company company) throws JsonProcessingException {
        Map<String, Object> reqBody = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(Map.of("Content-Type", "application/json"
                , "Accept", "*/*"));
        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(company), headers);

        String url = BASIC_URL+ companyId;

        ResponseEntity<String> httpResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        return httpResponse.getStatusCode().equals("200");
    }

    private Company getCompany(String companyId) {
        return restTemplate.getForObject(BASIC_URL+companyId, Company.class);
    }
}
