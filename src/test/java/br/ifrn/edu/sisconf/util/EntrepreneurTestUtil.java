package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;

public class EntrepreneurTestUtil {
    public static Entrepreneur createValidEntrepreneur() {
        var entrepreneur = new Entrepreneur(
            "Nome de Negócio Teste",
            PersonTestUtil.createValidPerson("12.345.678/9101-11"),
            null
        );
        entrepreneur.getPerson().setEntrepreneur(entrepreneur);
        return entrepreneur;
    }

    public static EntrepreneurRequestDTO toValidRequestDTO(Entrepreneur entrepreneur) {
        return new EntrepreneurRequestDTO(
            entrepreneur.getBusinessName(),
            PersonTestUtil.toValidRequestDTO(entrepreneur.getPerson())
        );
    }

    public static EntrepreneurResponseDTO toResponseDTO(Entrepreneur entrepreneur) {
        return new EntrepreneurResponseDTO(
            entrepreneur.getId(),
            PersonTestUtil.toResponseDTO(entrepreneur.getPerson()),
            entrepreneur.getBusinessName()
        );
    }
}
