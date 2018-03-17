package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.domain.Vendor;
import org.junit.Test;

import static org.junit.Assert.*;

public class VendorMapperTest {

    private final static String NAME = "Test vendor";
    private final static Long ID = 1L;
    private final static String VENDOR_URL = "/api/v1/vendors/" + ID;

    private VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void shouldMapVendorToVendorDTO() {
        // given
        Vendor vendor = new Vendor(NAME);
        vendor.setId(ID);

        // when
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        // then
        assertEquals(NAME, vendorDTO.getName());
        assertEquals(VENDOR_URL, vendorDTO.getVendorUrl());
    }

    @Test
    public void shouldMapVendorDTOToVendor() {
        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        // when
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        // then
        assertEquals(NAME, vendor.getName());
    }
}