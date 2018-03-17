package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.services.ResourceNotFoundException;
import guru.springframework.services.VendorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;

import static guru.springframework.controllers.v1.VendorController.BASE_URL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VendorController.class})
public class VendorControllerTest extends AbstractRestControllerTest {

    private final static String NAME = "New Vendor Name";
    private final static Long ID = 1L;

    @MockBean // provided by Spring Context
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;

    @Autowired  // provided by Spring Context
    private MockMvc mockMvc;


    @Test
    public void shouldListAllVendors() throws Exception {
        // given
        VendorDTO vendorDTO1 = new VendorDTO();
        VendorDTO vendorDTO2 = new VendorDTO();
        VendorDTO vendorDTO3 = new VendorDTO();

        given(vendorService.getAllVendors()).willReturn(Arrays.asList(vendorDTO1, vendorDTO2, vendorDTO3));

        // when
        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(3)));
    }

    @Test
    public void shouldGetVendorById() throws Exception {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);
        vendorDTO.setVendorUrl(BASE_URL + ID);

        given(vendorService.getVendorById(anyLong())).willReturn(vendorDTO);

        // when
        mockMvc.perform(get(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendor_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldCreateNewVendor() throws Exception {
        // given
        VendorDTO dto = new VendorDTO();
        dto.setName(NAME);

        VendorDTO savedDTO = new VendorDTO();
        savedDTO.setName(dto.getName());
        savedDTO.setVendorUrl(BASE_URL + ID);

        given(vendorService.createNewVendor(dto)).willReturn(savedDTO);

        // when
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldUpdateExistingCustomer() throws Exception {
        testUpdateCustomer(true);
    }

    @Test
    public void shouldPatchCustomer() throws Exception {
        testUpdateCustomer(false);
    }

    private void testUpdateCustomer(boolean isPut) throws Exception {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        VendorDTO updatedDTO = new VendorDTO();
        updatedDTO.setName(vendorDTO.getName());
        updatedDTO.setVendorUrl(BASE_URL + ID);

        given(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).willReturn(updatedDTO);
        given(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).willReturn(updatedDTO);

        MockHttpServletRequestBuilder request;
        if (isPut) {
            request = put(BASE_URL + ID);
        } else {
            request = patch(BASE_URL + ID);
        }
        // when
        mockMvc.perform(request
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(BASE_URL + ID)));
    }

    @Test
    public void shouldDeleteVendorById() throws Exception {
        // when
        mockMvc.perform(delete(BASE_URL + ID)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk());

        then(vendorService).should(times(1)).deleteVendor(anyLong());
    }

    @Test
    public void shouldReturnNotFountIfNotExist() throws Exception {
        // given
        given(vendorService.getVendorById(anyLong())).willThrow(ResourceNotFoundException.class);

        // when
        mockMvc.perform(get(BASE_URL + ID)
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