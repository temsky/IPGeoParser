package ru.temsky.ipgeo.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class InetType implements org.hibernate.usertype.UserType {

	private static final int[] SQL_TYPES = { Types.OTHER };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return String.class;
	}

	@Override
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		return arg0 == arg1;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		return arg0;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		return arg0;
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		return arg0;
	}

	@Override
	public Object nullSafeGet(ResultSet arg0, String[] arg1, SessionImplementor arg2, Object arg3) throws HibernateException, SQLException {
		String grade = arg0.getString(arg1[0]);
		return arg0.wasNull() ? null : grade;
	}

	@Override
	public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2, SessionImplementor arg3) throws HibernateException, SQLException {
		if (arg1 == null) {
			arg0.setNull(arg2, Types.OTHER);
		} else {
			arg0.setObject(arg2, arg1, Types.OTHER);
		}

	}
}