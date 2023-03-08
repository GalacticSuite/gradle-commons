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

import static org.gradle.internal.serialization.Transient.varOf;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.internal.attributes.ImmutableAttributes;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.publish.maven.MavenDependency;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.internal.dependencies.DefaultMavenDependency;
import org.gradle.api.publish.maven.internal.dependencies.MavenDependencyInternal;
import org.gradle.api.publish.maven.internal.dependencies.VersionRangeMapper;
import org.gradle.api.publish.maven.internal.publication.MavenPomInternal;
import org.gradle.api.publish.maven.internal.tasks.MavenPomFileGenerator;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.UntrackedTask;
import org.gradle.internal.serialization.Cached;
import org.gradle.internal.serialization.Transient;

@UntrackedTask(because = "Gradle doesn't understand the data structures used to configure this task")
public class SpecialGenerateMavenPom extends DefaultTask
{
    private final Transient.Var<MavenPom> pom = varOf();
    private final Transient.Var<ImmutableAttributes> compileScopeAttributes = varOf(ImmutableAttributes.EMPTY);
    private final Transient.Var<ImmutableAttributes> runtimeScopeAttributes = varOf(ImmutableAttributes.EMPTY);
    private Object destination;
    private final Cached<MavenPomFileGenerator.MavenPomSpec> mavenPomSpec = Cached.of(this::computeMavenPomSpec);

    @Inject
    protected FileResolver getFileResolver() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected VersionRangeMapper getVersionRangeMapper() {
        throw new UnsupportedOperationException();
    }

    public SpecialGenerateMavenPom withCompileScopeAttributes(ImmutableAttributes compileScopeAttributes) {
        this.compileScopeAttributes.set(compileScopeAttributes);
        return this;
    }

    public SpecialGenerateMavenPom withRuntimeScopeAttributes(ImmutableAttributes runtimeScopeAttributes) {
        this.runtimeScopeAttributes.set(runtimeScopeAttributes);
        return this;
    }

    /**
     * The Maven POM.
     *
     * @return The Maven POM.
     */
    @Internal
    public MavenPom getPom() {
        return pom.get();
    }

    public void setPom(MavenPom pom) {
        this.pom.set(pom);
    }

    /**
     * The file the POM will be written to.
     *
     * @return The file the POM will be written to
     */
    @OutputFile
    public File getDestination() {
        return destination == null ? null : getFileResolver().resolve(destination);
    }

    /**
     * Sets the destination the descriptor will be written to.
     *
     * @param destination The file the descriptor will be written to.
     * @since 4.0
     */
    public void setDestination(File destination) {
        this.destination = destination;
    }

    /**
     * Sets the destination the descriptor will be written to.
     *
     * The value is resolved with {@link org.gradle.api.Project#file(Object)}
     *
     * @param destination The file the descriptor will be written to.
     */
    public void setDestination(Object destination) {
        this.destination = destination;
    }

    @TaskAction
    public void doGenerate() {
        mavenPomSpec().writeTo(getDestination());
    }

    private MavenPomFileGenerator.MavenPomSpec mavenPomSpec() {
        return mavenPomSpec.get();
    }

    private MavenPomFileGenerator.MavenPomSpec computeMavenPomSpec() {
        MavenPomInternal pomInternal = (MavenPomInternal) getPom();

        MavenPomFileGenerator pomGenerator = new MavenPomFileGenerator(
            pomInternal.getProjectIdentity(),
            getVersionRangeMapper(),
            pomInternal.getVersionMappingStrategy(),
            compileScopeAttributes.get(),
            runtimeScopeAttributes.get(),
            pomInternal.writeGradleMetadataMarker());
        pomGenerator.configureFrom(pomInternal);

        for (MavenDependency mavenDependency : pomInternal.getApiDependencyManagement()) {
            pomGenerator.addApiDependencyManagement(versionCorrectDependency(mavenDependency));
        }

        for (MavenDependency mavenDependency : pomInternal.getRuntimeDependencyManagement()) {
            pomGenerator.addRuntimeDependencyManagement(versionCorrectDependency(mavenDependency));
        }

        for (MavenDependency mavenDependency : pomInternal.getImportDependencyManagement()) {
            pomGenerator.addImportDependencyManagement(versionCorrectDependency(mavenDependency));
        }

        for (MavenDependencyInternal runtimeDependency : pomInternal.getApiDependencies()) {
            pomGenerator.addApiDependency(versionCorrectInternalDependency(runtimeDependency));
        }
        for (MavenDependencyInternal runtimeDependency : pomInternal.getRuntimeDependencies()) {
            pomGenerator.addRuntimeDependency(versionCorrectInternalDependency(runtimeDependency));
        }
        for (MavenDependencyInternal optionalDependency : pomInternal.getOptionalDependencies()) {
            pomGenerator.addOptionalDependency(versionCorrectInternalDependency(optionalDependency));
        }

        pomGenerator.withXml(pomInternal.getXmlAction());

        return pomGenerator.toSpec();
    }
    
    private MavenDependencyInternal versionCorrectInternalDependency(MavenDependencyInternal d)
    {
        MavenDependencyInternal dep = d;
        getProject().getLogger().lifecycle(dep.getArtifactId() + " | " + dep.getVersion());
        if(d.getVersion().contains("_mapped_"))
        {
            dep = new DefaultMavenDependency(d.getGroupId(), d.getArtifactId(), d.getVersion().split("_mapped_")[0], d.getType());;
        }
        return dep;
    }
    
    private MavenDependency versionCorrectDependency(MavenDependency d)
    {
        MavenDependency dep = d;
        getProject().getLogger().lifecycle(dep.getArtifactId() + " | " + dep.getVersion());
        if(d.getVersion().contains("_mapped_"))
        {
            dep = new DefaultMavenDependency(d.getGroupId(), d.getArtifactId(), d.getVersion().split("_mapped_")[0], d.getType());;
        }
        return dep;
    }
}
