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

public class Profile extends ModelBase implements Cloneable
{
	private String id = "default";

	private Activation activation;

	private BuildBase build;

	public static final String SOURCE_POM = "pom";

	public static final String SOURCE_SETTINGS = "settings.xml";

	public Profile clone()
	{
		try
		{
			Profile copy = (Profile) super.clone();
			if (this.activation != null)
				copy.activation = this.activation.clone();
			if (this.build != null)
				copy.build = this.build.clone();
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public Activation getActivation()
	{
		return this.activation;
	}

	public BuildBase getBuild()
	{
		return this.build;
	}

	public String getId()
	{
		return this.id;
	}

	public void setActivation(Activation activation)
	{
		this.activation = activation;
	}

	public void setBuild(BuildBase build)
	{
		this.build = build;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	private String source = "pom";

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getSource()
	{
		return this.source;
	}

	public String toString()
	{
		return "Profile {id: " + getId() + ", source: " + getSource() + "}";
	}
}
