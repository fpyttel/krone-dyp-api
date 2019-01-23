package de.fpyttel.kronedyp.backend.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.fpyttel.kronedyp.backend.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

}
