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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.galacticraft.gradle.commons.model.nexus.BasicNexusRepository;
import dev.galacticraft.gradle.commons.project.NexusRepositories;
import dev.galacticraft.gradle.commons.util.StringUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NexusManager
{
    private static final Map<String, String> repositoriesMap = new HashMap<>();

    private static final OkHttpClient client = new OkHttpClient();

    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    private static final String GET_REPOSITORIES_URL = "https://maven.galacticraft.net/service/rest/v1/repositories";

    static {
        getRepositories().getAllRepositories().forEach(repo -> {
            repositoriesMap.put(StringUtil.getCleanedName(repo), repo.getUrl());
        });
    }

    public static NexusRepositories getRepositories()
    {
        try {
            return tryGetRepositories();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String getHostedRepository(String id)
    {
        if (repositoriesMap.containsKey(id)) {
            return repositoriesMap.get(id);
        }
        return null;
    }

    @Nullable
    private static NexusRepositories tryGetRepositories() throws Exception
    {
        NexusRepositories repositories = null;
        Request request = new Request.Builder().url(GET_REPOSITORIES_URL).get().build();
        try (Response response = client.newCall(request).execute()) {
            BasicNexusRepository[] repos = gson.fromJson(response.body().string(), BasicNexusRepository[].class);
            repositories = new NexusRepositories(repos);

        }
        return repositories;
    }
}
