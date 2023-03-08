/*
 * This file is part of gradle-commons, licensed under the MIT License (MIT).
 *
 * Copyright (c) Team Galacticraft <https://github.com/GalacticSuite/gradle-commons>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package dev.galacticraft.gradle.commons.xml.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Contributor implements Cloneable, InputLocationTracker
{
	private String name;

	private String email;

	private String url;

	private String organization;

	private String organizationUrl;

	private List<String> roles;

	private String timezone;

	private Properties properties;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation nameLocation;

	private InputLocation emailLocation;

	private InputLocation urlLocation;

	private InputLocation organizationLocation;

	private InputLocation organizationUrlLocation;

	private InputLocation rolesLocation;

	private InputLocation timezoneLocation;

	private InputLocation propertiesLocation;

	public void addProperty(String key, String value)
	{
		getProperties().put(key, value);
	}

	public void addRole(String string)
	{
		getRoles().add(string);
	}

	public Contributor clone()
	{
		try
		{
			Contributor copy = (Contributor) super.clone();
			if (this.roles != null)
			{
				copy.roles = new ArrayList<>();
				copy.roles.addAll(this.roles);
			}
			if (this.properties != null)
				copy.properties = (Properties) this.properties.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getEmail()
	{
		return this.email;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "name":
				return this.nameLocation;
			case "email":
				return this.emailLocation;
			case "url":
				return this.urlLocation;
			case "organization":
				return this.organizationLocation;
			case "organizationUrl":
				return this.organizationUrlLocation;
			case "roles":
				return this.rolesLocation;
			case "timezone":
				return this.timezoneLocation;
			case "properties":
				return this.propertiesLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public String getName()
	{
		return this.name;
	}

	public String getOrganization()
	{
		return this.organization;
	}

	public String getOrganizationUrl()
	{
		return this.organizationUrl;
	}

	public void setLocation(Object key, InputLocation location)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				this.location = location;
				return;
			case "name":
				this.nameLocation = location;
				return;
			case "email":
				this.emailLocation = location;
				return;
			case "url":
				this.urlLocation = location;
				return;
			case "organization":
				this.organizationLocation = location;
				return;
			case "organizationUrl":
				this.organizationUrlLocation = location;
				return;
			case "roles":
				this.rolesLocation = location;
				return;
			case "timezone":
				this.timezoneLocation = location;
				return;
			case "properties":
				this.propertiesLocation = location;
				return;
			}
			setOtherLocation(key, location);
			return;
		}
		setOtherLocation(key, location);
	}

	public void setOtherLocation(Object key, InputLocation location)
	{
		if (location != null)
		{
			if (this.locations == null)
				this.locations = new LinkedHashMap<>();
			this.locations.put(key, location);
		}
	}

	private InputLocation getOtherLocation(Object key)
	{
		return (this.locations != null) ? this.locations.get(key) : null;
	}

	public Properties getProperties()
	{
		if (this.properties == null)
			this.properties = new Properties();
		return this.properties;
	}

	public List<String> getRoles()
	{
		if (this.roles == null)
			this.roles = new ArrayList<>();
		return this.roles;
	}

	public String getTimezone()
	{
		return this.timezone;
	}

	public String getUrl()
	{
		return this.url;
	}

	public void removeRole(String string)
	{
		getRoles().remove(string);
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public void setOrganizationUrl(String organizationUrl)
	{
		this.organizationUrl = organizationUrl;
	}

	public void setProperties(Properties properties)
	{
		this.properties = properties;
	}

	public void setRoles(List<String> roles)
	{
		this.roles = roles;
	}

	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
