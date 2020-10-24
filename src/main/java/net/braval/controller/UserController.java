package net.braval.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import net.braval.dto.ChangePasswordForm;
import net.braval.entity.User;
import net.braval.exception.UsernameOrIdNotExist;
import net.braval.repository.RoleRepository;
import net.braval.service.UserService;

//spring.jpa.hibernate.ddl-auto=create


@Controller
public class UserController {

	@Autowired
	private UserService serviceUser;
	
	@Autowired
	private RoleRepository repoRole;
	
	@GetMapping({"/","/login"})
	public String index() {
		
		
		return "index";
	}
	
	@GetMapping("/userForm")
	public String listado(Model model) {
		
		model.addAttribute("userForm", new User());
		model.addAttribute("userList", serviceUser.getAllUsers());
		model.addAttribute("roles",repoRole.findAll());
		model.addAttribute("listTab", "active");
		return "user-form/user-view";
	}
	
	@GetMapping("/editUser/{id}")
	public String getEditUserForm(@PathVariable("id")Long id,Model model) throws Exception {
		User user=serviceUser.getUserById(id);
		
		model.addAttribute("userForm", user);
		model.addAttribute("userList", serviceUser.getAllUsers());
		model.addAttribute("roles",repoRole.findAll());
		model.addAttribute("formTab", "active");
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm",new ChangePasswordForm(id));
		return "user-form/user-view";
		
	}
	
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id")Long id, Model model) {
		
		try {
			serviceUser.deleteUser(id);
		}catch (UsernameOrIdNotExist e) {
			model.addAttribute("listErrorMessage", e.getMessage());
		}
		
		return listado(model);
	}
	
	@PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm")User user,BindingResult result, ModelMap model) {
		
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode", "true");
			model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
			
		}else {
			try {
				serviceUser.updateUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", serviceUser.getAllUsers());
				model.addAttribute("roles",repoRole.findAll());
				model.addAttribute("editMode", "true");
				model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
				
			}
		}
		
		model.addAttribute("userList", serviceUser.getAllUsers());
		model.addAttribute("roles",repoRole.findAll());
		
		return "user-form/user-view";
		
	}
	
	@GetMapping("/editUser/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}
	
	
	@PostMapping("/userForm")
	public String createUser(@Valid @ModelAttribute("userForm")User user,BindingResult result, ModelMap model) {
		
		if(result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
			
		}else {
			try {
				serviceUser.createUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", serviceUser.getAllUsers());
				model.addAttribute("roles",repoRole.findAll());
				
				
			}
		}
		
		model.addAttribute("userList", serviceUser.getAllUsers());
		model.addAttribute("roles",repoRole.findAll());
		
		return "user-form/user-view";
		
	}
	
	@PostMapping("/editUser/changePassword")
	public ResponseEntity postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			//If error, just return a 400 bad request, along with the error message
	        if (errors.hasErrors()) {
	            String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);
	        }
			serviceUser.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("success");
	}
	

}
