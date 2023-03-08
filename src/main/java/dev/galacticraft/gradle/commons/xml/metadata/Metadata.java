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

package dev.galacticraft.gradle.commons.xml.metadata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class Metadata implements Cloneable
{

	private String modelVersion;

	private String groupId;

	private String artifactId;

	private Versioning versioning;

	private String version;

	@Deprecated
	private List<Plugin> plugins;

	private String modelEncoding = "UTF-8";

	public void addPlugin(Plugin plugin)
	{
		getPlugins().add(plugin);
	}

	public Metadata clone()
	{
		try
		{
			Metadata copy = (Metadata) super.clone();

			if (this.versioning != null)
			{
				copy.versioning = (Versioning) this.versioning.clone();
			}

			if (this.plugins != null)
			{
				copy.plugins = new ArrayList<Plugin>();
				for (Plugin item : this.plugins)
				{
					copy.plugins.add(((Plugin) item).clone());
				}
			}

			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) new UnsupportedOperationException(getClass().getName() + " does not support clone()").initCause(ex);
		}
	}

	public String getArtifactId()
	{
		return this.artifactId;
	}

	public String getGroupId()
	{
		return this.groupId;
	}

	public String getModelEncoding()
	{
		return this.modelEncoding;
	}

	public String getModelVersion()
	{
		return this.modelVersion;
	}

	public List<Plugin> getPlugins()
	{
		if (this.plugins == null)
		{
			this.plugins = new ArrayList<Plugin>();
		}

		return this.plugins;
	}

	public String getVersion()
	{
		return this.version;
	}

	public Versioning getVersioning()
	{
		return this.versioning;
	}

	public void removePlugin(Plugin plugin)
	{
		getPlugins().remove(plugin);
	}

	public void setArtifactId(String artifactId)
	{
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setModelEncoding(String modelEncoding)
	{
		this.modelEncoding = modelEncoding;
	}

	public void setModelVersion(String modelVersion)
	{
		this.modelVersion = modelVersion;
	}

	public void setPlugins(List<Plugin> plugins)
	{
		this.plugins = plugins;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public void setVersioning(Versioning versioning)
	{
		this.versioning = versioning;
	}

	private String getSnapshotVersionKey(SnapshotVersion sv)
	{
		return sv.getClassifier() + ":" + sv.getExtension();
	}

	public boolean merge(Metadata sourceMetadata)
	{
		boolean changed = false;

		for (Plugin plugin : sourceMetadata.getPlugins())
		{
			boolean found = false;

			for (Plugin preExisting : getPlugins())
			{
				if (preExisting.getPrefix().equals(plugin.getPrefix()))
				{
					found = true;
					break;
				}
			}

			if (!found)
			{
				Plugin mappedPlugin = new Plugin();

				mappedPlugin.setArtifactId(plugin.getArtifactId());

				mappedPlugin.setPrefix(plugin.getPrefix());

				mappedPlugin.setName(plugin.getName());

				addPlugin(mappedPlugin);

				changed = true;
			}
		}

		Versioning versioning = sourceMetadata.getVersioning();
		if (versioning != null)
		{
			Versioning v = getVersioning();
			if (v == null)
			{
				v = new Versioning();
				setVersioning(v);
				changed = true;
			}

			for (String version : versioning.getVersions())
			{
				if (!v.getVersions().contains(version))
				{
					changed = true;
					v.getVersions().add(version);
				}
			}

			if ("null".equals(versioning.getLastUpdated()))
			{
				versioning.setLastUpdated(null);
			}

			if ("null".equals(v.getLastUpdated()))
			{
				v.setLastUpdated(null);
			}

			if (versioning.getLastUpdated() == null || versioning.getLastUpdated().length() == 0)
			{

				versioning.setLastUpdated(v.getLastUpdated());
			}

			if (v.getLastUpdated() == null || v.getLastUpdated().length() == 0 || versioning.getLastUpdated().compareTo(v.getLastUpdated()) >= 0)
			{
				changed = true;
				v.setLastUpdated(versioning.getLastUpdated());

				if (versioning.getRelease() != null)
				{
					changed = true;
					v.setRelease(versioning.getRelease());
				}
				if (versioning.getLatest() != null)
				{
					changed = true;
					v.setLatest(versioning.getLatest());
				}

				Snapshot	s			= v.getSnapshot();
				Snapshot	snapshot	= versioning.getSnapshot();
				if (snapshot != null)
				{
					boolean updateSnapshotVersions = false;
					if (s == null)
					{
						s = new Snapshot();
						v.setSnapshot(s);
						changed = true;
						updateSnapshotVersions = true;
					}

					if (s.getTimestamp() == null ? snapshot.getTimestamp() != null : !s.getTimestamp().equals(snapshot.getTimestamp()))
					{
						s.setTimestamp(snapshot.getTimestamp());
						changed = true;
						updateSnapshotVersions = true;
					}
					if (s.getBuildNumber() != snapshot.getBuildNumber())
					{
						s.setBuildNumber(snapshot.getBuildNumber());
						changed = true;
					}
					if (s.isLocalCopy() != snapshot.isLocalCopy())
					{
						s.setLocalCopy(snapshot.isLocalCopy());
						changed = true;
					}
					if (updateSnapshotVersions)
					{
						Map<String, SnapshotVersion> versions = new LinkedHashMap<>();

						if (!v.getSnapshotVersions().isEmpty())
						{
							for (SnapshotVersion sv : versioning.getSnapshotVersions())
							{
								String key = getSnapshotVersionKey(sv);
								versions.put(key, sv);
							}

							if (!versions.isEmpty())
							{
								for (SnapshotVersion sv : v.getSnapshotVersions())
								{
									String key = getSnapshotVersionKey(sv);
									if (!versions.containsKey(key))
									{
										versions.put(key, sv);
									}
								}
							}
							v.setSnapshotVersions(new ArrayList<SnapshotVersion>(versions.values()));
						}

						changed = true;
					}
				}
			}
		}
		return changed;
	}

}
