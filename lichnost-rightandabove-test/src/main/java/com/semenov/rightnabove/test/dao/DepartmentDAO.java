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

import com.semenov.rightnabove.test.model.Department;

@Repository
public class DepartmentDAO implements AbstractDAO<Department> {

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
	public void save(Department department) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO employee (");
		sql.append("id, ");
		sql.append("name");
		sql.append(")");
		sql.append("VALUES (");
		sql.append("?, ");
		sql.append("?");
		sql.append(")");
		jdbcTemplate.update(sql.toString(), nextval(), department.getName());
	}

	@Override
	public Department getById(long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("id, ");
		sql.append("name ");
		sql.append("FROM departments ");
		sql.append("WHERE id = ?");
		Department result = jdbcTemplate.queryForObject(sql.toString(),
				new Object[] { id }, new DepartmentRowMapper());
		return result;
	}

	@Override
	public void update(final Department department) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE departments ");
		sql.append("SET ");
		sql.append("name = ? ");
		sql.append("WHERE id = ?");
		jdbcTemplate.update(sql.toString(), department.getName(),
				department.getId());
	}

	@Override
	public void deleteById(long id) {
		String sql = "DELETE FROM departments WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Department> search(Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("id, ");
		sql.append("name ");
		sql.append("FROM departments ");
		sql.append("WHERE 1 = 1 ");

		List<Object> args = new ArrayList<Object>();
		if (parameters.containsKey("name")) {
			sql.append("AND name LIKE ? ");
			args.add(parameters.get("name"));
		}

		List<Department> result = jdbcTemplate.query(sql.toString(),
				args.toArray(), new DepartmentRowMapper());
		return result;
	}

	private static class DepartmentRowMapper implements RowMapper<Department> {
		@Override
		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			Department department = new Department();
			department.setId(rs.getLong("id"));
			department.setName(rs.getString("name"));
			return department;
		}
	}

}
