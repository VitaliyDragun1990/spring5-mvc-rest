package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VendorMapper {

    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    default VendorDTO vendorToVendorDTO(Vendor vendor) {
        VendorDTO vendorDTO = new VendorDTO();

        if (vendor == null) {
            return null;
        }

        vendorDTO.setName(vendor.getName());
        vendorDTO.setVendorUrl(VendorController.BASE_URL + vendor.getId());

        return vendorDTO;
    }

    Vendor vendorDTOToVendor(VendorDTO vendorDTO);
}
