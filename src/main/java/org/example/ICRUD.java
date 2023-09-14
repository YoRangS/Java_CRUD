package org.example;

import java.sql.SQLException;

public interface ICRUD {
	public int add(Word one) throws SQLException;
	public int update(Word one);
	public int delete(Word one);
}
