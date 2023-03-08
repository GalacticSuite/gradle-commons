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

package dev.galacticraft.gradle.commons.internal;

import java.util.Set;

import javax.inject.Inject;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.NamedDomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.attributes.Usage;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.artifacts.Module;
import org.gradle.api.internal.artifacts.configurations.DependencyMetaDataProvider;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.tasks.TaskDependencyFactory;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlatformPlugin;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.publish.internal.versionmapping.DefaultVersionMappingStrategy;
import org.gradle.api.publish.internal.versionmapping.VersionMappingStrategyInternal;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.internal.artifact.MavenArtifactNotationParserFactory;
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publication.WritableMavenProjectIdentity;
import org.gradle.api.publish.maven.internal.publisher.MutableMavenProjectIdentity;
import org.gradle.api.publish.tasks.GenerateModuleMetadata;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.internal.instantiation.InstantiatorFactory;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.typeconversion.NotationParser;

import dev.galacticraft.gradle.commons.plugin.TeamConstants;

public class SpecialMavenPlugin implements Plugin<Project>
{

    public static final String               PUBLISH_LOCAL_TASK_NAME = "toMavenLocal";
    private final InstantiatorFactory        instantiator;
    private final ObjectFactory              objectFactory;
    private final DependencyMetaDataProvider dependencyMetaDataProvider;
    private final FileResolver               fileResolver;
    private final ImmutableAttributesFactory immutableAttributesFactory;
    private final ProviderFactory            providerFactory;
    private final TaskDependencyFactory taskDependencyFactory;

    @Inject
    public SpecialMavenPlugin(InstantiatorFactory instantiatorFactory, ObjectFactory objectFactory, DependencyMetaDataProvider dependencyMetaDataProvider, FileResolver fileResolver, ImmutableAttributesFactory immutableAttributesFactory, ProviderFactory providerFactory, TaskDependencyFactory taskDependencyFactory)
    {
        this.instantiator = instantiatorFactory;
        this.objectFactory = objectFactory;
        this.dependencyMetaDataProvider = dependencyMetaDataProvider;
        this.fileResolver = fileResolver;
        this.immutableAttributesFactory = immutableAttributesFactory;
        this.providerFactory = providerFactory;
        this.taskDependencyFactory = taskDependencyFactory;
    }

    @Override
    public void apply(final Project project)
    {
        project.getPluginManager().apply(SpecialPublishPlugin.class);

        project.getExtensions().configure(SpecialPublishExt.class, extension ->
        {
            extension.getPublications().registerFactory(MavenPublication.class, new MavenPublicationFactory(dependencyMetaDataProvider, instantiator.decorateLenient(), fileResolver, project.getPluginManager(), project.getExtensions()));
            realizePublishingTasksLater(project, extension);
        });
    }

    private void realizePublishingTasksLater(final Project project, final SpecialPublishExt extension)
    {
        final NamedDomainObjectSet<MavenPublicationInternal> mavenPublications = extension.getPublications().withType(MavenPublicationInternal.class);
        final TaskContainer tasks = project.getTasks();
        final DirectoryProperty buildDirectory = project.getLayout().getBuildDirectory();

        mavenPublications.all(publication ->
        {
            createGenerateMetadataTask(tasks, publication, mavenPublications, buildDirectory);
            createGeneratePomTask(tasks, publication, buildDirectory, project);
        });
    }

    private void createGeneratePomTask(TaskContainer tasks, final MavenPublicationInternal publication, final DirectoryProperty buildDir, final Project project)
    {
        final String publicationName = publication.getName();
        String descriptorTaskName = "generatePomFileFor" + capitalize(publicationName);
        TaskProvider<SpecialGenerateMavenPom> generatorTask = tasks.register(descriptorTaskName, SpecialGenerateMavenPom.class, generatePomTask ->
        {
            generatePomTask.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
            generatePomTask.setPom(publication.getPom());
            if (generatePomTask.getDestination() == null)
            {
                generatePomTask.setDestination(buildDir.file("publications/" + publication.getName() + "/pom-special-default.xml"));
            }
            project.getPluginManager().withPlugin("org.gradle.java", plugin ->
            {
                generatePomTask.withCompileScopeAttributes(immutableAttributesFactory.of(Usage.USAGE_ATTRIBUTE, objectFactory.named(Usage.class, Usage.JAVA_API))).withRuntimeScopeAttributes(immutableAttributesFactory.of(Usage.USAGE_ATTRIBUTE, objectFactory.named(Usage.class, Usage.JAVA_RUNTIME)));
            });
        });
        publication.setPomGenerator(generatorTask);
    }

