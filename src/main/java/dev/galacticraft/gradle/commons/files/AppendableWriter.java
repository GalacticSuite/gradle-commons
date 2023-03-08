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

package dev.galacticraft.gradle.commons.files;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class AppendableWriter<T extends Appendable> extends Writer
{

    private final T appendable;

    public AppendableWriter(final T appendable)
    {
        this.appendable = appendable;
    }

    @Override
    public Writer append(final char c) throws IOException
    {
        appendable.append(c);
        return this;
    }

    @Override
    public Writer append(final CharSequence csq) throws IOException
    {
        appendable.append(csq);
        return this;
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws IOException
    {
        appendable.append(csq, start, end);
        return this;
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void flush() throws IOException
    {
    }

    public T getAppendable()
    {
        return appendable;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException
    {
        Objects.requireNonNull(cbuf, "Character array is missing");
        if (len < 0 || (off + len) > cbuf.length) {
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        for (int i = 0; i < len; i++) {
            appendable.append(cbuf[off + i]);
        }
    }

    @Override
    public void write(final int c) throws IOException
    {
        appendable.append((char) c);
    }

    @Override
    public void write(final String str, final int off, final int len) throws IOException
    {
        Objects.requireNonNull(str, "String is missing");
        appendable.append(str, off, off + len);
    }

}
