package com.semenov.rightnabove.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.semenov.rightnabove.test.model.Employee;

@Repository
public class EmployeeDAO implements AbstractDAO<Employee> {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public long nextval() {
		String sql = "SELECT nextval(\'seq_id\')";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	@Override
	public void save(Employee employee) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO employees (");
		sql.append("id, ");
		sql.append("department_id, ");
		sql.append("first_name, ");
		sql.append("last_name, ");
		sql.append("salary, ");
		sql.append("birthdate, ");
		sql.append("active");
		sql.append(")");
		sql.append("VALUES (");
		sql.append("?, ");
		sql.append("?, ");
		sql.append("?, ");
		sql.append("?, ");
		sql.append("?, ");
		sql.append("?, ");
		sql.append("?");
		sql.append(")");
		jdbcTemplate.update(sql.toString(), nextval(),
				employee.getDepartmentId(), employee.getFirstName(),
				employee.getLastName(), employee.getSalary(),
				employee.getBirthdate(), employee.getActive());
	}

	@Override
	public Employee getById(long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("e.id, ");
		sql.append("e.department_id, ");
		sql.append("(SELECT d.name FROM departments d WHERE d.id = e.department_id) department_name, ");
		sql.append("e.first_name, ");
		sql.append("e.last_name, ");
		sql.append("e.salary, ");
		sql.append("e.birthdate, ");
		sql.append("e.active ");
		sql.append("FROM employees e ");
		sql.append("WHERE e.id = ? ");
		Employee result = jdbcTemplate.queryForObject(sql.toString(),
				new Object[] { id }, new EmployeeRowMapper());
		return result;
	}

	@Override
	public void update(final Employee employee) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE employees ");
		sql.append("SET ");
		sql.append("department_id = ?, ");
		sql.append("first_name = ?, ");
		sql.append("last_name = ?, ");
		sql.append("salary = ?, ");
		sql.append("birthdate = ?, ");
		sql.append("active = ? ");
		sql.append("WHERE id = ?");
		jdbcTemplate.update(sql.toString(), employee.getDepartmentId(),
				employee.getFirstName(), employee.getLastName(),
				employee.getSalary(), employee.getBirthdate(),
				employee.getActive(), employee.getId());
	}

	@Override
	public void deleteById(long id) {
		String sql = "DELETE FROM employees WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Employee> search(Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("e.id, ");
		sql.append("e.department_id, ");
		sql.append("(SELECT d.name FROM departments d WHERE d.id = e.department_id) department_name, ");
		sql.append("e.first_name, ");
		sql.append("e.last_name, ");
		sql.append("e.salary, ");
		sql.append("e.birthdate, ");
		sql.append("e.active ");
		sql.append("FROM employees e ");
		sql.append("WHERE 1 = 1 ");

		List<Object> args = new ArrayList<Object>();
		if (parameters.containsKey("firstName")) {
			sql.append("AND e.first_name LIKE ? ");
			args.add(parameters.get("firstName"));
		}
		if (parameters.containsKey("lastName")) {
			sql.append("AND e.last_name LIKE ? ");
			args.add(parameters.get("lastName"));
		}
		if (parameters.containsKey("departmentName")) {
			sql.append("AND e.department_id IN (SELECT d.id FROM departments d where d.name LIKE ?) ");
			args.add(parameters.get("departmentName"));
		}
		if (parameters.containsKey("salary")) {
			// TODO поиск по диапазону
		}
		if (parameters.containsKey("birthdate")) {
			sql.append("AND e.birthdate = ? ");
			args.add(parameters.get("birthdate"));
		}
		if (parameters.containsKey("active")) {
			sql.append("AND e.active = ? ");
			args.add(parameters.get("active"));
		}

		List<Employee> result = jdbcTemplate.query(sql.toString(),
				args.toArray(), new EmployeeRowMapper());
		return result;
	}

	private static class EmployeeRowMapper implements RowMapper<Employee> {
		@Override
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setId(rs.getLong("id"));
			employee.setDepartmentId(rs.getLong("department_id"));
			employee.setDepartmentName(rs.getString("department_name"));
			employee.setFirstName(rs.getString("first_name"));
			employee.setLastName(rs.getString("last_name"));
			employee.setSalary(rs.getBigDecimal("salary"));
			employee.setBirthdate(rs.getDate("birthdate"));
			employee.setActive(rs.getBoolean("active"));
			return employee;
		}
	}

}
