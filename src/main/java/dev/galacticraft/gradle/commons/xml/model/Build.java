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

public class Build extends BuildBase implements Cloneable
{
	private String sourceDirectory;

	private String scriptSourceDirectory;

	private String testSourceDirectory;

	private String outputDirectory;

	private String testOutputDirectory;

	private List<Extension> extensions;

	public void addExtension(Extension extension)
	{
		getExtensions().add(extension);
	}

	public Build clone()
	{
		try
		{
			Build copy = (Build) super.clone();
			if (this.extensions != null)
			{
				copy.extensions = new ArrayList<>();
				for (Extension item : this.extensions)
					copy.extensions.add(item.clone());
			}
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public List<Extension> getExtensions()
	{
		if (this.extensions == null)
			this.extensions = new ArrayList<>();
		return this.extensions;
	}

	public String getOutputDirectory()
	{
		return this.outputDirectory;
	}

	public String getScriptSourceDirectory()
	{
		return this.scriptSourceDirectory;
	}

	public String getSourceDirectory()
	{
		return this.sourceDirectory;
	}

	public String getTestOutputDirectory()
	{
		return this.testOutputDirectory;
	}

	public String getTestSourceDirectory()
	{
		return this.testSourceDirectory;
	}

	public void removeExtension(Extension extension)
	{
		getExtensions().remove(extension);
	}

	public void setExtensions(List<Extension> extensions)
	{
		this.extensions = extensions;
	}

	public void setOutputDirectory(String outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}

	public void setScriptSourceDirectory(String scriptSourceDirectory)
	{
		this.scriptSourceDirectory = scriptSourceDirectory;
	}

	public void setSourceDirectory(String sourceDirectory)
	{
		this.sourceDirectory = sourceDirectory;
	}

	public void setTestOutputDirectory(String testOutputDirectory)
	{
		this.testOutputDirectory = testOutputDirectory;
	}

	public void setTestSourceDirectory(String testSourceDirectory)
	{
		this.testSourceDirectory = testSourceDirectory;
	}
}
