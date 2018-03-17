package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    private final static String FIRST_NAME = "Anna";
    private final static String LAST_NAME = "Smith";
    private final static long ID = 1L;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void shouldReturnAllCustomers() {
        // given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        // when
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        // then
        assertEquals(3, customerDTOS.size());
    }

    @Test
    public void shouldReturnCustomerById() {
        // given
        Customer customer  = new Customer(FIRST_NAME, LAST_NAME);
        customer.setId(ID);

        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));

        //when
        CustomerDTO customerDTO = customerService.getCustomerById(ID);

        // then
        assertEquals(FIRST_NAME, customerDTO.getFirstName());
        assertEquals(LAST_NAME, customerDTO.getLastName());
    }

    @Test
    public void shouldCreateNewCustomer() {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);

        Customer savedCustomer = new Customer();
        savedCustomer.setId(ID);
        savedCustomer.setFirstName(FIRST_NAME);
        savedCustomer.setLastName(LAST_NAME);

        when(customerRepository.save(any())).thenReturn(savedCustomer);

        // when
        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);

        // then
        assertEquals(savedCustomer.getFirstName(),savedDTO.getFirstName());
        assertEquals("/api/v1/customers/" + savedCustomer.getId(), savedDTO.getCustomerUrl());
    }

    @Test
    public void shouldUpdateCustomer() {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(ID);
        updatedCustomer.setFirstName(FIRST_NAME);
        updatedCustomer.setLastName(LAST_NAME);

        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // when
        CustomerDTO updatedDTO = customerService.updateCustomer(ID, customerDTO);

        // then
        assertEquals(customerDTO.getFirstName(), updatedDTO.getFirstName());
        assertEquals("/api/v1/customers/" + updatedCustomer.getId(), updatedDTO.getCustomerUrl());
    }

    @Test
    public void shouldDeleteCustomerById() {
        // given
        // when
        customerRepository.deleteById(ID);

        // then
        verify(customerRepository, times(1)).deleteById(ID);
    }
}