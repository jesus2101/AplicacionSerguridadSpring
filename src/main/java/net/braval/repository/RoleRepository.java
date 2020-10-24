package net.braval.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.braval.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{
	
	

}
