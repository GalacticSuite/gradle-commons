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

package dev.galacticraft.gradle.commons.plugin.helpers;

import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository.MetadataSources;

public class RepositoryHelper
{

    private final RepositoryHandler repositories;

    public RepositoryHelper(RepositoryHandler repositories)
    {
        this.repositories = repositories;
    }

    public RepositoryHandler handler()
    {
        return this.repositories;
    }

    public MavenArtifactRepository addMaven(Action<MavenArtifactRepository> action)
    {
        return this.repositories.maven(action);
    }

    /**
     * Adds a new maven Repository to the project
     *
     * @param name the name
     * @param url the url
     * @return the maven artifact repository
     */
    public MavenArtifactRepository addMaven(String name, String url)
    {
        return this.repositories.maven(maven -> {
            maven.setName(name);
            maven.setUrl(url);
            maven.metadataSources(Metadata.ALL.set());
        });
    }

    /**
     * Adds a new maven Repository to the project
     *
     * @param url the url
     * @return the maven artifact repository
     */
    public MavenArtifactRepository addMaven(String url)
    {
        return this.repositories.maven(maven -> {
            maven.setUrl(url);
            maven.metadataSources(Metadata.ALL.set());
        });
    }

    /**
     * Adds a new maven Repository to the project
     *
     * @param name the name
     * @param url the url
     * @param sources the sources
     * @return the maven artifact repository
     */
    public MavenArtifactRepository addMaven(String name, String url, Metadata... sources)
    {
        return this.repositories.maven(maven -> {
            maven.setName(name);
            maven.setUrl(url);
            maven.metadataSources(Metadata.ALL.set());
        });
    }

    /**
     * Adds a new maven Repository to the project
     *
     * @param url the url
     * @param sources the sources
     * @return the maven artifact repository
     */
    public MavenArtifactRepository addMaven(String url, Metadata... sources)
    {
        return this.repositories.maven(maven -> {
            maven.setUrl(url);
            for (Metadata metadata : sources) {
                maven.metadataSources(metadata.set());
            }
        });
    }

    public MavenArtifactRepository addLocalMaven(Action<MavenArtifactRepository> action)
    {
        return this.repositories.mavenLocal(action);
    }

    public MavenArtifactRepository addLocalMaven()
    {
        return this.repositories.mavenLocal();
    }

    public static enum Metadata
    {

        GRADLE {
            @Override
            public Action<MetadataSources> set()
            {
                return MetadataSources::gradleMetadata;
            }
        },

        POM() {
            @Override
            public Action<MetadataSources> set()
            {
                return MetadataSources::mavenPom;
            }
        },

        ARTIFACT() {
            @Override
            public Action<MetadataSources> set()
            {
                return MetadataSources::artifact;
            }
        },

        ALL {
            @Override
            public Action<MetadataSources> set()
            {
                return new Action<MetadataSources>()
                {
                    @Override
                    public void execute(MetadataSources metadataSources)
                    {
                        metadataSources.gradleMetadata();
                        metadataSources.mavenPom();
                        metadataSources.artifact();
                    }
                };
            }
        };

        public abstract Action<MetadataSources> set();
    }
}
