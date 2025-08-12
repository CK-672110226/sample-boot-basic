package th.mfu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public ProductReview createReview(ProductReview review) {
        validateReview(review);
        return productReviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productReviewRepository.findByProduct(product);
    }

    public Optional<ProductReview> getReviewById(Long id) {
        return productReviewRepository.findById(id);
    }

    public ProductReview updateReview(Long id, ProductReview updatedReview) {
        ProductReview existing = productReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        existing.setRating(updatedReview.getRating());
        
        return productReviewRepository.save(existing);
    }

    public void deleteReview(Long id) {
        productReviewRepository.deleteById(id);
    }

    public Double calculateAverageRating(Long productId) {
        Product product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productReviewRepository.calculateAverageRating(product);
    }

    private void validateReview(ProductReview review) {
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (review.getProduct() == null) {
            throw new IllegalArgumentException("Product is required");
        }
    }
}
