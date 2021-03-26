package com.codeoftheweb.salvo.Interface;

import com.codeoftheweb.salvo.Model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalvoRepository extends JpaRepository<Ship, Long> {
}
