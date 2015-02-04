package com.semenov.rightnabove.test.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.semenov.rightnabove.test.dao.DepartmentDAO;
import com.semenov.rightnabove.test.dao.EmployeeDAO;
import com.semenov.rightnabove.test.model.Department;
import com.semenov.rightnabove.test.model.Employee;

@Controller
public class MainController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EmployeeDAO employeeDao;
	@Autowired
	private Validator employeeValidator;

	@Autowired
	private DepartmentDAO departmentDao;
	@Autowired
	private Validator departmentValidator;

	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	@SuppressWarnings("unchecked")
	private boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();
		boolean result = false;
		for (GrantedAuthority authority : authorities) {
			result = authority.getAuthority().equals(role);
			if (result) {
				break;
			}
		}
		return result;
	}

	private boolean isViewer() {
		return hasRole("ROLE_VIEWR");
	}

	private boolean isEditor() {
		return hasRole("ROLE_EDITOR");
	}

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof Department) {
			binder.setValidator(departmentValidator);
		}
		if (binder.getTarget() instanceof Employee) {
			binder.setValidator(employeeValidator);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			binder.registerCustomEditor(Date.class, new CustomDateEditor(
					dateFormat, true));
		}
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "redirect:/pages/employees.html";
	}

	@RequestMapping(value = "/pages/employees", method = RequestMethod.GET)
	public String employees(
			@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult,
			Locale locale,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "departmentName", required = false) String departmentName,
			@RequestParam(value = "salary", required = false) String salaryStr,
			@RequestParam(value = "birthdate", required = false) String birthdateStr,
			@RequestParam(value = "active", required = false) String activeStr) {
		model.addAttribute("viewOnly", !isEditor());

		Double salary = null;
		Date birthdate = null;
		Boolean active = null;
		if (!StringUtils.isEmpty(salaryStr)) {
			try {
				salary = Double.parseDouble(salaryStr);
			} catch (NumberFormatException e) {
				bindingResult.addError(new ObjectError("salary", messageSource
						.getMessage("employees.errors.salary-parse",
								new String[] {}, locale)));
			}
		}
		if (!StringUtils.isEmpty(birthdateStr)) {
			try {
				birthdate = dateFormat.parse(birthdateStr);
			} catch (ParseException e) {
				bindingResult.addError(new ObjectError("birthdate",
						messageSource.getMessage(
								"employees.errors.birthdate-parse",
								new String[] {}, locale)));
			}
		}
		if (!StringUtils.isEmpty(activeStr)) {
			active = "on".equalsIgnoreCase(activeStr);
		}

		Map<String, Object> searchParameters = new HashMap<String, Object>();
		Map<String, Object> returnParameters = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(firstName)) {
			searchParameters.put("firstName", firstName.replace("*", "%")
					.replace("?", "_"));
			returnParameters.put("firstName", firstName);
		}
		if (!StringUtils.isEmpty(lastName)) {
			searchParameters.put("lastName", lastName.replace("*", "%")
					.replace("?", "_"));
			returnParameters.put("lastName", lastName);
		}
		if (!StringUtils.isEmpty(departmentName)) {
			searchParameters.put("departmentName",
					departmentName.replace("*", "%").replace("?", "_"));
			returnParameters.put("departmentName",
					departmentName);
		}
		if (salary != null) {
			searchParameters.put("salary", salary);
			returnParameters.put("salary", salary);
		}
		if (birthdate != null) {
			searchParameters.put("birthdate", birthdate);
			returnParameters.put("birthdate", birthdate);
		}
		if (active != null) {
			searchParameters.put("active", active);
			returnParameters.put("active", active);
		}
		
		List<Employee> list = employeeDao.search(searchParameters);
		
		model.addAllAttributes(returnParameters);
		model.addAttribute("employees", list);

		return "/pages/employees";
	}

	@RequestMapping(value = { "/pages/{action}/employee" }, method = RequestMethod.GET)
	public String actionEmployee(@PathVariable("action") String action,
			@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@RequestParam(value = "id", required = false) String id) {
		boolean viewOnly = !isEditor() || "view".equals(action);
		model.addAttribute("viewOnly", viewOnly);

		model.addAttribute("departments",
				departmentDao.search(Collections.<String, Object> emptyMap()));

		if (StringUtils.isEmpty(id)) {
			return "edit-employee";
		}

		Employee employee = employeeDao.getById(Long.valueOf(id));
		model.addAttribute("employee", employee);
		return "/pages/edit-employee";
	}

	@RequestMapping(value = "/pages/save/employee", method = RequestMethod.POST)
	public String saveEmployee(@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@ModelAttribute("employee") @Validated Employee employee) {
		boolean viewOnly = !isEditor();
		model.addAttribute("viewOnly", viewOnly);

		if (bindingResult.hasFieldErrors() || viewOnly) {
			model.addAttribute("departments", departmentDao.search(Collections
					.<String, Object> emptyMap()));
			model.addAttribute("employee", employee);

			// TODO похоже передача ошибок делается как-то по-другому
			model.addAttribute("binderrors", bindingResult);
			return "/pages/edit-employee";
		}

		if (employee.getId() != null) {
			employeeDao.update(employee);
		} else {
			employeeDao.save(employee);
		}
		// TODO слелать сообщение о результате выполнения
		return "redirect:/pages/employees.html";
	}

	@RequestMapping(value = "/pages/delete/employee", method = RequestMethod.GET)
	public String deleteEmployee(@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@RequestParam(value = "id") Long id) {
		if (id != null) {
			employeeDao.deleteById(id);
		}
		// TODO слелать сообщение о результате выполнения
		return "redirect:/pages/employees.html";
	}

	@RequestMapping(value = "/pages/departments", method = RequestMethod.GET)
	public String departments(@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@RequestParam(value = "name", required = false) String name) {
		model.addAttribute("viewOnly", !isEditor());

		Map<String, Object> searchParameters = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(name)) {
			searchParameters.put("name",
					name.replace("*", "%").replace("?", "_"));
		}
		model.addAllAttributes(searchParameters);

		List<Department> list = departmentDao.search(searchParameters);
		model.addAttribute("departments", list);
		return "/pages/departments";
	}

	@RequestMapping(value = { "/pages/{action}/department" }, method = RequestMethod.GET)
	public String actionDepartment(@PathVariable("action") String action,
			@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@RequestParam(value = "id", required = false) Long id) {
		boolean viewOnly = !isEditor() || "view".equals(action);
		model.addAttribute("viewOnly", viewOnly);

		if (StringUtils.isEmpty(id)) {
			return "/pages/edit-department";
		}

		Department department = departmentDao.getById(id);
		model.addAttribute("department", department);
		return "/pages/edit-department";
	}

	@RequestMapping(value = "/pages/save/department", method = RequestMethod.POST)
	public String saveDepartment(@ModelAttribute("model") ModelMap model,
			@ModelAttribute("department") @Validated Department department,
			BindingResult bindingResult, Locale locale) {
		boolean viewOnly = !isEditor();
		model.addAttribute("viewOnly", viewOnly);

		if (bindingResult.hasFieldErrors() || viewOnly) {
			model.addAttribute("department", department);

			// TODO похоже передача ошибок делается как-то по-другому
			model.addAttribute("binderrors", bindingResult);
			return "/pages/edit-department";
		}

		if (department.getId() != null) {
			departmentDao.update(department);
		} else {
			departmentDao.save(department);
		}
		// TODO слелать сообщение о результате выполнения
		return "redirect:/pages/departments.html";
	}

	@RequestMapping(value = "/pages/delete/department", method = RequestMethod.GET)
	public String deleteDepartment(@ModelAttribute("model") ModelMap model,
			BindingResult bindingResult, Locale locale,
			@RequestParam(value = "id") Long id) {
		if (id != null) {
			departmentDao.deleteById(id);
		}
		// TODO слелать сообщение о результате выполнения
		return "redirect:/pages/departments.html";
	}

	// TODO надо разделить исключения по типам
	@ExceptionHandler({ Exception.class })
	private ModelAndView exceptionHandler(Exception exception) {
		ModelAndView model = new ModelAndView("pages/exception");
		model.addObject("exception", exception.getMessage());
		return model;
	}

}
