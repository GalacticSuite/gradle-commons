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

import org.gradle.api.plugins.ExtensionContainer;

import dev.galacticraft.gradle.commons.plugin.exception.ExtensionNotFound;

public class ExtensionsHelper
{

    private final ExtensionContainer extensions;

    public ExtensionsHelper(ExtensionContainer extensions)
    {
        this.extensions = extensions;
    }

    public ExtensionContainer container()
    {
        return this.extensions;
    }

    public <E> E findOrCreate(final String name, final Class<E> type, Object... arguments)
    {
        E extension = extensions.findByType(type);
        if (extension == null) {
            extension = extensions.create(name, type, arguments);
        }
        return extension;
    }

    public <E> E findOrCreate(final String name, final Class<E> type)
    {
        E extension = extensions.findByType(type);
        if (extension == null) {
            extension = extensions.create(name, type);
        }
        return extension;
    }

    @SuppressWarnings("unchecked")
    public <E> E findOrCreate(final String name, final Class<? super E> publicType, final Class<E> implementationType)
    {
        E extension = (E) extensions.findByType(publicType);
        if (extension == null) {
            extension = (E) extensions.create(publicType, name, implementationType);
        }
        return extension;
    }

    @SuppressWarnings("unchecked")
    public <E> E findOrCreate(final String name, final Class<? super E> publicType, final Class<E> implementationType,
        Object... arguments)
    {
        E extension = (E) extensions.findByType(publicType);
        if (extension == null) {
            extension = (E) extensions.create(publicType, name, implementationType, arguments);
        }
        return extension;
    }

    /**
     * Attempts to find the extension provided but will not crash with excemption if not found Logs a ExtensionNotFound
     * error to the console
     *
     * @param <T> the generic type
     * @param type the type
     * @return the extension
     */
    public <T> T find(Class<T> type)
    {
        if (extensions.findByType(type) == null)
            throw new ExtensionNotFound(type, "getExtension(Class<T> type)");
        return extensions.findByType(type);
    }

    /**
     * Attempts to find the extension provided but will not crash with excemption if not found Logs a ExtensionNotFound
     * error to the console
     *
     * @param name the name
     * @return the extension
     */
    public Object find(String name)
    {
        if (extensions.findByName(name) == null)
            throw new ExtensionNotFound(name, "getExtension(String name)");
        return extensions.findByName(name);
    }
}
