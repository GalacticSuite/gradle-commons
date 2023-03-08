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

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.logging.Logger;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import dev.galacticraft.gradle.commons.plugin.helpers.ConfigurationHelper;
import dev.galacticraft.gradle.commons.plugin.helpers.ExtensionsHelper;
import dev.galacticraft.gradle.commons.plugin.helpers.RepositoryHelper;
import dev.galacticraft.gradle.commons.plugin.helpers.TaskHelper;
import dev.galacticraft.gradle.commons.plugin.property.BooleanProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Base abstract class for Plugin classes to extend that provides commonly used
 * methods and classes. Can help simply the readability of plugins code-wise
 * 
 * @author ROMVoid95
 */
@Accessors(fluent = true)
@NoArgsConstructor
public abstract class GradlePlugin implements SettingsPluginInterface {

	protected ConfigurationHelper configurations;

	protected ExtensionsHelper extensions;

	protected RepositoryHelper repositories;

	protected ConditionalLog conditionalLog;

	protected ObjectFactory factory;

	protected TaskHelper tasks;

	protected Variables variables;

	private Logger logger;

	private Project target;

	@Override
	public void project(@NotNull Project target, @NotNull PluginManager plugins, @NotNull ExtensionContainer extensions,
			@NotNull TaskContainer tasks) {
		this.target = target;
		this.factory = target.getObjects();
		this.conditionalLog = new ConditionalLog(target.getLogger());
		this.extensions = new ExtensionsHelper(extensions);
		this.configurations = new ConfigurationHelper(target);
		this.repositories = new RepositoryHelper(target.getRepositories());
		this.tasks = new TaskHelper(tasks);
		this.logger = target.getLogger();
		this.variables = new Variables(target);

		applyToProject(target, plugins);
	}

	protected abstract void applyToProject(@NotNull Project target, @NotNull PluginManager plugins);

	@Override
	public void settings(@NotNull Settings target, @NotNull PluginManager plugins, ExtensionContainer extensions) {
	}

	protected void afterEval(Action<Project> action) {
		GradleRequirement.safeAfterEval(this.target, () -> action.execute(target));
	}

	protected final Project getTarget() {
		return this.target;
	}

	protected void lifecycle(String format, Object... args) {
		this.logger.lifecycle(format, args);
	}

	protected void lifecycle(String message) {
		this.logger.lifecycle(message);
	}

	protected void debug(String format, Object... args) {
		this.logger.debug(format, args);
	}

	protected void debug(String message) {
		this.logger.debug(message);
	}

	protected void error(String format, Object... args) {
		this.logger.error(format, args);
	}

	protected void error(String message) {
		this.logger.error(message);
	}

	protected void info(String format, Object... args) {
		this.logger.info(format, args);
	}

	protected void info(String message) {
		this.logger.info(message);
	}

	public static class ConditionalLog {

		private BooleanProperty conditional;

		private final Logger logger;

		public ConditionalLog(Logger logger) {
			this.logger = logger;
		}

		public void setConditional(BooleanProperty conditional) {
			this.conditional = conditional;
		}

		public void lifecycle(String format, Object... args) {
			if (this.conditional.isTrue())
				this.logger.lifecycle(format, args);
		}

		public void lifecycle(String message) {
			if (this.conditional.isTrue())
				this.logger.lifecycle(message);
		}

		public void debug(String format, Object... args) {
			if (this.conditional.isTrue())
				this.logger.debug(format, args);
		}

		public void debug(String message) {
			if (this.conditional.isTrue())
				this.logger.debug(message);
		}

		public void error(String format, Object... args) {
			if (this.conditional.isTrue())
				this.logger.error(format, args);
		}

		public void error(String message) {
			if (this.conditional.isTrue())
				this.logger.error(message);
		}

		public void info(String format, Object... args) {
			if (this.conditional.isTrue())
				this.logger.info(format, args);
		}

		public void info(String message) {
			if (this.conditional.isTrue())
				this.logger.info(message);
		}
	}
}
