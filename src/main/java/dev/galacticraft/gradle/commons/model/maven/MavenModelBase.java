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

package dev.galacticraft.gradle.commons.model.maven;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import javax.annotation.Nullable;

import dev.galacticraft.gradle.commons.plugin.GradlePlugin.ConditionalLog;
import dev.galacticraft.gradle.commons.project.GalacticProject;
import dev.galacticraft.gradle.commons.util.Checks;
import dev.galacticraft.gradle.commons.util.IOHelper;
import dev.galacticraft.gradle.commons.version.Version;
import dev.galacticraft.gradle.commons.xml.metadata.Metadata;
import dev.galacticraft.gradle.commons.xml.model.Model;

public abstract class MavenModelBase
{
	protected GalacticProject	project;
	private URL					repositoryUrl;

	public void setProject(GalacticProject project)
	{
		this.project = project;
	}

	public void setLogger(ConditionalLog logger)
	{
		Internal.logger = logger;
	}

	public void setRepositoryUrl(URI url)
	{
		URL repositoryUrl = IOHelper.toURL(url);
		this.repositoryUrl = repositoryUrl;
	}

	protected Optional<Metadata> getMetadata()
	{
		this.runChecks(repositoryUrl, "getMetadata()");
		return Internal._getMetadata(repositoryUrl, project);
	}

	protected Optional<Metadata> getSnapshotMetadata(Version version)
	{
		this.runChecks(repositoryUrl, "getSnapshotMetadata()");
		return Internal._getSnapshotMetadata(repositoryUrl, project, version);
	}

	protected Optional<Model> getSnapshotPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrl, "getSnapshotPom()");
		return Internal._getSnapshotPom(repositoryUrl, project, version);
	}

	protected Optional<Model> getPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrl, "getPom()");
		return Internal._getPom(repositoryUrl, project, version);
	}

	private <T extends Object> void runChecks(@Nullable T object, String method)
	{
		Checks.notNull(project, "GalacticraftProject is not set and returning null. Did you use 'setProject(GalacticProject project)'?");
		String errm = String.format("[%s] MavenArtifactRepository Set<URL> for " + project.getArtifactId() + "is Null and shouldn't be", method);
		Checks.notNull(object, errm);
	}

}
