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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MailingList implements Cloneable, InputLocationTracker
{
	private String name;

	private String subscribe;

	private String unsubscribe;

	private String post;

	private String archive;

	private List<String> otherArchives;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation nameLocation;

	private InputLocation subscribeLocation;

	private InputLocation unsubscribeLocation;

	private InputLocation postLocation;

	private InputLocation archiveLocation;

	private InputLocation otherArchivesLocation;

	public void addOtherArchive(String string)
	{
		getOtherArchives().add(string);
	}

	public MailingList clone()
	{
		try
		{
			MailingList copy = (MailingList) super.clone();
			if (this.otherArchives != null)
			{
				copy.otherArchives = new ArrayList<>();
				copy.otherArchives.addAll(this.otherArchives);
			}
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getArchive()
	{
		return this.archive;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "name":
				return this.nameLocation;
			case "subscribe":
				return this.subscribeLocation;
			case "unsubscribe":
				return this.unsubscribeLocation;
			case "post":
				return this.postLocation;
			case "archive":
				return this.archiveLocation;
			case "otherArchives":
				return this.otherArchivesLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public String getName()
	{
		return this.name;
	}

	public List<String> getOtherArchives()
	{
		if (this.otherArchives == null)
			this.otherArchives = new ArrayList<>();
		return this.otherArchives;
	}

	public void setLocation(Object key, InputLocation location)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				this.location = location;
				return;
			case "name":
				this.nameLocation = location;
				return;
			case "subscribe":
				this.subscribeLocation = location;
				return;
			case "unsubscribe":
				this.unsubscribeLocation = location;
				return;
			case "post":
				this.postLocation = location;
				return;
			case "archive":
				this.archiveLocation = location;
				return;
			case "otherArchives":
				this.otherArchivesLocation = location;
				return;
			}
			setOtherLocation(key, location);
			return;
		}
		setOtherLocation(key, location);
	}

	public void setOtherLocation(Object key, InputLocation location)
	{
		if (location != null)
		{
			if (this.locations == null)
				this.locations = new LinkedHashMap<>();
			this.locations.put(key, location);
		}
	}

	private InputLocation getOtherLocation(Object key)
	{
		return (this.locations != null) ? this.locations.get(key) : null;
	}

	public String getPost()
	{
		return this.post;
	}

	public String getSubscribe()
	{
		return this.subscribe;
	}

	public String getUnsubscribe()
	{
		return this.unsubscribe;
	}

	public void removeOtherArchive(String string)
	{
		getOtherArchives().remove(string);
	}

	public void setArchive(String archive)
	{
		this.archive = archive;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOtherArchives(List<String> otherArchives)
	{
		this.otherArchives = otherArchives;
	}

	public void setPost(String post)
	{
		this.post = post;
	}

	public void setSubscribe(String subscribe)
	{
		this.subscribe = subscribe;
	}

	public void setUnsubscribe(String unsubscribe)
	{
		this.unsubscribe = unsubscribe;
	}
}
