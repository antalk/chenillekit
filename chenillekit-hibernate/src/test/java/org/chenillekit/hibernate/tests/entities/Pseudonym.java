/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.hibernate.tests.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.tapestry5.beaneditor.Validate;

/**
 * @version $Id$
 */
@Entity
@Table(name = "pseudonym")
public class Pseudonym implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, columnDefinition = "BIGINT")
	private long id;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "id_user", nullable = false, columnDefinition = "BIGINT")
	private User user;

	@Basic
	@Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(8)")
	@Validate("required")
	private String name;

	public Pseudonym()
	{
	}

	public Pseudonym(User user, String name)
	{
		this.user = user;
		this.name = name;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Pseudonym pseudonym = (Pseudonym) o;

		return id == pseudonym.id;
	}

	public int hashCode()
	{
		return (int) (id ^ (id >>> 32));
	}


	public String toString()
	{
		return "Pseudonym{" +
				"id=" + id +
				", user=" + user +
				'}';
	}
}