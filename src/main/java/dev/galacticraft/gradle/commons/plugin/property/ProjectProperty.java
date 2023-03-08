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

package dev.galacticraft.gradle.commons.plugin.property;

import java.util.function.Supplier;

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;

public abstract class ProjectProperty<T extends Object> implements Supplier<T>
{
    private final Project project;

    private final String property;

    private T value;

    ProjectProperty(T property)
    {
        this.project = null;
        this.property = null;
        this.value = property;
    }

    ProjectProperty(Project project, String property)
    {
        this.project = project;
        this.property = property;
    }

    protected void set(T value)
    {
        this.value = value;
        setProperty(value);
    }

    private void setProperty(Object value)
    {
        ExtraPropertiesExtension ext = project.getExtensions().findByType(ExtraPropertiesExtension.class);
        if (!ext.has(property)) {
            ext.set(property, value);
        }
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public T get()
    {
        return value;
    }
}
