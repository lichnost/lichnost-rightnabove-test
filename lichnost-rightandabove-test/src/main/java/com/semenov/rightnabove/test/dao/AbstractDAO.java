package com.semenov.rightnabove.test.dao;

import java.util.List;
import java.util.Map;

public interface AbstractDAO<T> {

	void save(T object);

	T getById(long id);

	void update(T object);

	void deleteById(long id);

	List<T> search(Map<String, Object> parameters);

}
