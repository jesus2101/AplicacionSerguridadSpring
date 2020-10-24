package net.braval.exception;

public class UsernameOrIdNotExist extends Exception{

	
	public UsernameOrIdNotExist() {
		super("Usuario o Id no econtrado");
	}
	
	public UsernameOrIdNotExist(String message) {
		super(message);
	}
	
}
