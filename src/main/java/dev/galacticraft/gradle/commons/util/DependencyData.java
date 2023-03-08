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

import java.io.File;
import java.util.Optional;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author FabricMC
 * @see <a href=
 *      "https://github.com/FabricMC/fabric-loom/blob/dev/0.11/src/main/java/net/fabricmc/loom/configuration/DependencyInfo.java">DependencyInfo</a>
 */
@Getter
@AllArgsConstructor
public class DependencyData
{
    final Dependency dependency;
    final Configuration sourceConfiguration;

    public static DependencyData create(Project project, String configuration)
    {
        return create(project.getConfigurations().getByName(configuration));
    }

    public static DependencyData create(Configuration configuration)
    {
        DependencySet dependencies = configuration.getDependencies();

        if (dependencies.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("Configuration '%s' has no dependencies", configuration.getName()));
        }

        if (dependencies.size() != 1) {
            throw new IllegalArgumentException(
                String.format("Configuration '%s' must only have 1 dependency", configuration.getName()));
        }

        return create(dependencies.iterator().next(), configuration);
    }

    public static DependencyData create(Dependency dependency, Configuration sourceConfiguration)
    {
        return new DependencyData(dependency, sourceConfiguration);
    }

    public Set<File> resolve()
    {
        return sourceConfiguration.files(dependency);
    }

    public Optional<File> resolveFile()
    {
        Set<File> files = resolve();

        if (files.isEmpty()) {
            return Optional.empty();
        } else if (files.size() > 1) {
            StringBuilder builder = new StringBuilder(this.toString());
            builder.append(" resolves to more than one file:");

            for (File f : files) {
                builder.append("\n\t-").append(f.getAbsolutePath());
            }

            throw new RuntimeException(builder.toString());
        } else {
            return files.stream().findFirst();
        }
    }

    @Override
    public String toString()
    {
        return getDepString();
    }

    public String getDepString()
    {
        return dependency.getGroup() + ":" + dependency.getName() + ":" + dependency.getVersion();
    }
}
