package net.braval.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.braval.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	public Optional<User> findByUsername(String username);
	
}
