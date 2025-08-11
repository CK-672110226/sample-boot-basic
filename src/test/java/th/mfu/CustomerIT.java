package th.mfu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;
public class CustomerIT {

    private static Client client;

    @BeforeClass
    public static void createClient() {
        // Use ClientBuilder to create a new client that can be used to create
        // connections to the Web service.
        client = ClientBuilder.newClient();
    }

    // test for posting a new customer
    @Test
    public void testCreateCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("John Denver");
        customer.setAddress("123 Main st.");
        customer.setEmail("john@email.com");
        customer.setPhone("0688888888");
        customer.setBirthday(LocalDate.of(2002, 5, 6));

        Builder builder = client.target("http://localhost:8080/customers").request();
        Response response = builder.post(Entity.json(customer));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateDeleteAndGet() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("John Denver");
        customer.setAddress("123 Main st.");
        customer.setEmail("john@email.com");
        customer.setPhone("0688888888");
        customer.setBirthday(LocalDate.of(2002, 5, 6));

        Builder builder = client.target("http://localhost:8080/customers").request();
        Response response = builder.post(Entity.json(customer));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Delete the customer
        Response deleteResponse = client.target("http://localhost:8080/customers/1").request().delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());

        // Try to get the deleted customer
        Response getDeletedCustomerResponse = client.target("http://localhost:8080/customers/1").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getDeletedCustomerResponse.getStatus());
    }

}