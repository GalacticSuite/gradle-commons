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

import javax.annotation.Nullable;

import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.GradleVersion;

public class GradleRequirement
{

    static void require(final @Nullable GradleVersion minimum, final Plugin<?> plugin, final String targetDisplayName)
    {
        // Check version
        if (minimum != null) {
            final GradleVersion current = GradleVersion.current();
            if (current.compareTo(minimum) < 0) {
                throw new GradleException("Your Gradle version is too old to apply the plugin from "
                    + plugin.getClass().getName() + " to " + targetDisplayName + "\n" + "    Minimum: " + minimum + "\n"
                    + "    Current: " + current + "\n");
            }
        }
    }

    static void safeAfterEval(final @Nullable Project target, Runnable afterEvaluate)
    {
        if (target != null) {
            target.afterEvaluate(p -> {
                if (p.getState().getFailure() != null) {
                    return;
                }

                afterEvaluate.run();
            });
        }
    }
}
