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

package dev.galacticraft.gradle.commons.xml.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportPlugin extends ConfigurationContainer implements Cloneable
{
	private String groupId = "net.galacticraft.gradle.core.xml.plugins";

	private String artifactId;

	private String version;

	private List<ReportSet> reportSets;

	public void addReportSet(ReportSet reportSet)
	{
		getReportSets().add(reportSet);
	}

	public ReportPlugin clone()
	{
		try
		{
			ReportPlugin copy = (ReportPlugin) super.clone();
			if (this.reportSets != null)
			{
				copy.reportSets = new ArrayList<>();
				for (ReportSet item : this.reportSets)
					copy.reportSets.add(item.clone());
			}
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
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

	public List<ReportSet> getReportSets()
	{
		if (this.reportSets == null)
			this.reportSets = new ArrayList<>();
		return this.reportSets;
	}

	public String getVersion()
	{
		return this.version;
	}

	public void removeReportSet(ReportSet reportSet)
	{
		getReportSets().remove(reportSet);
	}

	public void setArtifactId(String artifactId)
	{
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setReportSets(List<ReportSet> reportSets)
	{
		this.reportSets = reportSets;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	private Map<String, ReportSet> reportSetMap = null;

	public void flushReportSetMap()
	{
		this.reportSetMap = null;
	}

	public Map<String, ReportSet> getReportSetsAsMap()
	{
		if (this.reportSetMap == null)
		{
			this.reportSetMap = new LinkedHashMap<>();
			if (getReportSets() != null)
				for (Iterator<ReportSet> i = getReportSets().iterator(); i.hasNext();)
				{
					ReportSet reportSet = i.next();
					this.reportSetMap.put(reportSet.getId(), reportSet);
				}
		}
		return this.reportSetMap;
	}

	public String getKey()
	{
		return constructKey(this.groupId, this.artifactId);
	}

	public static String constructKey(String groupId, String artifactId)
	{
		return groupId + ":" + artifactId;
	}
}
