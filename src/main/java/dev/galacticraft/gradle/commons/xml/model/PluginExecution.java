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

public class PluginExecution extends ConfigurationContainer implements Cloneable
{
	private String id = "default";

	private String phase;

	private int priority = 0;

	private List<String> goals;

	public static final String DEFAULT_EXECUTION_ID = "default";

	public void addGoal(String string)
	{
		getGoals().add(string);
	}

	public PluginExecution clone()
	{
		try
		{
			PluginExecution copy = (PluginExecution) super.clone();
			if (this.goals != null)
			{
				copy.goals = new ArrayList<>();
				copy.goals.addAll(this.goals);
			}
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public List<String> getGoals()
	{
		if (this.goals == null)
			this.goals = new ArrayList<>();
		return this.goals;
	}

	public String getId()
	{
		return this.id;
	}

	public String getPhase()
	{
		return this.phase;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public void removeGoal(String string)
	{
		getGoals().remove(string);
	}

	public void setGoals(List<String> goals)
	{
		this.goals = goals;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setPhase(String phase)
	{
		this.phase = phase;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String toString()
	{
		return getId();
	}
}
