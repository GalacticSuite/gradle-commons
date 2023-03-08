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

package dev.galacticraft.gradle.commons.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.EntityReplacementMap;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import dev.galacticraft.gradle.commons.xml.metadata.Metadata;
import dev.galacticraft.gradle.commons.xml.metadata.Plugin;
import dev.galacticraft.gradle.commons.xml.metadata.Snapshot;
import dev.galacticraft.gradle.commons.xml.metadata.SnapshotVersion;
import dev.galacticraft.gradle.commons.xml.metadata.Versioning;

@SuppressWarnings("deprecation")
public class MetadataReader
{
    private boolean addDefaultEntities = true;

    public final ContentTransformer contentTransformer;

    public MetadataReader()
    {
        this(new ContentTransformer()
        {
            public String transform(String source, String fieldName)
            {
                return source;
            }
        });
    }

    public MetadataReader(ContentTransformer contentTransformer)
    {
        this.contentTransformer = contentTransformer;
    }

    private boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, Set<String> parsed)
        throws XmlPullParserException
    {
        if (!(parser.getName().equals(tagName) || parser.getName().equals(alias))) {
            return false;
        }
        if (!parsed.add(tagName)) {
            throw new XmlPullParserException("Duplicated tag: '" + tagName + "'", parser, null);
        }
        return true;
    }

    private void checkUnknownAttribute(XmlPullParser parser, String attribute, String tagName, boolean strict)
        throws XmlPullParserException, IOException
    {

        if (strict) {
            throw new XmlPullParserException("Unknown attribute '" + attribute + "' for tag '" + tagName + "'", parser,
                null);
        }
    }

    private void checkUnknownElement(XmlPullParser parser, boolean strict) throws XmlPullParserException, IOException
    {
        if (strict) {
            throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, null);
        }

        for (int unrecognizedTagCount = 1; unrecognizedTagCount > 0;) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                unrecognizedTagCount++;
            } else if (eventType == XmlPullParser.END_TAG) {
                unrecognizedTagCount--;
            }
        }
    }

    public boolean getAddDefaultEntities()
    {
        return addDefaultEntities;
    }

    private boolean getBooleanValue(String s, String attribute, XmlPullParser parser, String defaultValue)
        throws XmlPullParserException
    {
        if (s != null && s.length() != 0) {
            return Boolean.valueOf(s).booleanValue();
        }
        if (defaultValue != null) {
            return Boolean.valueOf(defaultValue).booleanValue();
        }
        return false;
    }

    private int getIntegerValue(String s, String attribute, XmlPullParser parser, boolean strict)
        throws XmlPullParserException
    {
        if (s != null) {
            try {
                return Integer.valueOf(s).intValue();
            } catch (NumberFormatException nfe) {
                if (strict) {
                    throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be an integer",
                        parser, nfe);
                }
            }
        }
        return 0;
    }

    private String getTrimmedValue(String s)
    {
        if (s != null) {
            s = s.trim();
        }
        return s;
    }

    private String interpolatedTrimmed(String value, String context)
    {
        return getTrimmedValue(contentTransformer.transform(value, context));
    }

    private int nextTag(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        int eventType = parser.next();
        if (eventType == XmlPullParser.TEXT) {
            eventType = parser.next();
        }
        if (eventType != XmlPullParser.START_TAG && eventType != XmlPullParser.END_TAG) {
            throw new XmlPullParserException("expected START_TAG or END_TAG not " + XmlPullParser.TYPES[eventType],
                parser, null);
        }
        return eventType;
    }

    public Metadata read(Reader reader, boolean strict) throws IOException, XmlPullParserException
    {
        XmlPullParser parser =
            addDefaultEntities ? new MXParser(EntityReplacementMap.defaultEntityReplacementMap) : new MXParser();

        parser.setInput(reader);

        return read(parser, strict);
    }

    public Metadata read(Reader reader) throws IOException, XmlPullParserException
    {
        return read(reader, true);
    }

    public Metadata read(InputStream in, boolean strict) throws IOException, XmlPullParserException
    {
        return read(ReaderFactory.newXmlReader(in), strict);
    }

    public Metadata read(InputStream in) throws IOException, XmlPullParserException
    {
        return read(ReaderFactory.newXmlReader(in));
    }

    private Metadata parseMetadata(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException
    {
        String tagName = parser.getName();
        Metadata metadata = new Metadata();
        for (int i = parser.getAttributeCount() - 1; i >= 0; i--) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeName(i);

            if (name.indexOf(':') >= 0) {

            } else if ("xmlns".equals(name)) {

            } else if ("modelVersion".equals(name)) {
                metadata.setModelVersion(interpolatedTrimmed(value, "modelVersion"));
            } else {
                checkUnknownAttribute(parser, name, tagName, strict);
            }
        }
        Set<String> parsed = new HashSet<>();
        while ((strict ? parser.nextTag() : nextTag(parser)) == XmlPullParser.START_TAG) {
            if (checkFieldWithDuplicate(parser, "groupId", null, parsed)) {
                metadata.setGroupId(interpolatedTrimmed(parser.nextText(), "groupId"));
            } else if (checkFieldWithDuplicate(parser, "artifactId", null, parsed)) {
                metadata.setArtifactId(interpolatedTrimmed(parser.nextText(), "artifactId"));
            } else if (checkFieldWithDuplicate(parser, "versioning", null, parsed)) {
                metadata.setVersioning(parseVersioning(parser, strict));
            } else if (checkFieldWithDuplicate(parser, "version", null, parsed)) {
                metadata.setVersion(interpolatedTrimmed(parser.nextText(), "version"));
            } else if (checkFieldWithDuplicate(parser, "plugins", null, parsed)) {
                List<Plugin> plugins = new ArrayList<Plugin>();
                metadata.setPlugins(plugins);
                while (parser.nextTag() == XmlPullParser.START_TAG) {
                    if ("plugin".equals(parser.getName())) {
                        plugins.add(parsePlugin(parser, strict));
                    } else {
                        checkUnknownElement(parser, strict);
                    }
                }
            } else {
                checkUnknownElement(parser, strict);
            }
        }
        return metadata;
    }

    private Plugin parsePlugin(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException
    {
        String tagName = parser.getName();
        Plugin plugin = new Plugin();
        for (int i = parser.getAttributeCount() - 1; i >= 0; i--) {
            String name = parser.getAttributeName(i);

            if (name.indexOf(':') >= 0) {

            } else {
                checkUnknownAttribute(parser, name, tagName, strict);
            }
        }
        Set<String> parsed = new HashSet<>();
        while ((strict ? parser.nextTag() : nextTag(parser)) == XmlPullParser.START_TAG) {
            if (checkFieldWithDuplicate(parser, "name", null, parsed)) {
                plugin.setName(interpolatedTrimmed(parser.nextText(), "name"));
            } else if (checkFieldWithDuplicate(parser, "prefix", null, parsed)) {
                plugin.setPrefix(interpolatedTrimmed(parser.nextText(), "prefix"));
            } else if (checkFieldWithDuplicate(parser, "artifactId", null, parsed)) {
                plugin.setArtifactId(interpolatedTrimmed(parser.nextText(), "artifactId"));
            } else {
                checkUnknownElement(parser, strict);
            }
        }
        return plugin;
    }

    private Snapshot parseSnapshot(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException
    {
        String tagName = parser.getName();
        Snapshot snapshot = new Snapshot();
        for (int i = parser.getAttributeCount() - 1; i >= 0; i--) {
            String name = parser.getAttributeName(i);

            if (name.indexOf(':') >= 0) {

            } else {
                checkUnknownAttribute(parser, name, tagName, strict);
            }
        }
        Set<String> parsed = new HashSet<>();
        while ((strict ? parser.nextTag() : nextTag(parser)) == XmlPullParser.START_TAG) {
            if (checkFieldWithDuplicate(parser, "timestamp", null, parsed)) {
                snapshot.setTimestamp(interpolatedTrimmed(parser.nextText(), "timestamp"));
            } else if (checkFieldWithDuplicate(parser, "buildNumber", null, parsed)) {
                snapshot.setBuildNumber(getIntegerValue(interpolatedTrimmed(parser.nextText(), "buildNumber"),
                    "buildNumber", parser, strict));
            } else if (checkFieldWithDuplicate(parser, "localCopy", null, parsed)) {
                snapshot.setLocalCopy(
                    getBooleanValue(interpolatedTrimmed(parser.nextText(), "localCopy"), "localCopy", parser, "false"));
            } else {
                checkUnknownElement(parser, strict);
            }
        }
        return snapshot;
    }

    private SnapshotVersion parseSnapshotVersion(XmlPullParser parser, boolean strict)
        throws IOException, XmlPullParserException
    {
        String tagName = parser.getName();
        SnapshotVersion snapshotVersion = new SnapshotVersion();
        for (int i = parser.getAttributeCount() - 1; i >= 0; i--) {
            String name = parser.getAttributeName(i);

            if (name.indexOf(':') >= 0) {

            } else {
                checkUnknownAttribute(parser, name, tagName, strict);
            }
        }
        Set<String> parsed = new HashSet<>();
        while ((strict ? parser.nextTag() : nextTag(parser)) == XmlPullParser.START_TAG) {
            if (checkFieldWithDuplicate(parser, "classifier", null, parsed)) {
                snapshotVersion.setClassifier(interpolatedTrimmed(parser.nextText(), "classifier"));
            } else if (checkFieldWithDuplicate(parser, "extension", null, parsed)) {
                snapshotVersion.setExtension(interpolatedTrimmed(parser.nextText(), "extension"));
            } else if (checkFieldWithDuplicate(parser, "value", null, parsed)) {
                snapshotVersion.setVersion(interpolatedTrimmed(parser.nextText(), "value"));
            } else if (checkFieldWithDuplicate(parser, "updated", null, parsed)) {
                snapshotVersion.setUpdated(interpolatedTrimmed(parser.nextText(), "updated"));
            } else {
                checkUnknownElement(parser, strict);
            }
        }
        return snapshotVersion;
    }

    private Versioning parseVersioning(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException
    {
        String tagName = parser.getName();
        Versioning versioning = new Versioning();
        for (int i = parser.getAttributeCount() - 1; i >= 0; i--) {
            String name = parser.getAttributeName(i);

            if (name.indexOf(':') >= 0) {

            } else {
                checkUnknownAttribute(parser, name, tagName, strict);
            }
        }
        Set<String> parsed = new HashSet<>();
        while ((strict ? parser.nextTag() : nextTag(parser)) == XmlPullParser.START_TAG) {
            if (checkFieldWithDuplicate(parser, "latest", null, parsed)) {
                versioning.setLatest(interpolatedTrimmed(parser.nextText(), "latest"));
            } else if (checkFieldWithDuplicate(parser, "release", null, parsed)) {
                versioning.setRelease(interpolatedTrimmed(parser.nextText(), "release"));
            } else if (checkFieldWithDuplicate(parser, "versions", null, parsed)) {
                List<String> versions = new ArrayList<String>();
                versioning.setVersions(versions);
                while (parser.nextTag() == XmlPullParser.START_TAG) {
                    if ("version".equals(parser.getName())) {
                        versions.add(interpolatedTrimmed(parser.nextText(), "versions"));
                    } else {
                        checkUnknownElement(parser, strict);
                    }
                }
            } else if (checkFieldWithDuplicate(parser, "lastUpdated", null, parsed)) {
                versioning.setLastUpdated(interpolatedTrimmed(parser.nextText(), "lastUpdated"));
            } else if (checkFieldWithDuplicate(parser, "snapshot", null, parsed)) {
                versioning.setSnapshot(parseSnapshot(parser, strict));
            } else if (checkFieldWithDuplicate(parser, "snapshotVersions", null, parsed)) {
                List<SnapshotVersion> snapshotVersions = new ArrayList<SnapshotVersion>();
                versioning.setSnapshotVersions(snapshotVersions);
                while (parser.nextTag() == XmlPullParser.START_TAG) {
                    if ("snapshotVersion".equals(parser.getName())) {
                        snapshotVersions.add(parseSnapshotVersion(parser, strict));
                    } else {
                        checkUnknownElement(parser, strict);
                    }
                }
            } else {
                checkUnknownElement(parser, strict);
            }
        }
        return versioning;
    }

    private Metadata read(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException
    {
        Metadata metadata = null;
        int eventType = parser.getEventType();
        boolean parsed = false;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (strict && !"metadata".equals(parser.getName())) {
                    throw new XmlPullParserException(
                        "Expected root element 'metadata' but found '" + parser.getName() + "'", parser, null);
                } else if (parsed) {

                    throw new XmlPullParserException("Duplicated tag: 'metadata'", parser, null);
                }
                metadata = parseMetadata(parser, strict);
                metadata.setModelEncoding(parser.getInputEncoding());
                parsed = true;
            }
            eventType = parser.next();
        }
        if (parsed) {
            return metadata;
        }
        throw new XmlPullParserException(
            "Expected root element 'metadata' but found no element at all: invalid XML document", parser, null);
    }

    public void setAddDefaultEntities(boolean addDefaultEntities)
    {
        this.addDefaultEntities = addDefaultEntities;
    }

    public static interface ContentTransformer
    {

        String transform(String source, String fieldName);
    }
}
