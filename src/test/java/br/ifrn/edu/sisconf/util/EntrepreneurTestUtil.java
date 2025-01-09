package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;

public class EntrepreneurTestUtil {
    public static EntrepreneurResponseDTO toResponseDTO(Entrepreneur entrepreneur) {
        return new EntrepreneurResponseDTO(
            entrepreneur.getId(),
            PersonTestUtil.toResponseDTO(entrepreneur.getPerson()),
            entrepreneur.getBusinessName()
        );
    }
}
