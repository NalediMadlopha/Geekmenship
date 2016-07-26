package com.geekmenship.app.util.validator;

import android.widget.EditText;

/**
 * A simple validator that validates the field only if the field is not empty.
 * @author Andrea Baccega <me@andreabaccega.com>
 *
 */
public class LengthValidator extends Validator {
	private int length;
	
	public LengthValidator(String message, int length) {
		super(message);
		this.length = length;
	}
	public boolean isValid(EditText et) {
		if (et.getText().toString().length() != this.length) {
			return false;
		}
		
		return true;
	}
}
