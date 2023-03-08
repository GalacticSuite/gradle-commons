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

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dev.galacticraft.gradle.commons.model.nexus.BasicNexusRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil
{

    public static String capitalize(String s)
    {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String uncapitalize(String s)
    {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static String asPath(URI uri)
    {
        return uri.toString();
    }

    public static String asPath(URL url, Object... objects)
    {
        int index = objects.length - 1;
        Object lastObj = objects[index];
        Object[] objs = new Object[index];
        System.arraycopy(objects, 0, objs, 0, index);

        StringBuilder builder = new StringBuilder();
        if (!url.toString().endsWith("/")) {
            builder.append(url.toString()).append("/");
        }
        builder.append(url.toString());

        for (Object object : objs) {
            String i = object.toString();
            if (i.endsWith("/")) {
                builder.append(object.toString());
            } else {
                builder.append(object.toString()).append("/");
            }
        }

        builder.append(lastObj.toString());
        return builder.toString();
    }

    public static String asPath(URI uri, Object... objects)
    {
        int index = objects.length - 1;
        Object lastObj = objects[index];
        Object[] objs = new Object[index];
        System.arraycopy(objects, 0, objs, 0, index);

        StringBuilder builder = new StringBuilder();
        if (!uri.toString().endsWith("/")) {
            builder.append(uri.toString()).append("/");
        }
        builder.append(uri.toString());

        for (Object object : objs) {
            String i = object.toString();
            if (i.endsWith("/")) {
                builder.append(object.toString());
            } else {
                builder.append(object.toString()).append("/");
            }
        }

        builder.append(lastObj.toString());
        return builder.toString();
    }

    public static String getCleanedName(BasicNexusRepository repository)
    {
        if (repository.getName().split("-").length > 0) {
            String[] parts = repository.getName().split("-");
            List<String> partList = new ArrayList<>();
            partList.add(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                partList.add(StringUtil.capitalize(parts[i]));
            }
            return String.join("", partList);
        }

        return repository.getName();
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr)
    {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring,
        int start, int length)
    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }
        if (srcLen < length || otherLen < length) {
            return false;
        }
        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);
            if (c1 == c2) {
                continue;
            }
            if (!ignoreCase) {
                return false;
            }
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }
        return true;
    }
}
