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

import java.util.LinkedHashMap;
import java.util.Map;

public class RepositoryBase implements Cloneable, InputLocationTracker
{
	private String id;

	private String name;

	private String url;

	private String layout = "default";

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation idLocation;

	private InputLocation nameLocation;

	private InputLocation urlLocation;

	private InputLocation layoutLocation;

	public RepositoryBase clone()
	{
		try
		{
			RepositoryBase copy = (RepositoryBase) super.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof RepositoryBase))
			return false;
		RepositoryBase	that	= (RepositoryBase) other;
		boolean			result	= true;
		result = (result && ((getId() == null) ? (that.getId() == null) : getId().equals(that.getId())));
		return result;
	}

	public String getId()
	{
		return this.id;
	}

	public String getLayout()
	{
		return this.layout;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "id":
				return this.idLocation;
			case "name":
				return this.nameLocation;
			case "url":
				return this.urlLocation;
			case "layout":
				return this.layoutLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public String getName()
	{
		return this.name;
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
			case "id":
				this.idLocation = location;
				return;
			case "name":
				this.nameLocation = location;
				return;
			case "url":
				this.urlLocation = location;
				return;
			case "layout":
				this.layoutLocation = location;
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

	public String getUrl()
	{
		return this.url;
	}

	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (id != null ? id.hashCode() : 0);

		return result;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setLayout(String layout)
	{
		this.layout = layout;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String toString()
	{
		StringBuilder buf = new StringBuilder(128);
		buf.append("id = '");
		buf.append(getId());
		buf.append("'");
		return buf.toString();
	}
}
