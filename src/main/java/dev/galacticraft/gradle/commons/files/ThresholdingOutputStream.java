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
import java.io.OutputStream;

public class ThresholdingOutputStream extends OutputStream
{

    private static final IOFunction<ThresholdingOutputStream, OutputStream> NOOP_OS_GETTER =
        os -> NullOutputStream.NULL_OUTPUT_STREAM;

    private final int threshold;

    private final IOConsumer<ThresholdingOutputStream> thresholdConsumer;

    private final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter;

    private long written;

    private boolean thresholdExceeded;

    public ThresholdingOutputStream(final int threshold)
    {
        this(threshold, IOConsumer.noop(), NOOP_OS_GETTER);
    }

    public ThresholdingOutputStream(final int threshold, final IOConsumer<ThresholdingOutputStream> thresholdConsumer,
        final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter)
    {
        this.threshold = threshold;
        this.thresholdConsumer = thresholdConsumer == null ? IOConsumer.noop() : thresholdConsumer;
        this.outputStreamGetter = outputStreamGetter == null ? NOOP_OS_GETTER : outputStreamGetter;
    }

    protected void checkThreshold(final int count) throws IOException
    {
        if (!thresholdExceeded && written + count > threshold) {
            thresholdExceeded = true;
            thresholdReached();
        }
    }

    @Override
    public void close() throws IOException
    {
        try {
            flush();
        } catch (final IOException ignored) {
        }
        getStream().close();
    }

    @Override
    public void flush() throws IOException
    {
        getStream().flush();
    }

    public long getByteCount()
    {
        return written;
    }

    protected OutputStream getStream() throws IOException
    {
        return outputStreamGetter.apply(this);
    }

    public int getThreshold()
    {
        return threshold;
    }

    public boolean isThresholdExceeded()
    {
        return written > threshold;
    }

    protected void resetByteCount()
    {
        this.thresholdExceeded = false;
        this.written = 0;
    }

    protected void setByteCount(final long count)
    {
        this.written = count;
    }

    protected void thresholdReached() throws IOException
    {
        thresholdConsumer.accept(this);
    }

    @Override
    public void write(final byte[] b) throws IOException
    {
        checkThreshold(b.length);
        getStream().write(b);
        written += b.length;
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException
    {
        checkThreshold(len);
        getStream().write(b, off, len);
        written += len;
    }

    @Override
    public void write(final int b) throws IOException
    {
        checkThreshold(1);
        getStream().write(b);
        written++;
    }
}
