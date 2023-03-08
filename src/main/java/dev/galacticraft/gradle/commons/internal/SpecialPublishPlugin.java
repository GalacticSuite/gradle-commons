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

import javax.inject.Inject;

import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.artifacts.ArtifactPublicationServices;
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectPublicationRegistry;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.internal.DefaultPublicationContainer;
import org.gradle.api.publish.internal.PublicationInternal;
import org.gradle.internal.Cast;
import org.gradle.internal.model.RuleBasedPluginListener;
import org.gradle.internal.reflect.Instantiator;

public class SpecialPublishPlugin implements Plugin<Project>
{

    private static final String               VALID_NAME_REGEX = "[A-Za-z0-9_\\-.]+";

    private final Instantiator                instantiator;

    private final ArtifactPublicationServices publicationServices;

    private final ProjectPublicationRegistry  projectPublicationRegistry;

    private CollectionCallbackActionDecorator collectionCallbackActionDecorator;

    @Inject
    public SpecialPublishPlugin(ArtifactPublicationServices publicationServices, Instantiator instantiator, ProjectPublicationRegistry projectPublicationRegistry, CollectionCallbackActionDecorator collectionCallbackActionDecorator)
    {
        this.publicationServices = publicationServices;
        this.instantiator = instantiator;
        this.projectPublicationRegistry = projectPublicationRegistry;
        this.collectionCallbackActionDecorator = collectionCallbackActionDecorator;
    }

    @Override
    public void apply(final Project project)
    {
        RepositoryHandler repositories = publicationServices.createRepositoryHandler();
        PublicationContainer publications = instantiator.newInstance(DefaultPublicationContainer.class, instantiator, collectionCallbackActionDecorator);
        SpecialPublishExt extension = project.getExtensions().create(SpecialPublishExt.class, SpecialPublishExt.NAME, SpecialPublishExtension.class, repositories, publications);

        extension.getPublications().all(publication ->
        {
            PublicationInternal<?> internalPublication = Cast.uncheckedNonnullCast(publication);
            ProjectInternal projectInternal = (ProjectInternal) project;
            projectPublicationRegistry.registerPublication(projectInternal, internalPublication);
        });
        bridgeToSoftwareModelIfNeeded((ProjectInternal) project);
        validatePublishingModelWhenComplete(project, extension);
    }

    private void validatePublishingModelWhenComplete(Project project, SpecialPublishExt extension)
    {
        project.afterEvaluate(projectAfterEvaluate ->
        {
            for (ArtifactRepository repository : extension.getRepositories())
            {
                String repositoryName = repository.getName();
                if (!repositoryName.matches(VALID_NAME_REGEX))
                {
                    throw new InvalidUserDataException("Repository name '" + repositoryName + "' is not valid for publication. Must match regex " + VALID_NAME_REGEX + ".");
                }
            }
            for (Publication publication : extension.getPublications())
            {
                String publicationName = publication.getName();
                if (!publicationName.matches(VALID_NAME_REGEX))
                {
                    throw new InvalidUserDataException("Publication name '" + publicationName + "' is not valid for publication. Must match regex " + VALID_NAME_REGEX + ".");
                }
            }
        });
    }

    private void bridgeToSoftwareModelIfNeeded(ProjectInternal project)
    {
        project.addRuleBasedPluginListener(new RuleBasedPluginListener()
        {
            @Override
            public void prepareForRuleBasedPlugins(Project project)
            {
                project.getPluginManager().apply(SpecialPublishPluginRules.class);
            }
        });
    }

}
