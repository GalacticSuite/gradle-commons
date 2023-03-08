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
import java.util.List;

public class BuildBase extends PluginConfiguration implements Cloneable
{
	private String defaultGoal;

	private List<Resource> resources;

	private List<Resource> testResources;

	private String directory;

	private String finalName;

	private List<String> filters;

	public void addFilter(String string)
	{
		getFilters().add(string);
	}

	public void addResource(Resource resource)
	{
		getResources().add(resource);
	}

	public void addTestResource(Resource resource)
	{
		getTestResources().add(resource);
	}

	public BuildBase clone()
	{
		try
		{
			BuildBase copy = (BuildBase) super.clone();
			if (this.resources != null)
			{
				copy.resources = new ArrayList<>();
				for (Resource item : this.resources)
					copy.resources.add(item.clone());
			}
			if (this.testResources != null)
			{
				copy.testResources = new ArrayList<>();
				for (Resource item : this.testResources)
					copy.testResources.add(item.clone());
			}
			if (this.filters != null)
			{
				copy.filters = new ArrayList<>();
				copy.filters.addAll(this.filters);
			}
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getDefaultGoal()
	{
		return this.defaultGoal;
	}

	public String getDirectory()
	{
		return this.directory;
	}

	public List<String> getFilters()
	{
		if (this.filters == null)
			this.filters = new ArrayList<>();
		return this.filters;
	}

	public String getFinalName()
	{
		return this.finalName;
	}

	public List<Resource> getResources()
	{
		if (this.resources == null)
			this.resources = new ArrayList<>();
		return this.resources;
	}

	public List<Resource> getTestResources()
	{
		if (this.testResources == null)
			this.testResources = new ArrayList<>();
		return this.testResources;
	}

	public void removeFilter(String string)
	{
		getFilters().remove(string);
	}

	public void removeResource(Resource resource)
	{
		getResources().remove(resource);
	}

	public void removeTestResource(Resource resource)
	{
		getTestResources().remove(resource);
	}

	public void setDefaultGoal(String defaultGoal)
	{
		this.defaultGoal = defaultGoal;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public void setFilters(List<String> filters)
	{
		this.filters = filters;
	}

	public void setFinalName(String finalName)
	{
		this.finalName = finalName;
	}

	public void setResources(List<Resource> resources)
	{
		this.resources = resources;
	}

	public void setTestResources(List<Resource> testResources)
	{
		this.testResources = testResources;
	}
}
