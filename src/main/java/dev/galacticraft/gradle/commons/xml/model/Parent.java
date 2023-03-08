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

public class Parent implements Cloneable, InputLocationTracker
{
	private String groupId;

	private String artifactId;

	private String version;

	private String relativePath = "../pom.xml";

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation groupIdLocation;

	private InputLocation artifactIdLocation;

	private InputLocation versionLocation;

	private InputLocation relativePathLocation;

	public Parent clone()
	{
		try
		{
			Parent copy = (Parent) super.clone();
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
			case "relativePath":
				return this.relativePathLocation;
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
			case "relativePath":
				this.relativePathLocation = location;
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

	public String getRelativePath()
	{
		return this.relativePath;
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

	public void setRelativePath(String relativePath)
	{
		this.relativePath = relativePath;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getId()
	{
		StringBuilder id = new StringBuilder(64);
		id.append(getGroupId());
		id.append(":");
		id.append(getArtifactId());
		id.append(":");
		id.append("pom");
		id.append(":");
		id.append(getVersion());
		return id.toString();
	}

	public String toString()
	{
		return getId();
	}
}
