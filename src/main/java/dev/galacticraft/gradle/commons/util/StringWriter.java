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

import java.io.Serializable;
import java.io.Writer;

public class StringWriter extends Writer implements Serializable
{

    private static final long serialVersionUID = 7255603777878318808L;

    private final StringBuilder builder;

    public StringWriter()
    {
        this.builder = new StringBuilder();
    }

    public StringWriter(final int capacity)
    {
        this.builder = new StringBuilder(capacity);
    }

    public StringWriter(final StringBuilder builder)
    {
        this.builder = builder != null ? builder : new StringBuilder();
    }

    @Override
    public Writer append(final char value)
    {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(final CharSequence value)
    {
        builder.append(value);
        return this;
    }

    @Override
    public Writer append(final CharSequence value, final int start, final int end)
    {
        builder.append(value, start, end);
        return this;
    }

    @Override
    public void close()
    {
    }

    @Override
    public void flush()
    {
    }

    @Override
    public void write(final String value)
    {
        if (value != null) {
            builder.append(value);
        }
    }

    @Override
    public void write(final char[] value, final int offset, final int length)
    {
        if (value != null) {
            builder.append(value, offset, length);
        }
    }

    public StringBuilder getBuilder()
    {
        return builder;
    }

    @Override
    public String toString()
    {
        return builder.toString();
    }
}
