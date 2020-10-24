package net.braval.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.braval.dto.ChangePasswordForm;
import net.braval.entity.User;
import net.braval.exception.UsernameOrIdNotExist;
import net.braval.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repoUser;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override
	public Iterable<User> getAllUsers() {
		// TODO Auto-generated method stub
		return repoUser.findAll();
	}
	
	
	
	
	
	private boolean checkUserNameExist(User user) throws Exception {
		Optional<User> userFound=repoUser.findByUsername(user.getUsername());
		
		if(userFound.isPresent()) {
			throw new Exception("Username no disponible");
		}
		
		return true;
		
	}
	
	private boolean checkPasswordMatch(User user) throws Exception {
		
		if(user.getConfirmPassword()==null || user.getConfirmPassword().isEmpty()) {
			throw new Exception("Confirme password es obligatorio");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception("Passwrod y correo no son iguales");
		}
		
		return true;
	}





	@Override
	public User createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		
		if(checkUserNameExist(user) && checkPasswordMatch(user)) {
			
			String encodePassword=bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			user=repoUser.save(user);
		}
		return user;
	}





	@Override
	public User getUserById(Long id) throws UsernameOrIdNotExist {
		// TODO Auto-generated method stub
		return repoUser.findById(id).orElseThrow(()->new UsernameOrIdNotExist("El id del usuario no existe"));
	}





	@Override
	public User updateUser(User fromUser) throws Exception {
		// TODO Auto-generated method stub
		
		User toUser=getUserById(fromUser.getId());
		
		mapUser(fromUser,toUser);
		
		
		return repoUser.save(toUser); 
	} 
	
	protected void mapUser(User from, User to) {
		
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
		
		
	}





	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UsernameOrIdNotExist {
		// TODO Auto-generated method stub
		
		User user=getUserById(id);
		
		repoUser.delete(user);
		
		
	}




	
	/*public boolean isLoggedUserADMIN(){
		 return loggedUserHasRole("ROLE_ADMIN");
		}

		public boolean loggedUserHasRole(String role) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails loggedUser = null;
			Object roles = null; 
			if (principal instanceof UserDetails) {
				loggedUser = (UserDetails) principal;
			
				roles = loggedUser.getAuthorities().stream()
						.filter(x -> role.equals(x.getAuthority() ))      
						.findFirst().orElse(null); //loggedUser = null;
			}
			return roles != null ?true :false;
		}*/
	
	public boolean isLoggedUserADMIN(){
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails loggedUser = null;
		Object roles = null;
		
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		
			roles = loggedUser.getAuthorities().stream()
					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority()))      
					.findFirst().orElse(null);
		}
		return roles != null ?true :false;
		
	}
	
	
	public User getLoggedUser() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		
		User myUser = repoUser
				.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Problemas obteneido usuario de sesion"));
		
		return myUser;
	}
	
		
	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		
		User storedUser = repoUser
				.findById( form.getId() )
				.orElseThrow(() -> new Exception("UsernotFound in ChangePassword -"+this.getClass().getName()));
		
		//User storedUser=getUserById(form.getId());
		
		System.out.println(storedUser);
		System.out.println(form.getCurrentPassword());
		if( !isLoggedUserADMIN() && !form.getCurrentPassword().equals(storedUser.getPassword())) {
			throw new Exception("Current Password Incorrect.");
		}
		
		if ( form.getCurrentPassword().equals(form.getNewPassword())) {
			throw new Exception("New Password must be different than Current Password!");
		}
		
		if( !form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception("New Password and Confirm Password does not match!");
		}
		
		
		String encodePassword=bCryptPasswordEncoder.encode(form.getNewPassword());
		storedUser.setPassword(encodePassword);
		return repoUser.save(storedUser);
		
		
	}

}
