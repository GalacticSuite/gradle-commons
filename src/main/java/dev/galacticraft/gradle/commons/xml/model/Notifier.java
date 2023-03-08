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
import java.util.Properties;

public class Notifier implements Cloneable, InputLocationTracker
{
	private String type = "mail";

	private boolean sendOnError = true;

	private boolean sendOnFailure = true;

	private boolean sendOnSuccess = true;

	private boolean sendOnWarning = true;

	private String address;

	private Properties configuration;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation typeLocation;

	private InputLocation sendOnErrorLocation;

	private InputLocation sendOnFailureLocation;

	private InputLocation sendOnSuccessLocation;

	private InputLocation sendOnWarningLocation;

	private InputLocation addressLocation;

	private InputLocation configurationLocation;

	public void addConfiguration(String key, String value)
	{
		getConfiguration().put(key, value);
	}

	public Notifier clone()
	{
		try
		{
			Notifier copy = (Notifier) super.clone();
			if (this.configuration != null)
				copy.configuration = (Properties) this.configuration.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getAddress()
	{
		return this.address;
	}

	public Properties getConfiguration()
	{
		if (this.configuration == null)
			this.configuration = new Properties();
		return this.configuration;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "type":
				return this.typeLocation;
			case "sendOnError":
				return this.sendOnErrorLocation;
			case "sendOnFailure":
				return this.sendOnFailureLocation;
			case "sendOnSuccess":
				return this.sendOnSuccessLocation;
			case "sendOnWarning":
				return this.sendOnWarningLocation;
			case "address":
				return this.addressLocation;
			case "configuration":
				return this.configurationLocation;
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
			case "type":
				this.typeLocation = location;
				return;
			case "sendOnError":
				this.sendOnErrorLocation = location;
				return;
			case "sendOnFailure":
				this.sendOnFailureLocation = location;
				return;
			case "sendOnSuccess":
				this.sendOnSuccessLocation = location;
				return;
			case "sendOnWarning":
				this.sendOnWarningLocation = location;
				return;
			case "address":
				this.addressLocation = location;
				return;
			case "configuration":
				this.configurationLocation = location;
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

	public String getType()
	{
		return this.type;
	}

	public boolean isSendOnError()
	{
		return this.sendOnError;
	}

	public boolean isSendOnFailure()
	{
		return this.sendOnFailure;
	}

	public boolean isSendOnSuccess()
	{
		return this.sendOnSuccess;
	}

	public boolean isSendOnWarning()
	{
		return this.sendOnWarning;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public void setConfiguration(Properties configuration)
	{
		this.configuration = configuration;
	}

	public void setSendOnError(boolean sendOnError)
	{
		this.sendOnError = sendOnError;
	}

	public void setSendOnFailure(boolean sendOnFailure)
	{
		this.sendOnFailure = sendOnFailure;
	}

	public void setSendOnSuccess(boolean sendOnSuccess)
	{
		this.sendOnSuccess = sendOnSuccess;
	}

	public void setSendOnWarning(boolean sendOnWarning)
	{
		this.sendOnWarning = sendOnWarning;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
