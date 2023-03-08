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

import java.util.Objects;

public enum IOCase
{

    SENSITIVE("Sensitive", true),

    INSENSITIVE("Insensitive", false),

    SYSTEM("System", !FileUtility.isSystemWindows());

    public static boolean isCaseSensitive(final IOCase caseSensitivity)
    {
        return caseSensitivity != null && !caseSensitivity.isCaseSensitive();
    }

    private static final long serialVersionUID = -6343169151696340687L;

    private final String name;

    private final transient boolean sensitive;

    public static IOCase forName(final String name)
    {
        for (final IOCase ioCase : IOCase.values()) {
            if (ioCase.getName().equals(name)) {
                return ioCase;
            }
        }
        throw new IllegalArgumentException("Invalid IOCase name: " + name);
    }

    IOCase(final String name, final boolean sensitive)
    {
        this.name = name;
        this.sensitive = sensitive;
    }

    private Object readResolve()
    {
        return forName(name);
    }

    public String getName()
    {
        return name;
    }

    public boolean isCaseSensitive()
    {
        return sensitive;
    }

    public int checkCompareTo(final String str1, final String str2)
    {
        Objects.requireNonNull(str1, "str1");
        Objects.requireNonNull(str2, "str2");
        return sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
    }

    public boolean checkEquals(final String str1, final String str2)
    {
        Objects.requireNonNull(str1, "str1");
        Objects.requireNonNull(str2, "str2");
        return sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
    }

    public boolean checkStartsWith(final String str, final String start)
    {
        return str != null && start != null && str.regionMatches(!sensitive, 0, start, 0, start.length());
    }

    public boolean checkEndsWith(final String str, final String end)
    {
        if (str == null || end == null) {
            return false;
        }
        final int endLen = end.length();
        return str.regionMatches(!sensitive, str.length() - endLen, end, 0, endLen);
    }

    public int checkIndexOf(final String str, final int strStartIndex, final String search)
    {
        final int endIndex = str.length() - search.length();
        if (endIndex >= strStartIndex) {
            for (int i = strStartIndex; i <= endIndex; i++) {
                if (checkRegionMatches(str, i, search)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkRegionMatches(final String str, final int strStartIndex, final String search)
    {
        return str.regionMatches(!sensitive, strStartIndex, search, 0, search.length());
    }

    @Override
    public String toString()
    {
        return name;
    }

}
