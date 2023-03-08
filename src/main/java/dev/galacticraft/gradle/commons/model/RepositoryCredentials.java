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

package dev.galacticraft.gradle.commons.model;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;

import dev.galacticraft.gradle.commons.plugin.Variables;
import lombok.Getter;

@Getter
public class RepositoryCredentials
{
    private Property<String> username;

    private Property<String> password;

    private final Variables vars;

    public static RepositoryCredentials create(Project project)
    {
        return new RepositoryCredentials(project);
    }

    private RepositoryCredentials(Project project)
    {
        this.vars = new Variables(project);
        this.username = project.getObjects().property(String.class);
        this.password = project.getObjects().property(String.class);
    }

    public RepositoryCredentials username(Provider<String> provider)
    {
        this.username.set(vars.get(provider.getOrNull()));
        return this;
    }

    public RepositoryCredentials password(Provider<String> provider)
    {
        this.password.set(vars.get(provider.getOrNull()));
        return this;
    }

    public boolean notNull()
    {
        return this.username.isPresent() && this.password.isPresent();
    }

    public Action<PasswordCredentials> get()
    {
        return passwordCredentials -> {
            passwordCredentials.setUsername(username.getOrNull());
            passwordCredentials.setPassword(password.getOrNull());
        };
    }
}
