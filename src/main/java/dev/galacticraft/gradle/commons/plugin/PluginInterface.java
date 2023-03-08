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

import javax.annotation.Nonnull;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.util.GradleVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A more friendly interface for creating a {@link Plugin} that operates on a {@link Project}.
 * <p>
 * Implementations should override the non-deprecated overload of {@code apply}, but until {@code Convention} is
 * removed, either will work.
 * </p>
 *
 * @since 1.0.0
 */
public interface PluginInterface extends Plugin<Project>
{
    @Override
    @SuppressWarnings("deprecation") // workaround
    default void apply(final @Nonnull Project project)
    {
        // Check version
        GradleRequirement.require(this.minimumGradleVersion(), this, project.getDisplayName());

        if (Compatibility.HAS_CONVENTION) {
            this.project(project, project.getExtensions(), project.getConvention(), project.getTasks());
        } else {
            this.project(project, project.getPluginManager(), project.getExtensions(), project.getTasks());
        }
    }

    /**
     * Applies the plugin.
     *
     * @param project the project
     * @param extensions the extension container
     * @param convention the convention
     * @param tasks the task container
     * @since 1.0.0
     * @deprecated for removal since 1.1.0, use
     *             {@link #apply(Project, PluginContainer, ExtensionContainer, TaskContainer)} instead
     */
    @Deprecated
    default void project(final @Nonnull Project project, final @Nonnull ExtensionContainer extensions,
        final @Nonnull Convention convention, final @Nonnull TaskContainer tasks)
    {
        this.project(project, project.getPluginManager(), extensions, tasks);
    }

    /**
     * Applies the plugin.
     *
     * @param project the project
     * @param plugins the plugin manager
     * @param extensions the extension container
     * @param tasks the task container
     * @since 1.1.0
     */
    default void project(final @NotNull Project target, final @NotNull PluginManager plugins,
        final @NotNull ExtensionContainer extensions, final @NotNull TaskContainer tasks)
    {
    }

    /**
     * Return a minimum Gradle version required to use this plugin.
     *
     * @return the minimum required version
     * @since 1.2.0
     */
    default @Nullable GradleVersion minimumGradleVersion()
    {
        return null;
    }
}
