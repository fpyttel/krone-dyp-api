package de.fpyttel.kronedyp.api.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.fpyttel.kronedyp.api.dao.entity.PlayerBE;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerBE, Long> {

}
