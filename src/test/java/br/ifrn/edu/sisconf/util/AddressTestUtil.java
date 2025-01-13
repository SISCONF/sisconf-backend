package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.AddressResponseDTO;

public class AddressTestUtil {
    public static Address createValidAddress() {
        return new Address(
            "Rua Teste",
            "10000-000",
            "Bairro Teste",
            90,
            new City("Cidade do Teste", new CountryState("Estado do Teste", "ET"))
        );
    }

    public static AddressRequestDTO createValidAddressRequestDTO() {
        return new AddressRequestDTO(
            "Rua Teste",
            "10000-000",
            "Bairro Teste",
            90,
            1L
        );
    }

    public static AddressRequestDTO toValidRequestDTO(Address address) {
        return new AddressRequestDTO(
            address.getStreet(),
            address.getZipCode(),
            address.getNeighbourhood(),
            address.getNumber(),
            address.getCity().getId()
        );
    }

    public static AddressResponseDTO toResponseDTO(Address address) {
        return new AddressResponseDTO(
            address.getId(),
            address.getStreet(),
            address.getZipCode(),
            address.getNeighbourhood(),
            address.getNumber(),
            address.getCity().getId()
        );
    }
}
