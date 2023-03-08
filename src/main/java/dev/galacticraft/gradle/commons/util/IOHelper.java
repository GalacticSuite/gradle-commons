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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;

import org.gradle.api.GradleException;

import dev.galacticraft.gradle.commons.project.GalacticProject;

public class IOHelper
{
    public static IOWrapper getIOWrapper(GalacticProject project, URL url, String filename, @Nullable String version)
    {
        return new IOWrapper(buildUrl(project, url, filename, version));
    }

    private static URL buildUrl(GalacticProject project, URL uri, String filename, @Nullable String version)
    {
        if (version != null) {
            try {
                return new URL(StringUtil.asPath(uri, project.toPath(version), filename));
            } catch (MalformedURLException e) {
                throw new GradleException(
                    "Malformed URL entry: " + StringUtil.asPath(uri, project.toPath(version), filename));
            }
        }

        try {
            return new URL(StringUtil.asPath(uri, project.toPath(), filename));
        } catch (MalformedURLException e) {
            throw new GradleException("Malformed URL entry: " + StringUtil.asPath(uri, project.toPath(), filename));
        }
    }

    public static URL toURL(URI uri)
    {
        try {
            return new URL(StringUtil.asPath(uri));
        } catch (MalformedURLException e) {
            throw new GradleException("Malformed URL entry: " + StringUtil.asPath(uri));
        }
    }
}
