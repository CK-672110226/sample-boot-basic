package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SaleOrderRepository extends CrudRepository<SaleOrder, Long> {
    // Find all orders for a given customer
    List<SaleOrder> findByCustomerId(Long customerId);
}