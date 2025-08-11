package th.mfu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    private CustomerController controller;

    @Test
    public void createAndGet() {
        // create a customer
        Customer cust = new Customer();
        cust.setName("Dummy Dummy");
        cust.setAddress("123 Main st.");
        cust.setEmail("dummy@email.com");
        cust.setPhone("1111111");
        ResponseEntity<String> response = controller.createCustomer(cust);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // get that customer
        ResponseEntity<Collection> getResponse = controller.getAllCustomers();
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(11, getResponse.getBody().size());

    }

     @Test
    public void createAndDelete() {
        // Get the initial number of customers
        ResponseEntity<Collection> initialGetResponse = controller.getAllCustomers();
        assertEquals(HttpStatus.OK, initialGetResponse.getStatusCode());
        int initialSize = initialGetResponse.getBody().size();

        // Create a new customer to be deleted
        Customer cust = new Customer();
        cust.setName("Delete Me");
        cust.setAddress("456 Delete Lane");
        cust.setEmail("delete@me.com");
        cust.setPhone("2222222");
        ResponseEntity<String> createResponse = controller.createCustomer(cust);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Verify the customer was created (the size of the collection should be initialSize + 1)
        ResponseEntity<Collection> getResponseAfterCreate = controller.getAllCustomers();
        assertEquals(HttpStatus.OK, getResponseAfterCreate.getStatusCode());
        assertEquals(initialSize + 1, getResponseAfterCreate.getBody().size());

        // Find the newly created customer to get its ID
        List<Customer> customersAfterCreate = (List<Customer>) getResponseAfterCreate.getBody();
        Customer newCustomer = customersAfterCreate.stream()
                                                    .filter(c -> "Delete Me".equals(c.getName()))
                                                    .findFirst()
                                                    .orElse(null);
        assertNotNull(newCustomer, "Newly created customer should not be null");
        Long customerId = newCustomer.getId();

        // Delete the customer
        ResponseEntity<String> deleteResponse = controller.deleteCustomer(customerId);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Verify the customer was deleted (the size of the collection should be back to initialSize)
        ResponseEntity<Collection> getResponseAfterDelete = controller.getAllCustomers();
        assertEquals(HttpStatus.OK, getResponseAfterDelete.getStatusCode());
        assertEquals(initialSize, getResponseAfterDelete.getBody().size());
    }
}
