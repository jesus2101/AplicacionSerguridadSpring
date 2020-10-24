package net.braval.exception;

public class CustomeFieldValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2534497832533850984L;
	
	
	private String fieldName;
	
	public CustomeFieldValidationException(String message, String fieldName) {
		super(message);
		this.fieldName=fieldName;
	}
	
	public String getFieldName() {
		return this.fieldName;
	}

}
