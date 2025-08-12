package th.mfu;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ProductReviewController {

    @Autowired
    private ProductReviewRepository reviewRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductRepository productRepo;

    @PostMapping
    public ResponseEntity<ProductReview> createReview(@RequestBody ProductReview review) {
        // Validate customer
        if (review.getCustomer() == null || review.getCustomer().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Customer> customerOpt = customerRepo.findById(review.getCustomer().getId());
        if (!customerOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validate product
        if (review.getProduct() == null || review.getProduct().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Product> productOpt = productRepo.findById(review.getProduct().getId());
        if (!productOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Set full objects and server-side data
        review.setCustomer(customerOpt.get());
        review.setProduct(productOpt.get());
        review.setReviewDate(LocalDate.now());

        ProductReview savedReview = reviewRepo.save(review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }
}