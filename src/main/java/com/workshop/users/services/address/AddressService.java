package com.workshop.users.services.address;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.exceptions.NotFoundAddressException;
import com.workshop.users.exceptions.RegisterException;
import org.springframework.stereotype.Service;


@Service
public interface AddressService {
    AddressDto addAddress(AddressDto address) throws RegisterException;
    AddressDto getAddressById(Long id) throws NotFoundAddressException;
    AddressDto updateAddress(Long id, AddressDto addressDto) throws NotFoundAddressException;

}
