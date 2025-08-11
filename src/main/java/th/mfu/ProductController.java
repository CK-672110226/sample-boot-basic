package th.mfu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository prodRepo;

    // GET for a product
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){
        if(!prodRepo.existsById(id))
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        Optional<Product> product = prodRepo.findById(id);
        return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
    }

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<Collection> getAllProducts(){
        return new ResponseEntity<Collection>(prodRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/products/description/{infix}")
    public ResponseEntity<Collection> searchByDescription(@PathVariable String infix){
        return new ResponseEntity<Collection>(prodRepo.findByDescriptionContaining(infix), HttpStatus.OK);
    }

    @GetMapping("/products/price")
    public ResponseEntity<Collection> listByPrice(){
        return new ResponseEntity<Collection>(prodRepo.findByOrderByPrice(), HttpStatus.OK);
    }

    // POST for creating a product
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        prodRepo.save(product);
        return new ResponseEntity<String>("Product created", HttpStatus.CREATED);
    }

    // DELETE for deleting a product by id
    @DeleteMapping("products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        prodRepo.deleteById(id);
        return new ResponseEntity<String>("Product deleted", HttpStatus.NO_CONTENT);
    }

}