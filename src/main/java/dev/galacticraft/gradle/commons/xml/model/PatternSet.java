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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatternSet implements Cloneable, InputLocationTracker
{
	private List<String> includes;

	private List<String> excludes;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation includesLocation;

	private InputLocation excludesLocation;

	public void addExclude(String string)
	{
		getExcludes().add(string);
	}

	public void addInclude(String string)
	{
		getIncludes().add(string);
	}

	public PatternSet clone()
	{
		try
		{
			PatternSet copy = (PatternSet) super.clone();
			if (this.includes != null)
			{
				copy.includes = new ArrayList<>();
				copy.includes.addAll(this.includes);
			}
			if (this.excludes != null)
			{
				copy.excludes = new ArrayList<>();
				copy.excludes.addAll(this.excludes);
			}
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public List<String> getExcludes()
	{
		if (this.excludes == null)
			this.excludes = new ArrayList<>();
		return this.excludes;
	}

	public List<String> getIncludes()
	{
		if (this.includes == null)
			this.includes = new ArrayList<>();
		return this.includes;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "includes":
				return this.includesLocation;
			case "excludes":
				return this.excludesLocation;
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
			case "includes":
				this.includesLocation = location;
				return;
			case "excludes":
				this.excludesLocation = location;
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

	public void removeExclude(String string)
	{
		getExcludes().remove(string);
	}

	public void removeInclude(String string)
	{
		getIncludes().remove(string);
	}

	public void setExcludes(List<String> excludes)
	{
		this.excludes = excludes;
	}

	public void setIncludes(List<String> includes)
	{
		this.includes = includes;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(128);
		sb.append("PatternSet [includes: {");
		for (Iterator<String> iterator1 = getIncludes().iterator(); iterator1.hasNext();)
		{
			String str = iterator1.next();
			sb.append(str).append(", ");
		}
		if (sb.substring(sb.length() - 2).equals(", "))
			sb.delete(sb.length() - 2, sb.length());
		sb.append("}, excludes: {");
		for (Iterator<String> i = getExcludes().iterator(); i.hasNext();)
		{
			String str = i.next();
			sb.append(str).append(", ");
		}
		if (sb.substring(sb.length() - 2).equals(", "))
			sb.delete(sb.length() - 2, sb.length());
		sb.append("}]");
		return sb.toString();
	}
}
