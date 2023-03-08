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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import dev.galacticraft.gradle.commons.version.Versions;
import dev.galacticraft.gradle.commons.version.list.VersionSet;

@SuppressWarnings("all")
public class Versioning implements Cloneable
{

	private String latest;

	private String release;

	private List<String> versions;

	private String lastUpdated;

	private Snapshot snapshot;

	private List<SnapshotVersion> snapshotVersions;

	public void addSnapshotVersion(SnapshotVersion snapshotVersion)
	{
		getSnapshotVersions().add(snapshotVersion);
	}

	public void addVersion(String string)
	{
		getVersions().add(string);
	}

	public Versioning clone()
	{
		try
		{
			Versioning copy = (Versioning) super.clone();

			if (this.versions != null)
			{
				copy.versions = new ArrayList<String>();
				copy.versions.addAll(this.versions);
			}

			if (this.snapshot != null)
			{
				copy.snapshot = (Snapshot) this.snapshot.clone();
			}

			if (this.snapshotVersions != null)
			{
				copy.snapshotVersions = new ArrayList<SnapshotVersion>();
				for (SnapshotVersion item : this.snapshotVersions)
				{
					copy.snapshotVersions.add(((SnapshotVersion) item).clone());
				}
			}

			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) new UnsupportedOperationException(getClass().getName() + " does not support clone()").initCause(ex);
		}
	}

	public String getLastUpdated()
	{
		return this.lastUpdated;
	}

	public String getLatest()
	{
		return this.latest;
	}

	public String getRelease()
	{
		return this.release;
	}

	public Snapshot getSnapshot()
	{
		return this.snapshot;
	}

	public List<SnapshotVersion> getSnapshotVersions()
	{
		if (this.snapshotVersions == null)
		{
			this.snapshotVersions = new ArrayList<SnapshotVersion>();
		}

		return this.snapshotVersions;
	}

	public List<String> getVersions()
	{
		if (this.versions == null)
		{
			this.versions = new ArrayList<String>();
		}

		return this.versions;
	}

	public VersionSet getVersionSet()
	{
		return Versions.of(getVersions());
	}
	
	public void removeSnapshotVersion(SnapshotVersion snapshotVersion)
	{
		getSnapshotVersions().remove(snapshotVersion);
	}

	public void removeVersion(String string)
	{
		getVersions().remove(string);
	}

	public void setLastUpdated(String lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public void setLatest(String latest)
	{
		this.latest = latest;
	}

	public void setRelease(String release)
	{
		this.release = release;
	}

	public void setSnapshot(Snapshot snapshot)
	{
		this.snapshot = snapshot;
	}

	public void setSnapshotVersions(List<SnapshotVersion> snapshotVersions)
	{
		this.snapshotVersions = snapshotVersions;
	}

	public void setVersions(List<String> versions)
	{
		this.versions = versions;
	}

	public void updateTimestamp()
	{
		setLastUpdatedTimestamp(new Date());
	}

	public void setLastUpdatedTimestamp(Date date)
	{
		TimeZone				timezone	= TimeZone.getTimeZone("UTC");
		java.text.DateFormat	fmt			= new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		fmt.setTimeZone(timezone);
		setLastUpdated(fmt.format(date));
	}

}
