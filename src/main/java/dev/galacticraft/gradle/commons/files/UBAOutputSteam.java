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
import java.io.InputStream;
import java.io.OutputStream;

public final class UBAOutputSteam extends AbstractByteArrayOutputStream
{

    public UBAOutputSteam()
    {
        this(DEFAULT_SIZE);
    }

    public UBAOutputSteam(final int size)
    {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        needNewBuffer(size);
    }

    @Override
    public void write(final byte[] b, final int off, final int len)
    {
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", off, len));
        }
        if (len == 0) {
            return;
        }
        writeImpl(b, off, len);
    }

    @Override
    public void write(final int b)
    {
        writeImpl(b);
    }

    @Override
    public int write(final InputStream in) throws IOException
    {
        return writeImpl(in);
    }

    @Override
    public int size()
    {
        return count;
    }

    @Override
    public void reset()
    {
        resetImpl();
    }

    @Override
    public void writeTo(final OutputStream out) throws IOException
    {
        writeToImpl(out);
    }

    public static InputStream toBufferedInputStream(final InputStream input) throws IOException
    {
        return toBufferedInputStream(input, DEFAULT_SIZE);
    }

    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException
    {
        try (final UBAOutputSteam output = new UBAOutputSteam(size)) {
            output.write(input);
            return output.toInputStream();
        }
    }

    @Override
    public InputStream toInputStream()
    {
        return toInputStream(UBAInputStream::new);
    }

    @Override
    public byte[] toByteArray()
    {
        return toByteArrayImpl();
    }
}
