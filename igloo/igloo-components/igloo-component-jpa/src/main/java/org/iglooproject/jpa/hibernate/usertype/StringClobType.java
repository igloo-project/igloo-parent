/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.iglooproject.jpa.hibernate.usertype;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * Map a String to a Clob
 * Copied from the original org.hibernate.type.StringClobType.
 *
 * @author Emmanuel Bernard
 */
public class StringClobType implements UserType, Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name for use in @Type annotations:
	 * <code>@Type(StringClobType.TYPENAME)</code>
	 */
	public static final String TYPENAME = "org.iglooproject.jpa.hibernate.usertype.StringClobType";
	
	@Override
	public int[] sqlTypes() {
		return new int[]{Types.CLOB};
	}

	@Override
	public Class<String> returnedClass() {
		return String.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException { // NOSONAR pmd:SuspiciousEqualsMethodName
		return ( x == y ) || ( x != null && x.equals( y ) );
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
		Reader reader = rs.getCharacterStream( names[0] );
		if ( reader == null ) return null;
		StringBuilder result = new StringBuilder( 4096 );
		try {
			char[] charbuf = new char[4096];
			for ( int i = reader.read( charbuf ); i > 0 ; i = reader.read( charbuf ) ) {
				result.append( charbuf, 0, i );
			}
		}
		catch (IOException e) {
			throw new SQLException( e.getMessage() );
		}
		return result.toString();
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		if ( value != null ) {
			String string = (String) value;
			StringReader reader = new StringReader( string );
			st.setCharacterStream( index, reader, string.length() );
		}
		else {
			st.setNull( index, sqlTypes()[0] );
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		//returning value should be OK since String are immutable
		return value;
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
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
