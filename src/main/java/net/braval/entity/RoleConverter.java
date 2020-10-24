package net.braval.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import net.braval.repository.RoleRepository;

@Component
public class RoleConverter implements Converter<String,Role> {

	@Autowired
	private RoleRepository repoRole;
	
	@Override
	public Role convert(String id) {
		// TODO Auto-generated method stub
		return repoRole.findById(Long.valueOf(id)).orElse(null);
	}

}
