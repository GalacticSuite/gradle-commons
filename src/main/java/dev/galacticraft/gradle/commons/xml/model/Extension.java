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

public class Extension implements Cloneable, InputLocationTracker
{
	private String groupId;

	private String artifactId;

	private String version;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation groupIdLocation;

	private InputLocation artifactIdLocation;

	private InputLocation versionLocation;

	public Extension clone()
	{
		try
		{
			Extension copy = (Extension) super.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getArtifactId()
	{
		return this.artifactId;
	}

	public String getGroupId()
	{
		return this.groupId;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "groupId":
				return this.groupIdLocation;
			case "artifactId":
				return this.artifactIdLocation;
			case "version":
				return this.versionLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
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
			case "groupId":
				this.groupIdLocation = location;
				return;
			case "artifactId":
				this.artifactIdLocation = location;
				return;
			case "version":
				this.versionLocation = location;
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

	public String getVersion()
	{
		return this.version;
	}

	public void setArtifactId(String artifactId)
	{
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof Extension))
			return false;
		Extension e = (Extension) o;
		if (!equal(e.getArtifactId(), getArtifactId()))
			return false;
		if (!equal(e.getGroupId(), getGroupId()))
			return false;
		if (!equal(e.getVersion(), getVersion()))
			return false;
		return true;
	}

	private static <T> boolean equal(T obj1, T obj2)
	{
		return (obj1 != null) ? obj1.equals(obj2) : ((obj2 == null));
	}

	public int hashCode()
	{
		int result = 17;
		result = 37 * result + ((getArtifactId() != null) ? getArtifactId().hashCode() : 0);
		result = 37 * result + ((getGroupId() != null) ? getGroupId().hashCode() : 0);
		result = 37 * result + ((getVersion() != null) ? getVersion().hashCode() : 0);
		return result;
	}
}
