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

public class Resource extends FileSet implements Cloneable
{
	private String targetPath;

	private String filtering;

	private String mergeId;

	public Resource clone()
	{
		try
		{
			Resource copy = (Resource) super.clone();
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getFiltering()
	{
		return this.filtering;
	}

	public String getMergeId()
	{
		return this.mergeId;
	}

	public String getTargetPath()
	{
		return this.targetPath;
	}

	public void setFiltering(String filtering)
	{
		this.filtering = filtering;
	}

	public void setMergeId(String mergeId)
	{
		this.mergeId = mergeId;
	}

	public void setTargetPath(String targetPath)
	{
		this.targetPath = targetPath;
	}

	private static int mergeIdCounter = 0;

	public void initMergeId()
	{
		if (getMergeId() == null)
			setMergeId("resource-" + mergeIdCounter++);
	}

	public boolean isFiltering()
	{
		return (this.filtering != null) ? Boolean.parseBoolean(this.filtering) : false;
	}

	public void setFiltering(boolean filtering)
	{
		this.filtering = String.valueOf(filtering);
	}

	public String toString()
	{
		return "Resource {targetPath: " + getTargetPath() + ", filtering: " + isFiltering() + ", " + super.toString() + "}";
	}
}
