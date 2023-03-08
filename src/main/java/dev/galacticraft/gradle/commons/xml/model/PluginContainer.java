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

public class PluginContainer implements Cloneable, InputLocationTracker
{
	private List<Plugin> plugins;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation pluginsLocation;

	Map<String, Plugin> pluginMap;

	public void addPlugin(Plugin plugin)
	{
		getPlugins().add(plugin);
	}

	public PluginContainer clone()
	{
		try
		{
			PluginContainer copy = (PluginContainer) super.clone();
			if (this.plugins != null)
			{
				copy.plugins = new ArrayList<>();
				for (Plugin item : this.plugins)
					copy.plugins.add(item.clone());
			}
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "plugins":
				return this.pluginsLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
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
			case "plugins":
				this.pluginsLocation = location;
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

	public List<Plugin> getPlugins()
	{
		if (this.plugins == null)
			this.plugins = new ArrayList<>();
		return this.plugins;
	}

	public void removePlugin(Plugin plugin)
	{
		getPlugins().remove(plugin);
	}

	public void setPlugins(List<Plugin> plugins)
	{
		this.plugins = plugins;
	}

	public synchronized void flushPluginMap()
	{
		this.pluginMap = null;
	}

	public synchronized Map<String, Plugin> getPluginsAsMap()
	{
		if (this.pluginMap == null)
		{
			this.pluginMap = new LinkedHashMap<>();
			if (this.plugins != null)
				for (Iterator<Plugin> it = this.plugins.iterator(); it.hasNext();)
				{
					Plugin plugin = it.next();
					this.pluginMap.put(plugin.getKey(), plugin);
				}
		}
		return this.pluginMap;
	}
}
