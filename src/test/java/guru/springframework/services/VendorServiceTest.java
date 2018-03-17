package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static guru.springframework.controllers.v1.VendorController.BASE_URL;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VendorServiceTest {

    private final static String NAME = "Test vendor";
    private final static Long ID = 1L;

    @Mock
    private VendorRepository vendorRepository;

    private VendorService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        service = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    public void shouldReturnAllVendors() {
        // given
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());
        given(vendorRepository.findAll()).willReturn(vendors);

        // when
        List<VendorDTO> vendorDTOS = service.getAllVendors();

        // then
        then(vendorRepository).should(times(1)).findAll();
        assertThat(vendorDTOS.size(), is(equalTo(3)));
    }

    @Test
    public void shouldReturnVendorById() {
        // given
        Vendor vendor = new Vendor();
        vendor.setName(NAME);
        vendor.setId(ID);

        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));

        // when
        VendorDTO vendorDTO = service.getVendorById(ID);

        // then
        then(vendorRepository).should(times(1)).findById(anyLong());
        assertThat(vendorDTO.getName(), is(equalTo(NAME)));
        assertThat(vendorDTO.getVendorUrl(), is(equalTo(BASE_URL + ID)));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldThrowRNFEIfIdNotFound() {
        //given
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        VendorDTO vendorDTO = service.getVendorById(ID);

        // then
        then(vendorRepository).should(times(1)).findById(anyLong());
    }

    @Test
    public void shouldCreateNewVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(NAME);
        savedVendor.setId(ID);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO newVendorDTO = service.createNewVendor(vendorDTO);

        // then
        then(vendorRepository).should().save(any(Vendor.class));
        assertThat(newVendorDTO.getVendorUrl(), containsString(ID.toString()));
        assertThat(newVendorDTO.getName(), is(equalTo(NAME)));
    }

    @Test
    public void shouldUpdateVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        Vendor updatedVendor = new Vendor();
        updatedVendor.setName(NAME);
        updatedVendor.setId(ID);

        given(vendorRepository.save(any(Vendor.class))).willReturn(updatedVendor);

        //when
        VendorDTO updatedVendorDTO = service.updateVendor(ID, vendorDTO);

        // then
        then(vendorRepository).should().save(any(Vendor.class));
        assertThat(updatedVendorDTO.getName(), is(equalTo(NAME)));
        assertThat(updatedVendorDTO.getVendorUrl(), containsString(ID.toString()));
    }

    @Test
    public void shouldDeleteVendorById() {
        // when
        service.deleteVendor(ID);

        // then
        then(vendorRepository).should(times(1)).deleteById(ID);
    }
}