package com.workshop.users.services.address;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.exceptions.AddressServiceException;
import com.workshop.users.exceptions.NotFoundAddressException;
import com.workshop.users.exceptions.RegisterException;
import com.workshop.users.model.AddressEntity;
import com.workshop.users.repositories.AddressDAORepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressDAORepository addressDAORepository;



    @Override
    public AddressDto addAddress(AddressDto address) throws RegisterException {
        if (address.getId()!=null){
            throw new AddressServiceException("You can't add an address with id");
        }
        return AddressEntity.fromEntity(addressDAORepository.save(AddressDto.toEntity(address)));
    }

    @Override
    public AddressDto getAddressById(Long id) throws NotFoundAddressException {
        isNotNull(id);
        return AddressEntity.fromEntity(addressDAORepository.findById(id).orElseThrow());
    }



    private void isNotNull(Long id) {
        if (id == null){
            throw new NotFoundAddressException("Request not valid. The address with the id " + id  + " is null");
        }
    }


    @Override
    public AddressDto updateAddress(Long id, AddressDto updatedAddressDto) throws NotFoundAddressException{
        AddressEntity addressEntity = addressDAORepository.findById(updatedAddressDto.getId())
                .orElseThrow(() -> new NotFoundAddressException("The address with the id " + id  + " was not found."));
        addressEntity.setCityName(updatedAddressDto.getCityName());
        addressEntity.setZipCode(updatedAddressDto.getZipCode());
        addressEntity.setStreet(updatedAddressDto.getStreet());
        addressEntity.setNumber(updatedAddressDto.getNumber());
        addressEntity.setDoor(updatedAddressDto.getDoor());

        return AddressEntity.fromEntity(addressDAORepository.save(addressEntity));
    }
}