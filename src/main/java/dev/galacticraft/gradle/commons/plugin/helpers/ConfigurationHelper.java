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

package dev.galacticraft.gradle.commons.plugin.helpers;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.UnknownConfigurationException;
import org.gradle.api.provider.Provider;

public class ConfigurationHelper
{
    private final Project project;

    private final ConfigurationContainer configurations;

    public ConfigurationHelper(Project project)
    {
        this.project = project;
        this.configurations = project.getConfigurations();
    }

    public ConfigurationContainer container()
    {
        return this.configurations;
    }

    public Configuration findOrCreate(final String name)
    {
        Configuration configuration = configurations.findByName(name);
        if (configuration == null) {
            configuration = configurations.create(name);
        }
        return configuration;
    }

    public Configuration findOrCreate(final String name, final Action<Configuration> action)
    {
        Configuration configuration = configurations.findByName(name);
        if (configuration == null) {
            configuration = configurations.create(name, action);
        }
        return configuration;
    }

    public Provider<Configuration> findOrRegister(final String name)
    {
        Configuration configuration = configurations.findByName(name);
        if (configuration == null) {
            return configurations.register(name);
        }
        return project.provider(() -> configuration);
    }

    public Provider<Configuration> findOrRegister(final String name, final Action<Configuration> action)
    {
        Configuration configuration = configurations.findByName(name);
        if (configuration == null) {
            return configurations.register(name, action);
        }
        action.execute(configuration);
        return project.provider(() -> configuration);
    }

    public boolean hasConfiguration(String name)
    {
        try {
            configurations.getByName(name);
        } catch (UnknownConfigurationException e) {
            return false;
        }
        return true;
    }
}
