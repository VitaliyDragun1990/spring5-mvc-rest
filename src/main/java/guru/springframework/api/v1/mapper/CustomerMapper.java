package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

     CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

     default CustomerDTO customerToCustomerDTO(Customer customer) {
         CustomerDTO customerDTO = new CustomerDTO();
         if (customer.getFirstName() != null) {
             customerDTO.setFirstName(customer.getFirstName());
         }
         if (customer.getLastName() != null) {
             customerDTO.setLastName(customer.getLastName());
         }
         customerDTO.setCustomerUrl("/api/v1/customers/" + customer.getId());

         return customerDTO;
     }
}
