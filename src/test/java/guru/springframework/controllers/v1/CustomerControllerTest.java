package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controllers.RestResponseEntityExceptionHandler;
import guru.springframework.services.CustomerService;
import guru.springframework.services.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static guru.springframework.controllers.v1.CustomerController.BASE_URL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractRestControllerTest {

    public static final String FIRST_NAME = "Jack";
    public static final String LAST_NAME = "Doe";
    private final static long ID = 1L;
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void shouldListAllCustomers() throws Exception {
        // given
        CustomerDTO customer1 = new CustomerDTO();
        CustomerDTO customer2 = new CustomerDTO();
        CustomerDTO customer3 = new CustomerDTO();

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2, customer3));

        // when
        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(3)));
    }

    @Test
    public void shouldGetCustomerById() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerUrl(BASE_URL + ID);

        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        // when
        mockMvc.perform(get(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldCreateNewCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);

        CustomerDTO savedDTO = new CustomerDTO();
        savedDTO.setFirstName(customerDTO.getFirstName());
        savedDTO.setLastName(customerDTO.getLastName());
        savedDTO.setCustomerUrl(BASE_URL + ID);

        when(customerService.createNewCustomer(customerDTO)).thenReturn(savedDTO);

        //when
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo("Jack")))
                .andExpect(jsonPath("$.customer_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldUpdateCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);

        CustomerDTO updatedDTO = new CustomerDTO();
        updatedDTO.setFirstName(customerDTO.getFirstName());
        updatedDTO.setLastName(customerDTO.getLastName());
        updatedDTO.setCustomerUrl(BASE_URL + ID);

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(updatedDTO);

        // when
        mockMvc.perform(put(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.customer_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldPatchCustomer() throws Exception {
        // given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName(FIRST_NAME);

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(FIRST_NAME);
        returnDTO.setLastName(LAST_NAME);
        returnDTO.setCustomerUrl(BASE_URL + ID);

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        // when
        mockMvc.perform(patch(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME)))
                .andExpect(jsonPath("$.customer_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldDeleteCustomerById() throws Exception {
        // when
        mockMvc.perform(delete(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(anyLong());
    }

    @Test
    public void shouldReturnNotFoundIfIdNotExist() throws Exception {
        // given
        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        // when
        mockMvc.perform(get(BASE_URL + 110L)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBedRequestIfIdInvalid() throws Exception {
        // when
        mockMvc.perform(get(BASE_URL + "Invalid_ID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}