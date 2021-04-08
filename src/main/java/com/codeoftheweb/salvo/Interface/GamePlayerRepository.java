package com.codeoftheweb.salvo.Interface;

import com.codeoftheweb.salvo.Model.Game;
import com.codeoftheweb.salvo.Model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long>{
    Optional<GamePlayer> findById(@Param("id") long id);
}
