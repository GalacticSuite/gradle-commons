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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.galacticraft.gradle.commons.util.Checks;
import dev.galacticraft.gradle.commons.util.MavenDependency;
import dev.galacticraft.gradle.commons.version.Version;
import dev.galacticraft.gradle.commons.version.Versions;
import dev.galacticraft.gradle.commons.version.list.VersionSet;
import dev.galacticraft.gradle.commons.xml.metadata.Metadata;
import dev.galacticraft.gradle.commons.xml.model.Dependency;
import dev.galacticraft.gradle.commons.xml.model.Model;

public class MavenModel extends MavenModelBase
{
	private Version				version;
	private Optional<Metadata>	metadata;
	private Optional<Model>		pom;

	public void setVersion(Version version)
	{
		this.version = version;
	}

	public void setVersion(String version)
	{
		setVersion(Version.of(version));
	}

	public Version getVersion()
	{
		return this.version;
	}

	private Optional<Metadata> metadata()
	{
		if (this.metadata == null)
		{
			this.metadata = this.getMetadata();
		}
		return this.metadata;
	}

	private Optional<Model> pom(Version version)
	{
		if (this.pom == null)
		{
			if (version.isSnapshotVersion())
			{
				this.pom = this.getSnapshotPom(project.getArtifactId(), version);
			} else
			{
				this.pom = this.getPom(project.getArtifactId(), version);
			}
		}
		return this.pom;
	}

	public List<MavenDependency> getDependencies()
	{
		Checks.notNull(version, "[getDependencies()] No Version set for " + project.getArtifactId());
		Optional<Model> pom = pom(version);

		List<MavenDependency> mavenDependencies = new ArrayList<>();
		if (pom.isPresent())
		{
			List<Dependency> dependencies = pom.get().getDependencies();
			mavenDependencies.addAll(dependencies.stream().map(MavenDependency::from).collect(Collectors.toList()));
			return mavenDependencies;
		}

		return Collections.emptyList();
	}

	public Version readLatestSnapshot()
	{
		return readVersions().extractSnapshots().latest();
	}

	public Version readLatestRelease()
	{
		Optional<Metadata> metadata = metadata();
		if (metadata.isPresent())
			return Version.of(metadata.get().getVersioning().getRelease());
		return Version.Null();
	}

	public VersionSet readVersions()
	{
		Optional<Metadata> metadata = metadata();
		if (metadata.isPresent())
		{
			return metadata.get().getVersioning().getVersionSet();
		}
		return Versions.empty();
	}
}
