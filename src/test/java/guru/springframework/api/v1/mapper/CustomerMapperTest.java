package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerMapperTest {

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Jack";
    private static final String LAST_NAME = "Doe";
    private static final String CUSTOMER_URI = "/api/v1/customers/1";

    private CustomerMapper mapper = CustomerMapper.INSTANCE;

    @Test
    public void shouldMapCustomerToCustomerDTO() {
        // given
        Customer customer = new Customer(FIRST_NAME, LAST_NAME);
        customer.setId(ID);

        // when
        CustomerDTO customerDTO = mapper.customerToCustomerDTO(customer);

        // then
        assertEquals(FIRST_NAME, customerDTO.getFirstName());
        assertEquals(LAST_NAME, customerDTO.getLastName());
        assertEquals(CUSTOMER_URI, customerDTO.getCustomerUrl());
    }
}