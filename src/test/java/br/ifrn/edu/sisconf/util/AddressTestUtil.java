package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;

public class AddressTestUtil {
    public static AddressRequestDTO createValidAddressRequestDTO() {
        return new AddressRequestDTO(
            "Rua Teste",
            "10000-000",
            "Bairro Teste",
            90,
            1L
        );
    }
}
