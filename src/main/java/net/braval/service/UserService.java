package net.braval.service;

import java.util.List;

import javax.validation.Valid;

import net.braval.dto.ChangePasswordForm;
import net.braval.entity.User;
import net.braval.exception.UsernameOrIdNotExist;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
	
	public User getUserById(Long id ) throws UsernameOrIdNotExist;
	
	public User updateUser(User formUser) throws Exception;
	
	public void deleteUser(Long id) throws UsernameOrIdNotExist;
	
	public User changePassword(ChangePasswordForm form) throws Exception;
	
}
