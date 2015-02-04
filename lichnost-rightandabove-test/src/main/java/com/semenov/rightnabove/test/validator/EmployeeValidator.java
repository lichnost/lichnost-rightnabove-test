package com.semenov.rightnabove.test.validator;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.semenov.rightnabove.test.model.Employee;

public class EmployeeValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Employee.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO упростить все
		Employee employee = (Employee) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
				"errors.field.empty-or-whitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName",
				"errors.field.empty-or-whitespace");
		if (errors.getFieldError("salary") == null) {
			BigDecimal salary = employee.getSalary();
			if (salary == null) {
				errors.rejectValue("salary", "errors.field.empty-or-whitespace");
			} else if (salary.precision() > 15 || salary.scale() > 2) {
				errors.rejectValue("salary", "errors.field.salary-scale");
			}
		}
		if (errors.getFieldError("birthdate") == null) {

			Date birthdate = employee.getBirthdate();
			if (birthdate == null) {
				errors.rejectValue("birthdate",
						"errors.field.empty-or-whitespace");
			} else {
				Date now = new Date();
				Date begin = new Date(0, 0, 1);
				if (!(birthdate.after(begin) && birthdate.before(now))) {
					errors.rejectValue("birthdate",
							"errors.field.birthdate-constraint");
				}

			}
		}

	}

}
