package com.geekmenship.app.util.validator;

public class AlphaValidator extends RegexpValidator {
	public AlphaValidator(String message) {
		super(message, "[a-zA-Z \\./-]*");
	}
}
