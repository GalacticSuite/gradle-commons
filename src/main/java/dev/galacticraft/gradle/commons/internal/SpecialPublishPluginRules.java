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

import org.gradle.api.Task;
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectPublicationRegistry;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Model;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;

public class SpecialPublishPluginRules extends RuleSource
{
    @Model
    PublishingExtension publishing(ExtensionContainer extensions)
    {
        return extensions.getByType(PublishingExtension.class);
    }

    @Model
    ProjectPublicationRegistry projectPublicationRegistry(ServiceRegistry serviceRegistry)
    {
        return serviceRegistry.get(ProjectPublicationRegistry.class);
    }

    @Mutate
    void addConfiguredPublicationsToProjectPublicationRegistry(ProjectPublicationRegistry projectPublicationRegistry,
        PublishingExtension extension)
    {
        // this rule is just here to ensure backwards compatibility for builds that
        // create publications with model rules
    }

    @Mutate
    void tasksDependOnProjectPublicationRegistry(ModelMap<Task> tasks, PublishingExtension extension)
    {
        // this rule is just here to ensure backwards compatibility for builds that
        // create publications with model rules
    }
}
