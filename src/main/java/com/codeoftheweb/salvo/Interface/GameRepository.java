package com.codeoftheweb.salvo.Interface;

import com.codeoftheweb.salvo.Model.Game;
import com.codeoftheweb.salvo.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findById(@Param("id") long id);
}