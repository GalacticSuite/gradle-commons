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

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AndFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable
{

    private static final long serialVersionUID = 4781382227543838209L;
	private final List<IOFileFilter> fileFilters;

    public AndFileFilter()
    {
        this(0);
    }

    private AndFileFilter(final ArrayList<IOFileFilter> initialList)
    {
        this.fileFilters = Objects.requireNonNull(initialList, "initialList");
    }

    private AndFileFilter(final int initialCapacity)
    {
        this(new ArrayList<>(initialCapacity));
    }

    public AndFileFilter(final IOFileFilter filter1, final IOFileFilter filter2)
    {
        this(2);
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    public AndFileFilter(final IOFileFilter... fileFilters)
    {
        this(Objects.requireNonNull(fileFilters, "fileFilters").length);
        addFileFilter(fileFilters);
    }

    public AndFileFilter(final List<IOFileFilter> fileFilters)
    {
        this(new ArrayList<>(Objects.requireNonNull(fileFilters, "fileFilters")));
    }

    @Override
    public boolean accept(final File file)
    {
        if (isEmpty()) {
            return false;
        }
        for (final IOFileFilter fileFilter : fileFilters) {
            if (!fileFilter.accept(file)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean accept(final File file, final String name)
    {
        if (isEmpty()) {
            return false;
        }
        for (final IOFileFilter fileFilter : fileFilters) {
            if (!fileFilter.accept(file, name)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes)
    {
        if (isEmpty()) {
            return FileVisitResult.TERMINATE;
        }
        for (final IOFileFilter fileFilter : fileFilters) {
            if (fileFilter.accept(file, attributes) != FileVisitResult.CONTINUE) {
                return FileVisitResult.TERMINATE;
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public void addFileFilter(final IOFileFilter fileFilter)
    {
        this.fileFilters.add(Objects.requireNonNull(fileFilter, "fileFilter"));
    }

    public void addFileFilter(final IOFileFilter... fileFilters)
    {
        for (final IOFileFilter fileFilter : Objects.requireNonNull(fileFilters, "fileFilters")) {
            addFileFilter(fileFilter);
        }
    }

    @Override
    public List<IOFileFilter> getFileFilters()
    {
        return Collections.unmodifiableList(this.fileFilters);
    }

    private boolean isEmpty()
    {
        return this.fileFilters.isEmpty();
    }

    @Override
    public boolean removeFileFilter(final IOFileFilter ioFileFilter)
    {
        return this.fileFilters.remove(ioFileFilter);
    }

    @Override
    public void setFileFilters(final List<IOFileFilter> fileFilters)
    {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }

    @Override
    public String toString()
    {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        for (int i = 0; i < fileFilters.size(); i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(fileFilters.get(i));
        }
        buffer.append(")");
        return buffer.toString();
    }

}
