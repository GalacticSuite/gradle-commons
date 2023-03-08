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

public class Activation implements Cloneable, InputLocationTracker
{
	private boolean activeByDefault = false;

	private String jdk;

	private ActivationOS os;

	private ActivationProperty property;

	private ActivationFile file;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation activeByDefaultLocation;

	private InputLocation jdkLocation;

	private InputLocation osLocation;

	private InputLocation propertyLocation;

	private InputLocation fileLocation;

	public Activation clone()
	{
		try
		{
			Activation copy = (Activation) super.clone();
			if (this.os != null)
				copy.os = this.os.clone();
			if (this.property != null)
				copy.property = this.property.clone();
			if (this.file != null)
				copy.file = this.file.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public ActivationFile getFile()
	{
		return this.file;
	}

	public String getJdk()
	{
		return this.jdk;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "activeByDefault":
				return this.activeByDefaultLocation;
			case "jdk":
				return this.jdkLocation;
			case "os":
				return this.osLocation;
			case "property":
				return this.propertyLocation;
			case "file":
				return this.fileLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public ActivationOS getOs()
	{
		return this.os;
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
			case "activeByDefault":
				this.activeByDefaultLocation = location;
				return;
			case "jdk":
				this.jdkLocation = location;
				return;
			case "os":
				this.osLocation = location;
				return;
			case "property":
				this.propertyLocation = location;
				return;
			case "file":
				this.fileLocation = location;
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

	public ActivationProperty getProperty()
	{
		return this.property;
	}

	public boolean isActiveByDefault()
	{
		return this.activeByDefault;
	}

	public void setActiveByDefault(boolean activeByDefault)
	{
		this.activeByDefault = activeByDefault;
	}

	public void setFile(ActivationFile file)
	{
		this.file = file;
	}

	public void setJdk(String jdk)
	{
		this.jdk = jdk;
	}

	public void setOs(ActivationOS os)
	{
		this.os = os;
	}

	public void setProperty(ActivationProperty property)
	{
		this.property = property;
	}
}
