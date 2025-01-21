package es.judith.security.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.judith.security.domain.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

  Optional<Client> findByClientId(String clientId);
  Optional<Client> findByClientName(String name);
}