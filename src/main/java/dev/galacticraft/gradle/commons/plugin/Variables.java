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

package dev.galacticraft.gradle.commons.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.gradle.api.Project;

import com.google.common.collect.ImmutableMap;

import dev.galacticraft.gradle.commons.util.Checks;
import lombok.Setter;

public class Variables
{
    @Setter
    private boolean debug;
    
    private final ImmutableMap<String, String> knownVariables; 

    public Variables(Project target)
    {
        this.debug = false;
        final Map<String,String> holder = new HashMap<>();
        holder.putAll(System.getenv());
        for(Map.Entry<String, ?> entry : target.getProperties().entrySet())
        {
            if(!entry.getKey().equalsIgnoreCase("properties"))
            {
                holder.put(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : "NULL");
            }
        }
        this.knownVariables = ImmutableMap.copyOf(holder);
    }

    @Nullable
    @CheckForNull
    public String get(@Nonnull String name)
    {
        return knownVariables.get(name);
    }

    public boolean has(@Nonnull String name)
    {
        Checks.notNull(name, "Argument `%s` passed to Varibles.has(String) is null", name);
        return knownVariables.containsKey(name);
    }

    @Nullable
    @Deprecated
    public final String getString(final String name)
    {
        return get(name);
    }
}
