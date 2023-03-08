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

package dev.galacticraft.gradle.commons.plugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor;
import org.gradle.internal.impldep.org.junit.internal.Checks;

import dev.galacticraft.gradle.commons.model.NexusManager;
import dev.galacticraft.gradle.commons.model.RepositoryCredentials;
import dev.galacticraft.gradle.commons.plugin.helpers.RepositoryHelper.Metadata;
import dev.galacticraft.gradle.commons.project.GalacticProject;
import dev.galacticraft.gradle.commons.version.Version;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamConstants
{
    public static final Version version(Object object)
    {
        return Version.of(object.getClass().getPackage().getImplementationVersion());
    }

    @UtilityClass
    public static class Properties
    {
        public static final String GRADLE_TASK_GROUP = "galacticgradle";

        public static final String GITHUB_ORG_NAME = "TeamGalacticraft";

        public static final String GITHUB_ORG_URL = "https://github.com/TeamGalacticraft";

        public static final String MAVEN_URL_BASE = "https://maven.galacticraft.net/repository/";

        public static final String TASK_GROUP_NAME = "galactic suite";
    }

    @UtilityClass
    public static class SignatoryProperties
    {
        public static final String SIGNING_KEY = "galacticSigningKey";

        public static final String SIGNING_PASSWORD = "galacticSigningPassword";

        public static final String KEY_STORE = "galacticKeyStore";

        public static final String KEY_STORE_ALIAS = "galacticKeyStoreAlias";

        public static final String KEY_STORE_PASSWORD = "galacticKeyStorePassword";
    }

    @UtilityClass
    public static class Repositories
    {

        public static Action<MavenArtifactRepository> getRepository(String id)
        {
            Checks.notNull(NexusManager.getHostedRepository(id), "NexusManager returned Null on Repository" + id);
            return maven -> {
                maven.setUrl(URI(NexusManager.getHostedRepository(id)));
                maven.metadataSources(Metadata.ALL.set());
            };
        }

        @UtilityClass
        public static class Groups
        {

            public static URI uriGradle = URI("gradle/");

            public static final Action<MavenArtifactRepository> gradle = maven -> {
                maven.setName("gc_gradle");
                maven.setUrl(uriGradle);
                maven.metadataSources(Metadata.ALL.set());
            };

            //// -- ////

            public static URI uriCommonProxy = URI("common-proxy/");

            /**
             * Adds the common-proxy repository to your Project. This group contains the following repositories:
             * <p>
             * <Strong>Gradle Plugins</Strong><br>
             * &emsp;-> <i>https://plugins.gradle.org/m2/</i><br>
             * <Strong>Forge</Strong><br>
             * &emsp;-> <i>https://maven.minecraftforge.net/</i><br>
             * <Strong>Fabric</Strong><br>
             * &emsp;-> <i>https://maven.fabricmc.net/</i><br>
             * <Strong>FancyGradle</Strong><br>
             * &emsp;-> <i>https://gitlab.com/api/v4/projects/26758973/packages/maven/</i><br>
             * <Strong>CurseMaven</Strong><br>
             * &emsp;-> <i>https://cursemaven.com/</i><br>
             * <Strong>QuiltMC Snapshots</Strong><br>
             * &emsp;-> <i>https://maven.quiltmc.org/repository/snapshot/</i><br>
             * <Strong>QuiltMC Releases</Strong><br>
             * &emsp;-> <i>https://maven.quiltmc.org/repository/release/</i><br>
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> commonProxy = maven -> {
                maven.setName("common_proxy");
                maven.setUrl(uriCommonProxy);
                maven.metadataSources(Metadata.ALL.set());
            };

            //// -- ////

            public static URI uriMavenPublic = URI("maven-public/");

            /**
             * Adds the maven-public repository to your Project.<br>
             * This group contains every hosted and proxy repositories available on TeamGalacticraft Maven
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> mavenPublic = maven -> {
                maven.setName("maven-public");
                maven.setUrl(uriMavenPublic);
                maven.metadataSources(Metadata.ALL.set());
            };

            //// -- ////

            public static URI uriLegacy = URI("legacy/");

            /**
             * Adds the legacy repository to your Project. This group contains the following repositories:
             * <p>
             * <Strong>Legacy Common</Strong><br>
             * &emsp;-> {@link Constans.Repositories.Hosted#legacyCommon() Repositories.Hosted.mavenCommon()}<br>
             * <Strong>Legacy Releases</Strong><br>
             * &emsp;-> {@link Constans.Repositories.Hosted#legacyCommon() Repositories.Hosted.legacyReleases()}<br>
             * <Strong>Legacy Snapshots</Strong><br>
             * &emsp;-> {@link Constans.Repositories.Hosted#legacyCommon() Repositories.Hosted.legacySnapshots()}<br>
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacy = maven -> {
                maven.setName("legacy");
                maven.setUrl(uriLegacy);
                maven.metadataSources(Metadata.ALL.set());
            };
            
            public static final Action<MavenArtifactRepository> legacy(Action<RepositoryContentDescriptor> action)
            {
                return maven -> {
                    maven.setName("legacy");
                    maven.setUrl(uriLegacy);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.content(action);
                };
            }
        }

        @UtilityClass
        public static class Hosted
        {
            public static URI uriMavenReleases = URI("maven-releases/");

            /**
             * Adds the maven-releases repository to your Project.
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> mavenReleases = maven -> {
                maven.setName("maven-releases");
                maven.setUrl(uriMavenReleases);
                maven.metadataSources(Metadata.ALL.set());
            };

            /**
             * Adds the maven-releases repository to your Project with the provided Credentials.
             * 
             * @apiNote Should only used when adding as a publishing repository
             * @param nexusLogin RepositoryCredentials
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> mavenReleases(RepositoryCredentials nexusLogin)
            {
                return maven -> {
                    maven.setName("maven-releases");
                    maven.setUrl(uriMavenReleases);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.credentials(nexusLogin.get());
                };
            };

            //// -- ////

            public static URI uriMavenSnapshots = URI("maven-snapshots/");

            /**
             * Adds the maven-snapshots repository to your Project.
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> mavenSnapshots = maven -> {
                maven.setName("maven-snapshots");
                maven.setUrl(uriMavenSnapshots);
                maven.metadataSources(Metadata.ALL.set());
            };

            /**
             * Adds the maven-snapshots repository to your Project with the provided Credentials.
             * 
             * @apiNote Should only used when adding as a publishing repository
             * @param nexusLogin RepositoryCredentials
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> mavenSnapshots(RepositoryCredentials nexusLogin)
            {
                return maven -> {
                    maven.setName("maven-snapshots");
                    maven.setUrl(uriMavenSnapshots);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.credentials(nexusLogin.get());
                };
            };

            //// -- ////

            public static URI uriLegacyReleases = URI("legacy-releases/");

            /**
             * Adds the legacy-releases repository to your Project.
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacyReleases = maven -> {
                maven.setName("legacy-releases");
                maven.setUrl(uriLegacyReleases);
                maven.metadataSources(Metadata.ALL.set());
            };

            /**
             * Adds the legacy-releases repository to your Project with the provided Credentials.
             * 
             * @apiNote Should only used when adding as a publishing repository
             * @param nexusLogin RepositoryCredentials
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacyReleases(RepositoryCredentials nexusLogin)
            {
                return maven -> {
                    maven.setName("legacy-releases");
                    maven.setUrl(uriLegacyReleases);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.credentials(nexusLogin.get());
                };
            };

            //// -- ////

            public static URI uriLegacySnapshots = URI("legacy-snapshots/");

            /**
             * Adds the legacy-snapshots repository to your Project.
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacySnapshots = maven -> {
                maven.setName("legacy-snapshots");
                maven.setUrl(uriLegacySnapshots);
                maven.metadataSources(Metadata.ALL.set());
            };

            /**
             * Adds the legacy-snapshots repository to your Project with the provided Credentials.
             * 
             * @apiNote Should only used when adding as a publishing repository
             * @param nexusLogin RepositoryCredentials
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacySnapshots(RepositoryCredentials nexusLogin)
            {
                return maven -> {
                    maven.setName("legacy-snapshots");
                    maven.setUrl(uriLegacySnapshots);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.credentials(nexusLogin.get());
                };
            };

            //// -- ////

            public static URI uriLegacyCommon = URI("legacy-common/");

            /**
             * Adds the legacy-common repository to your Project.
             * 
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacyCommon = maven -> {
                maven.setName("legacy-common");
                maven.setUrl(uriLegacyCommon);
                maven.metadataSources(Metadata.ALL.set());
            };

            /**
             * Adds the legacy-common repository to your Project with the provided Credentials.
             * 
             * @apiNote Should only used when adding as a publishing repository
             * @param nexusLogin RepositoryCredentials
             * @return MavenArtifactRepository
             */
            public static final Action<MavenArtifactRepository> legacyCommon(RepositoryCredentials nexusLogin)
            {
                return maven -> {
                    maven.setName("legacy-common");
                    maven.setUrl(uriLegacyCommon);
                    maven.metadataSources(Metadata.ALL.set());
                    maven.credentials(nexusLogin.get());
                };
            };
        }

        private static URI URI(String url)
        {
            try {
                return new URI(Properties.MAVEN_URL_BASE + url);
            } catch (URISyntaxException e) {
                throw new GradleException("Could not build URI from: " + url);
            }
        }
    }

    @UtilityClass
    public static class Projects
    {
        public static final GalacticProject GALACTICRAFT_LEGACY = GalacticProject.create("galacticraft-legacy");

        public static final GalacticProject GALACTICRAFT = GalacticProject.create("galacticraft");

        public static final GalacticProject GALACTICRAFT_API = GalacticProject.create("galacticraft-api");

        public static final GalacticProject DYNAMIC_DIMENSIONS = GalacticProject.create("dyndims");

        public static final GalacticProject DYNAMIC_DIMENSIONS_COMMON = GalacticProject.create("dyndims-common");

        public static final GalacticProject DYNAMIC_DIMENSIONS_FORGE = GalacticProject.create("dyndims-forge");

        public static final GalacticProject DYNAMIC_WORLDS = GalacticProject.create("dynworlds");

        public static final GalacticProject MACHINE_LIB = GalacticProject.create("machinelib");
    }

    /**
     * <p>
     * Ids must conform to the following requirements:
     * </p>
     * <ul>
     * <li>Must be between 2 and 64 characters in length</li>
     * <li>Must start with a lower case letter (a-z)</li>
     * <li>May only contain a mix of lower case letters (a-z), numbers (0-9), dashes (-), and underscores (_)</li>
     * </ul>
     */
    public static final Pattern VALID_ID_PATTERN = Pattern.compile("^[a-z][a-z0-9-_]{1,63}$");

    public static final String INVALID_ID_REQUIREMENTS_MESSAGE =
        "IDs can be between 2 and 64 characters long and must start with a lower case"
            + " letter, followed by any mix of lower case letters (a-z), numbers (0-9), dashes (-) and underscores (_).";
}
