package com.geekmenship.app.util.validator;


public class AlphaNumericValidator extends RegexpValidator {
	public AlphaNumericValidator(String message) {
		super(message, 	"[a-zA-Z0-9 \\./-]*");
	}
	
}
