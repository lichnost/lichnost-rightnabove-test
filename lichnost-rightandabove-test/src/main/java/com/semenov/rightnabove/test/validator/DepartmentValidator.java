package com.semenov.rightnabove.test.validator;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.semenov.rightnabove.test.model.Department;

public class DepartmentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Department.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
				"errors.field.empty-or-whitespace");
		if (target instanceof Department && !errors.hasFieldErrors()) {
			//TODO это должно быть не в валидаторе
			Department department = (Department) target;
			department.setName(StringUtils.capitalize(department.getName()
					.trim()));
		}
	}

}
