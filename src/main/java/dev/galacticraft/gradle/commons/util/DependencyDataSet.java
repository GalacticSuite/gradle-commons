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

package dev.galacticraft.gradle.commons.util;

import java.util.HashSet;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class DependencyDataSet extends HashSet<DependencyData>
{
	final Configuration sourceConfiguration;
	
	public static DependencyDataSet create(Project project, String configuration)
	{
		return create(project, project.getConfigurations().getByName(configuration));
	}

	public static DependencyDataSet create(Project project, Configuration configuration)
	{
		DependencySet dependencies = configuration.getDependencies();

		if (dependencies.isEmpty())
		{
			throw new IllegalArgumentException(String.format("Configuration '%s' has no dependencies", configuration.getName()));
		}

		return new DependencyDataSet(project, dependencies, configuration);
	}

	DependencyDataSet(Project project, DependencySet dependencySet, Configuration configuration)
	{
		this.sourceConfiguration = configuration;
		for(Dependency dep : dependencySet)
		{
		    add(DependencyData.create(dep, configuration));
		}
	}
}
