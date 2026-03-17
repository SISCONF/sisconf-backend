package br.ifrn.edu.sisconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifrn.edu.sisconf.domain.CountryState;

public interface CountryStateRepository extends JpaRepository<CountryState, Long> {

}