    private void createGenerateMetadataTask(final TaskContainer tasks, final MavenPublicationInternal publication, final Set<? extends MavenPublicationInternal> publications, final DirectoryProperty buildDir)
    {
        final String publicationName = publication.getName();
        String descriptorTaskName = "generateMetadataFor" + capitalize(publicationName);
        TaskProvider<GenerateModuleMetadata> generatorTask = tasks.register(descriptorTaskName, GenerateModuleMetadata.class, generateTask ->
        {
            generateTask.getPublication().set(publication);
            generateTask.getPublications().set(publications);
            generateTask.getOutputFile().convention(buildDir.file("maven/publications/" + publication.getName() + "/module.json"));
        });
        publication.setModuleDescriptorGenerator(generatorTask);
    }

    private class MavenPublicationFactory implements NamedDomainObjectFactory<MavenPublication>
    {
        private final Instantiator               instantiator;
        private final DependencyMetaDataProvider dependencyMetaDataProvider;
        private final FileResolver               fileResolver;
        private final PluginManager              plugins;
        private final ExtensionContainer         extensionContainer;

        private MavenPublicationFactory(DependencyMetaDataProvider dependencyMetaDataProvider, Instantiator instantiator, FileResolver fileResolver, PluginManager plugins, ExtensionContainer extensionContainer)
        {
            this.dependencyMetaDataProvider = dependencyMetaDataProvider;
            this.instantiator = instantiator;
            this.fileResolver = fileResolver;
            this.plugins = plugins;
            this.extensionContainer = extensionContainer;
        }

        @Override
        public MavenPublication create(final String name)
        {
            MutableMavenProjectIdentity projectIdentity = createProjectIdentity();
            NotationParser<Object, MavenArtifact> artifactNotationParser = new MavenArtifactNotationParserFactory(instantiator, fileResolver, taskDependencyFactory).create();
            VersionMappingStrategyInternal versionMappingStrategy = objectFactory.newInstance(DefaultVersionMappingStrategy.class);
            configureDefaultConfigurationsUsedWhenMappingToResolvedVersions(versionMappingStrategy);
            return objectFactory.newInstance(DefaultMavenPublication.class, name, projectIdentity, artifactNotationParser, versionMappingStrategy);
        }

        private void configureDefaultConfigurationsUsedWhenMappingToResolvedVersions(VersionMappingStrategyInternal versionMappingStrategy)
        {
            plugins.withPlugin("org.gradle.java", plugin ->
            {
                SourceSet mainSourceSet = extensionContainer.getByType(SourceSetContainer.class).getByName(SourceSet.MAIN_SOURCE_SET_NAME);
                versionMappingStrategy.defaultResolutionConfiguration(Usage.JAVA_API, mainSourceSet.getCompileClasspathConfigurationName());
                versionMappingStrategy.defaultResolutionConfiguration(Usage.JAVA_RUNTIME, mainSourceSet.getRuntimeClasspathConfigurationName());
            });
            plugins.withPlugin("org.gradle.java-platform", plugin ->
            {
                versionMappingStrategy.defaultResolutionConfiguration(Usage.JAVA_API, JavaPlatformPlugin.CLASSPATH_CONFIGURATION_NAME);
                versionMappingStrategy.defaultResolutionConfiguration(Usage.JAVA_RUNTIME, JavaPlatformPlugin.CLASSPATH_CONFIGURATION_NAME);
            });
        }

        private MutableMavenProjectIdentity createProjectIdentity()
        {
            final Module module = dependencyMetaDataProvider.getModule();
            MutableMavenProjectIdentity projectIdentity = new WritableMavenProjectIdentity(objectFactory);
            projectIdentity.getGroupId().set(providerFactory.provider(module::getGroup));
            projectIdentity.getArtifactId().set(providerFactory.provider(module::getName));
            projectIdentity.getVersion().set(providerFactory.provider(module::getVersion));
            return projectIdentity;
        }
    }

    private String capitalize(final String str)
    {
        final int strLen = str == null ? 0 : str.length();
        if (strLen == 0)
        {
            return str;
        }
        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint)
        {
            return str;
        }
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen;)
        {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }
}
