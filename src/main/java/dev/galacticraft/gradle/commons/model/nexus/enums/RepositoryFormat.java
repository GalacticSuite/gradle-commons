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

package dev.galacticraft.gradle.commons.model.nexus.enums;

import static dev.galacticraft.gradle.commons.model.nexus.enums.RepositoryType.GROUP;
import static dev.galacticraft.gradle.commons.model.nexus.enums.RepositoryType.HOSTED;
import static dev.galacticraft.gradle.commons.model.nexus.enums.RepositoryType.PROXY;

public enum RepositoryFormat
{
	APT(HOSTED, PROXY),
	BOWER(GROUP, HOSTED, PROXY),
	COCOAPODS(PROXY),
	CONAN(PROXY),
	CONDA(PROXY),
	DOCKER(GROUP, HOSTED, PROXY),
	GITLFS(HOSTED),
	GO(GROUP, PROXY),
	HELM(HOSTED, PROXY),
	MAVEN2(GROUP, HOSTED, PROXY),
	NPM(GROUP, HOSTED, PROXY),
	NUGET(GROUP, HOSTED, PROXY),
	P2(PROXY),
	PYPI(GROUP, HOSTED, PROXY),
	R(GROUP, HOSTED, PROXY),
	RAW(GROUP, HOSTED, PROXY),
	RUBYGEMS(GROUP, HOSTED, PROXY),
	YUM(GROUP, HOSTED, PROXY);

	private RepositoryType[] supportedTypes;

	RepositoryFormat(RepositoryType... hosted)
	{
		this.supportedTypes = hosted;
	}

	@Override
	public String toString()
	{
		return super.toString().toLowerCase();
	}

	public RepositoryType[] getSupportedRepositoryTypes()
	{
		return supportedTypes;
	}
}
