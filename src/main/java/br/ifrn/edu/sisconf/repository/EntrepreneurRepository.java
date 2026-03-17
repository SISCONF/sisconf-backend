package br.ifrn.edu.sisconf.repository;

import br.ifrn.edu.sisconf.domain.Entrepreneur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {
    Optional<Entrepreneur> findByPersonKeycloakId(String keycloakId);
}
