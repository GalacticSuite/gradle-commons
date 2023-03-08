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

package dev.galacticraft.gradle.commons.plugin.helpers;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class TaskHelper
{

    private TaskContainer tasks;

    public TaskHelper(TaskContainer tasks)
    {
        this.tasks = tasks;
    }

    public TaskContainer container()
    {
        return this.tasks;
    }

    public TaskProvider<Task> registerTask(final String name)
    {
        return container().register(name);
    }

    public TaskProvider<Task> registerTask(final String name, final Action<? super Task> action)
    {
        return container().register(name, action);
    }

    public <T extends Task> TaskProvider<T> registerTask(final String name, final Class<T> type)
    {
        return container().register(name, type);
    }

    public <T extends Task> TaskProvider<T> registerTask(final String name, final Class<T> type,
        final Action<? super T> action)
    {
        return container().register(name, type, action);
    }
}
