package com.review.dtos;

import com.review.entity.Company;
import com.review.entity.Review;

import java.util.List;

public class ReviewWithCompanyDTO {
    private Company company;
    private List<Review> review;

    public ReviewWithCompanyDTO(Company company, List<Review> review) {
        this.company = company;
        this.review = review;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }
}
