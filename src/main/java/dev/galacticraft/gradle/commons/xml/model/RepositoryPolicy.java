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

public class RepositoryPolicy implements Cloneable, InputLocationTracker
{
	private String enabled;

	private String updatePolicy;

	private String checksumPolicy;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation enabledLocation;

	private InputLocation updatePolicyLocation;

	private InputLocation checksumPolicyLocation;

	public RepositoryPolicy clone()
	{
		try
		{
			RepositoryPolicy copy = (RepositoryPolicy) super.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getChecksumPolicy()
	{
		return this.checksumPolicy;
	}

	public String getEnabled()
	{
		return this.enabled;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "enabled":
				return this.enabledLocation;
			case "updatePolicy":
				return this.updatePolicyLocation;
			case "checksumPolicy":
				return this.checksumPolicyLocation;
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
			case "enabled":
				this.enabledLocation = location;
				return;
			case "updatePolicy":
				this.updatePolicyLocation = location;
				return;
			case "checksumPolicy":
				this.checksumPolicyLocation = location;
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

	public String getUpdatePolicy()
	{
		return this.updatePolicy;
	}

	public void setChecksumPolicy(String checksumPolicy)
	{
		this.checksumPolicy = checksumPolicy;
	}

	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}

	public void setUpdatePolicy(String updatePolicy)
	{
		this.updatePolicy = updatePolicy;
	}

	public boolean isEnabled()
	{
		return (this.enabled != null) ? Boolean.parseBoolean(this.enabled) : true;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = String.valueOf(enabled);
	}
}
