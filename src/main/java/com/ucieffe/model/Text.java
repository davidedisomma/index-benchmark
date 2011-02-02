/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.ucieffe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name="text")
@Indexed(index="index")
public class Text implements java.io.Serializable {

	private static final long serialVersionUID = -3646271383125946501L;

	private Integer oldId;
	private String oldText;

	@Id
	@Column(name="old_id")
	public Integer getOldId() {
		return this.oldId;
	}

	public void setOldId(Integer oldId) {
		this.oldId = oldId;
	}

	@Column(name="old_text")
	@Field
	public String getOldText() {
		return this.oldText;
	}

	public void setOldText(String oldText) {
		this.oldText = oldText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oldId == null) ? 0 : oldId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Text other = (Text) obj;
		if (oldId == null) {
			if (other.oldId != null)
				return false;
		} else if (!oldId.equals(other.oldId))
			return false;
		return true;
	}

}

