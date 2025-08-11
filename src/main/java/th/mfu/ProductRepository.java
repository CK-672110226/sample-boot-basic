package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{
    List<Product> findAll();
    List<Product> findByOrderByPrice();
    List<Product> findByDescriptionContaining(String infix);

}
