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

import java.util.Optional;

import dev.galacticraft.gradle.commons.xml.model.Dependency;

public class MavenDependency
{
    private Optional<String> groupId;

    private Optional<String> artifactId;

    private Optional<String> version;

    private Optional<String> classifier;

    private Optional<String> scope;

    public static MavenDependency from(Dependency dependency)
    {
        return new MavenDependency(dependency);
    }

    private MavenDependency(Dependency dependency)
    {
        this.groupId = Optional.ofNullable(dependency.getGroupId());
        this.artifactId = Optional.ofNullable(dependency.getArtifactId());
        this.version = Optional.ofNullable(dependency.getVersion());
        this.classifier = Optional.ofNullable(dependency.getClassifier());
        this.scope = Optional.ofNullable(dependency.getScope());
    }

    public String getGroupId()
    {
        return groupId.orElse("");
    }

    public String getArtifactId()
    {
        return artifactId.orElse("");
    }

    public String getVersion()
    {
        return version.orElse("");
    }

    public Optional<String> getClassifier()
    {
        return classifier;
    }

    public String getScope()
    {
        return scope.orElse("");
    }

    public String toDependencyNotation()
    {
        if (getClassifier().isPresent()) {
            return String.format("%s:%s:%s:%s", getGroupId(), getArtifactId(), getVersion(), getClassifier().get());
        } else {
            return String.format("%s:%s:%s", getGroupId(), getArtifactId(), getVersion());
        }
    }
}
