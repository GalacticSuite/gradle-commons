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

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public class StringProperty extends ProjectProperty<String>
{

    public StringProperty(Property<String> property)
    {
        super(property.get());
    }

    @Inject
    public StringProperty(Project project, String property)
    {
        super(project, property);
        Object prop = project.findProperty(property);
        if (prop != null) {
            this.set(String.valueOf(prop));
        } else {
            this.set("unspecified");
        }
    }

    public boolean notNull()
    {
        return this.get() != null;
    }

    public boolean equals(String arg, boolean ignoreCase)
    {
        if (ignoreCase)
            return this.get().equalsIgnoreCase(arg);
        else
            return this.get().equals(arg);
    }
}
