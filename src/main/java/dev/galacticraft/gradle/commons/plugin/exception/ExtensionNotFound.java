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

package dev.galacticraft.gradle.commons.plugin.exception;

public class ExtensionNotFound extends NotFoundException
{

    private static final long serialVersionUID = 1L;

    private final static String PREFIX = "Cannot find extension by %s '%s'\n";

    private final static String HELPFUL =
        " -> You may have called '%s' before creating the extension or applying the Plugin that supplies it";

    public <T> ExtensionNotFound(Class<T> type, String callingMethod)
    {
        super(String.format(PREFIX, "type", type.getSimpleName()) + String.format(HELPFUL, callingMethod));
    }

    public ExtensionNotFound(String name, String callingMethod)
    {
        super(String.format(PREFIX, "name", name) + String.format(HELPFUL, callingMethod));
    }
}
