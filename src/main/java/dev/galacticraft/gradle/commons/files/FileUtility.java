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

package dev.galacticraft.gradle.commons.files;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.galacticraft.gradle.commons.util.StringWriter;

public class FileUtility
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static final int EOF = -1;

    private static final int NOT_FOUND = -1;

    private static final ThreadLocal<byte[]> SKIP_BYTE_BUFFER = ThreadLocal.withInitial(FileUtility::byteArray);

    private static final ThreadLocal<char[]> SKIP_CHAR_BUFFER = ThreadLocal.withInitial(FileUtility::charArray);

    public static final byte[] EMPTY_BYTE_ARRAY = {};

    public static final File[] EMPTY_FILE_ARRAY = {};

    private static final String EMPTY_STRING = "";

    public static final char EXTENSION_SEPARATOR = '.';

    private static final char UNIX_SEPARATOR = '/';

    private static final char WINDOWS_SEPARATOR = '\\';

    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    private static final char OTHER_SEPARATOR;

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    public static boolean isSystemWindows()
    {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    public static boolean isSeparator(final char ch)
    {
        return ch == UNIX_SEPARATOR || ch == WINDOWS_SEPARATOR;
    }

    public static byte[] byteArray()
    {
        return byteArray(DEFAULT_BUFFER_SIZE);
    }

    public static byte[] byteArray(final int size)
    {
        return new byte[size];
    }

    private static char[] charArray()
    {
        return charArray(DEFAULT_BUFFER_SIZE);
    }

    private static char[] charArray(final int size)
    {
        return new char[size];
    }

    public static int indexOfExtension(final String fileName) throws IllegalArgumentException
    {
        if (fileName == null) {
            return NOT_FOUND;
        }
        if (isSystemWindows()) {
            final int offset = fileName.indexOf(':', getAdsCriticalOffset(fileName));
            if (offset != -1) {
                throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
            }
        }
        final int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        final int lastSeparator = indexOfLastSeparator(fileName);
        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
    }

    private static int getAdsCriticalOffset(final String fileName)
    {
        // Step 1: Remove leading path segments.
        final int offset1 = fileName.lastIndexOf(SYSTEM_SEPARATOR);
        final int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            if (offset2 == -1) {
                return 0;
            }
            return offset2 + 1;
        }
        if (offset2 == -1) {
            return offset1 + 1;
        }
        return Math.max(offset1, offset2) + 1;
    }

    public static int indexOfLastSeparator(final String fileName)
    {
        if (fileName == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = fileName.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowsPos = fileName.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static String getExtension(final String fileName) throws IllegalArgumentException
    {
        if (fileName == null) {
            return null;
        }
        final int index = indexOfExtension(fileName);
        if (index == NOT_FOUND) {
            return EMPTY_STRING;
        }
        return fileName.substring(index + 1);
    }

    public static int copy(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        final long count = copyLarge(inputStream, outputStream);
        if (count > Integer.MAX_VALUE) {
            return EOF;
        }
        return (int) count;
    }

    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize)
        throws IOException
    {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    public static void copy(final InputStream input, final Writer writer, final Charset inputCharset) throws IOException
    {
        final InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(inputCharset));
        copy(reader, writer);
    }

    public static void copy(final InputStream input, final Writer writer, final String inputCharsetName)
        throws IOException
    {
        copy(input, writer, Charsets.toCharset(inputCharsetName));
    }

    public static long copy(final Reader reader, final Appendable output) throws IOException
    {
        return copy(reader, output, CharBuffer.allocate(DEFAULT_BUFFER_SIZE));
    }

    public static long copy(final Reader reader, final Appendable output, final CharBuffer buffer) throws IOException
    {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            buffer.flip();
            output.append(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static void copy(final Reader reader, final OutputStream output, final Charset outputCharset)
        throws IOException
    {
        final OutputStreamWriter writer = new OutputStreamWriter(output, Charsets.toCharset(outputCharset));
        copy(reader, writer);
        writer.flush();
    }

    public static void copy(final Reader reader, final OutputStream output, final String outputCharsetName)
        throws IOException
    {
        copy(reader, output, Charsets.toCharset(outputCharsetName));
    }

    public static int copy(final Reader reader, final Writer writer) throws IOException
    {
        final long count = copyLarge(reader, writer);
        if (count > Integer.MAX_VALUE) {
            return EOF;
        }
        return (int) count;
    }

    public static long copy(final URL url, final File file) throws IOException
    {
        try (OutputStream outputStream = Files.newOutputStream(Objects.requireNonNull(file, "file").toPath())) {
            return copy(url, outputStream);
        }
    }

    public static long copy(final URL url, final OutputStream outputStream) throws IOException
    {
        try (InputStream inputStream = Objects.requireNonNull(url, "url").openStream()) {
            return copyLarge(inputStream, outputStream);
        }
    }

    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream) throws IOException
    {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream, final byte[] buffer)
        throws IOException
    {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");
        long count = 0;
        int n;
        while (EOF != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
        final long length) throws IOException
    {
        return copyLarge(input, output, inputOffset, length, getByteArray());
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
        final long length, final byte[] buffer) throws IOException
    {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    public static long copyLarge(final Reader reader, final Writer writer) throws IOException
    {
        return copyLarge(reader, writer, getCharArray());
    }

    public static long copyLarge(final Reader reader, final Writer writer, final char[] buffer) throws IOException
    {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length)
        throws IOException
    {
        return copyLarge(reader, writer, inputOffset, length, getCharArray());
    }

    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length,
        final char[] buffer) throws IOException
    {
        if (inputOffset > 0) {
            skipFully(reader, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = reader.read(buffer, 0, bytesToRead))) {
            writer.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    public static long skip(final InputStream input, final long toSkip) throws IOException
    {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        long remain = toSkip;
        while (remain > 0) {
            final byte[] byteArray = getByteArray();
            final long n = input.read(byteArray, 0, (int) Math.min(remain, byteArray.length));
            if (n < 0) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException
    {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, DEFAULT_BUFFER_SIZE));
        long remain = toSkip;
        while (remain > 0) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int) Math.min(remain, DEFAULT_BUFFER_SIZE));
            final int n = input.read(skipByteBuffer);
            if (n == EOF) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static long skip(final Reader reader, final long toSkip) throws IOException
    {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        long remain = toSkip;
        while (remain > 0) {
            // See https://issues.apache.org/jira/browse/IO-203 for why we use read() rather than delegating to skip()
            final char[] charArray = getCharArray();
            final long n = reader.read(charArray, 0, (int) Math.min(remain, charArray.length));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static void skipFully(final InputStream input, final long toSkip) throws IOException
    {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException
    {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final Reader reader, final long toSkip) throws IOException
    {
        final long skipped = skip(reader, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

    static byte[] getByteArray()
    {
        return SKIP_BYTE_BUFFER.get();
    }

    static char[] getCharArray()
    {
        return SKIP_CHAR_BUFFER.get();
    }

    public static int length(final byte[] array)
    {
        return array == null ? 0 : array.length;
    }

    public static int length(final char[] array)
    {
        return array == null ? 0 : array.length;
    }

    public static int length(final CharSequence csq)
    {
        return csq == null ? 0 : csq.length();
    }

    public static int length(final Object[] array)
    {
        return array == null ? 0 : array.length;
    }

    public static String toString(final InputStream input, final Charset charset) throws IOException
    {
        try (final StringWriter sw = new StringWriter()) {
            copy(input, sw, charset);
            return sw.toString();
        }
    }

    public static File createParentDirectories(final File file) throws IOException
    {
        return mkdirs(getParentFile(file));
    }

    public static FileInputStream openInputStream(final File file) throws IOException
    {
        Objects.requireNonNull(file, "file");
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(final File file) throws IOException
    {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException
    {
        Objects.requireNonNull(file, "file");
        if (file.exists()) {
            requireFile(file, "file");
            requireCanWrite(file, "file");
        } else {
            createParentDirectories(file);
        }
        return new FileOutputStream(file, append);
    }

    public static byte[] toByteArray(final InputStream inputStream) throws IOException
    {
        try (final UBAOutputSteam ubaOutput = new UBAOutputSteam();
            final ThresholdingOutputStream thresholdOuput = new ThresholdingOutputStream(Integer.MAX_VALUE, os -> {
                throw new IllegalArgumentException(
                    String.format("Cannot read more than %,d into a byte array", Integer.MAX_VALUE));
            }, os -> ubaOutput)) {
            copy(inputStream, thresholdOuput);
            return ubaOutput.toByteArray();
        }
    }

    public static byte[] toByteArray(final InputStream input, final int size) throws IOException
    {

        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        final byte[] data = byteArray(size);
        int offset = 0;
        int read;

        while (offset < size && (read = input.read(data, offset, size - offset)) != EOF) {
            offset += read;
        }

        if (offset != size) {
            throw new IOException("Unexpected read size, current: " + offset + ", expected: " + size);
        }

        return data;
    }

    public static byte[] toByteArray(final InputStream input, final long size) throws IOException
    {

        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }

        return toByteArray(input, (int) size);
    }

    public static byte[] toByteArray(final Reader reader, final Charset charset) throws IOException
    {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(reader, output, charset);
            return output.toByteArray();
        }
    }

    public static byte[] toByteArray(final Reader reader, final String charsetName) throws IOException
    {
        return toByteArray(reader, Charsets.toCharset(charsetName));
    }

    public static byte[] toByteArray(final URI uri) throws IOException
    {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(final URL url) throws IOException
    {
        final URLConnection conn = url.openConnection();
        try {
            return toByteArray(conn);
        } finally {
            close(conn);
        }
    }

    public static byte[] toByteArray(final URLConnection urlConn) throws IOException
    {
        try (InputStream inputStream = urlConn.getInputStream()) {
            return toByteArray(inputStream);
        }
    }

    public static byte[] readFileToByteArray(final File file) throws IOException
    {
        try (InputStream inputStream = openInputStream(file)) {
            final long fileLength = file.length();
            return fileLength > 0 ? toByteArray(inputStream, fileLength) : toByteArray(inputStream);
        }
    }

    public static String readToString(final File file)
    {
        try {
            return readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static String readFileToString(final File file, final Charset charsetName) throws IOException
    {
        try (InputStream inputStream = openInputStream(file)) {
            return toString(inputStream, Charsets.toCharset(charsetName));
        }
    }

    public static String readFileToString(final File file, final String charsetName) throws IOException
    {
        return readFileToString(file, Charsets.toCharset(charsetName));
    }

    public static List<String> readLines(final File file, final Charset charset) throws IOException
    {
        try (InputStream inputStream = openInputStream(file)) {
            return readLines(inputStream, Charsets.toCharset(charset));
        }
    }

    public static List<String> readLines(final File file, final String charsetName) throws IOException
    {
        return readLines(file, Charsets.toCharset(charsetName));
    }

    public static List<String> readLines(final InputStream input, final Charset charset) throws IOException
    {
        final InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(charset));
        return readLines(reader);
    }

    public static List<String> readLines(final InputStream input, final String charsetName) throws IOException
    {
        return readLines(input, Charsets.toCharset(charsetName));
    }

    public static List<String> readLines(final Reader reader) throws IOException
    {
        final BufferedReader bufReader = toBufferedReader(reader);
        final List<String> list = new ArrayList<>();
        String line;
        while ((line = bufReader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    public static BufferedReader toBufferedReader(final Reader reader)
    {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(final Reader reader, final int size)
    {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    private static File getParentFile(final File file)
    {
        return file == null ? null : file.getParentFile();
    }

    private static void requireCanWrite(final File file, final String name)
    {
        Objects.requireNonNull(file, "file");
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File parameter '" + name + " is not writable: '" + file + "'");
        }
    }

    private static File requireDirectory(final File directory, final String name)
    {
        Objects.requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        }
        return directory;
    }

    private static File requireDirectoryExists(final File directory, final String name)
    {
        requireExists(directory, name);
        requireDirectory(directory, name);
        return directory;
    }

    private static File requireExists(final File file, final String fileParamName)
    {
        Objects.requireNonNull(file, fileParamName);
        if (!file.exists()) {
            throw new IllegalArgumentException(
                "File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    private static File requireFile(final File file, final String name)
    {
        Objects.requireNonNull(file, name);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
        }
        return file;
    }

    public static long lastModified(final File file) throws IOException
    {
        return Files.getLastModifiedTime(Objects.requireNonNull(file.toPath(), "file")).toMillis();
    }

    private static void setLastModified(final File file, final long timeMillis) throws IOException
    {
        Objects.requireNonNull(file, "file");
        if (!file.setLastModified(timeMillis)) {
            throw new IOException(String.format("Failed setLastModified(%s) on '%s'", timeMillis, file));
        }
    }

    public static long sizeOf(final File file)
    {
        requireExists(file, "file");
        return file.isDirectory() ? sizeOfDirectory0(file) : file.length();
    }

    private static long sizeOf0(final File file)
    {
        Objects.requireNonNull(file, "file");
        if (file.isDirectory()) {
            return sizeOfDirectory0(file);
        }
        return file.length(); // will be 0 if file does not exist
    }

    public static BigInteger sizeOfAsBigInteger(final File file)
    {
        requireExists(file, "file");
        return file.isDirectory() ? sizeOfDirectoryBig0(file) : BigInteger.valueOf(file.length());
    }

    private static BigInteger sizeOfBig0(final File file)
    {
        Objects.requireNonNull(file, "fileOrDir");
        return file.isDirectory() ? sizeOfDirectoryBig0(file) : BigInteger.valueOf(file.length());
    }

    public static long sizeOfDirectory(final File directory)
    {
        return sizeOfDirectory0(requireDirectoryExists(directory, "directory"));
    }

    private static long sizeOfDirectory0(final File directory)
    {
        Objects.requireNonNull(directory, "directory");
        final File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            return 0L;
        }
        long size = 0;

        for (final File file : files) {
            if (!isSymlink(file)) {
                size += sizeOf0(file);
                if (size < 0) {
                    break;
                }
            }
        }

        return size;
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(final File directory)
    {
        return sizeOfDirectoryBig0(requireDirectoryExists(directory, "directory"));
    }

    private static BigInteger sizeOfDirectoryBig0(final File directory)
    {
        Objects.requireNonNull(directory, "directory");
        final File[] files = directory.listFiles();
        if (files == null) {
            // null if security restricted
            return BigInteger.ZERO;
        }
        BigInteger size = BigInteger.ZERO;

        for (final File file : files) {
            if (!isSymlink(file)) {
                size = size.add(sizeOfBig0(file));
            }
        }

        return size;
    }

    public static boolean isSymlink(final File file)
    {
        return file != null && Files.isSymbolicLink(file.toPath());
    }

    public static Stream<File> streamFiles(final File directory, final boolean recursive, final String... extensions)
        throws IOException
    {
        final IOFileFilter filter = extensions == null ? FileFileFilter.INSTANCE
            : FileFileFilter.INSTANCE.and(new SuffixFileFilter(toSuffixes(extensions)));
        return walk(directory.toPath(), filter, toMaxDepth(recursive), false, FileVisitOption.FOLLOW_LINKS)
            .map(Path::toFile);
    }

    public static File toFile(final URL url)
    {
        if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
            return null;
        }
        final String filename = url.getFile().replace('/', File.separatorChar);
        return new File(decodeUrl(filename));
    }

    public static File[] toFiles(final URL... urls)
    {
        if (length(urls) == 0) {
            return EMPTY_FILE_ARRAY;
        }
        final File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            final URL url = urls[i];
            if (url != null) {
                if (!"file".equalsIgnoreCase(url.getProtocol())) {
                    throw new IllegalArgumentException("Can only convert file URL to a File: " + url);
                }
                files[i] = toFile(url);
            }
        }
        return files;
    }

    static String decodeUrl(final String url)
    {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            final int n = url.length();
            final StringBuilder buffer = new StringBuilder();
            final ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n;) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (final RuntimeException e) {
                    } finally {
                        if (bytes.position() > 0) {
                            bytes.flip();
                            buffer.append(StandardCharsets.UTF_8.decode(bytes).toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }

    public static List<File> toList(final Stream<File> stream)
    {
        return stream.collect(Collectors.toList());
    }

    private static int toMaxDepth(final boolean recursive)
    {
        return recursive ? Integer.MAX_VALUE : 1;
    }

    private static String[] toSuffixes(final String... extensions)
    {
        Objects.requireNonNull(extensions, "extensions");
        final String[] suffixes = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            suffixes[i] = "." + extensions[i];
        }
        return suffixes;
    }

    public static void touch(final File file) throws IOException
    {
        Objects.requireNonNull(file, "file");
        if (!file.exists()) {
            openOutputStream(file).close();
        }
        setLastModified(file, System.currentTimeMillis());
    }

    public static URL[] toURLs(final File... files) throws IOException
    {
        Objects.requireNonNull(files, "files");
        final URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    private static File mkdirs(final File directory) throws IOException
    {
        if ((directory != null) && (!directory.mkdirs() && !directory.isDirectory())) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        }
        return directory;
    }

    public static boolean waitFor(final File file, final int seconds)
    {
        Objects.requireNonNull(file, "file");
        final long finishAtMillis = System.currentTimeMillis() + (seconds * 1000L);
        boolean wasInterrupted = false;
        try {
            while (!file.exists()) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(100, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }

    public static void write(final File file, final CharSequence data, final Charset charset) throws IOException
    {
        write(file, data, charset, false);
    }

    public static void write(final File file, final CharSequence data, final Charset charset, final boolean append)
        throws IOException
    {
        writeStringToFile(file, Objects.toString(data, null), charset, append);
    }

    public static void write(final File file, final CharSequence data, final String charsetName) throws IOException
    {
        write(file, data, charsetName, false);
    }

    public static void write(final File file, final CharSequence data, final String charsetName, final boolean append)
        throws IOException
    {
        write(file, data, Charsets.toCharset(charsetName), append);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data) throws IOException
    {
        writeByteArrayToFile(file, data, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final boolean append) throws IOException
    {
        writeByteArrayToFile(file, data, 0, data.length, append);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len)
        throws IOException
    {
        writeByteArrayToFile(file, data, off, len, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len,
        final boolean append) throws IOException
    {
        try (OutputStream out = openOutputStream(file, append)) {
            out.write(data, off, len);
        }
    }

    public static void writeLines(final File file, final Collection<?> lines) throws IOException
    {
        writeLines(file, null, lines, null, false);
    }

    public static void writeLines(final File file, final Collection<?> lines, final boolean append) throws IOException
    {
        writeLines(file, null, lines, null, append);
    }

    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding)
        throws IOException
    {
        writeLines(file, null, lines, lineEnding, false);
    }

    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding,
        final boolean append) throws IOException
    {
        writeLines(file, null, lines, lineEnding, append);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines)
        throws IOException
    {
        writeLines(file, charsetName, lines, null, false);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final boolean append) throws IOException
    {
        writeLines(file, charsetName, lines, null, append);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final String lineEnding) throws IOException
    {
        writeLines(file, charsetName, lines, lineEnding, false);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
        final String lineEnding, final boolean append) throws IOException
    {
        try (OutputStream out = new BufferedOutputStream(openOutputStream(file, append))) {
            writeLines(lines, lineEnding, out, charsetName);
        }
    }

    public static void writeStringToFile(final File file, final String data, final Charset charset) throws IOException
    {
        writeStringToFile(file, data, charset, false);
    }

    public static void writeStringToFile(final File file, final String data, final Charset charset,
        final boolean append) throws IOException
    {
        try (OutputStream out = openOutputStream(file, append)) {
            write(data, out, charset);
        }
    }

    public static void writeStringToFile(final File file, final String data, final String charsetName)
        throws IOException
    {
        writeStringToFile(file, data, charsetName, false);
    }

    public static void writeStringToFile(final File file, final String data, final String charsetName,
        final boolean append) throws IOException
    {
        writeStringToFile(file, data, Charsets.toCharset(charsetName), append);
    }

    public static void close(final Closeable closeable) throws IOException
    {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void close(final Closeable... closeables) throws IOException
    {
        if (closeables != null) {
            for (final Closeable closeable : closeables) {
                close(closeable);
            }
        }
    }

    public static void close(final Closeable closeable, final IOConsumer<IOException> consumer) throws IOException
    {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void close(final URLConnection conn)
    {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    public static void closeQuietly(final Closeable closeable)
    {
        closeQuietly(closeable, (Consumer<IOException>) null);
    }

    public static void closeQuietly(final Closeable... closeables)
    {
        if (closeables == null) {
            return;
        }
        for (final Closeable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

    public static void closeQuietly(final Closeable closeable, final Consumer<IOException> consumer)
    {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void closeQuietly(final InputStream input)
    {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(final OutputStream output)
    {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(final Reader reader)
    {
        closeQuietly((Closeable) reader);
    }

    public static void closeQuietly(final Selector selector)
    {
        closeQuietly((Closeable) selector);
    }

    public static void closeQuietly(final ServerSocket serverSocket)
    {
        closeQuietly((Closeable) serverSocket);
    }

    public static void closeQuietly(final Socket socket)
    {
        closeQuietly((Closeable) socket);
    }

    public static void closeQuietly(final Writer writer)
    {
        closeQuietly((Closeable) writer);
    }

    public static void write(final byte[] data, final OutputStream output) throws IOException
    {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(final byte[] data, final Writer writer, final Charset charset) throws IOException
    {
        if (data != null) {
            writer.write(new String(data, Charsets.toCharset(charset)));
        }
    }

    public static void write(final byte[] data, final Writer writer, final String charsetName) throws IOException
    {
        write(data, writer, Charsets.toCharset(charsetName));
    }

    public static void write(final char[] data, final OutputStream output, final Charset charset) throws IOException
    {
        if (data != null) {
            output.write(new String(data).getBytes(Charsets.toCharset(charset)));
        }
    }

    public static void write(final char[] data, final OutputStream output, final String charsetName) throws IOException
    {
        write(data, output, Charsets.toCharset(charsetName));
    }

    public static void write(final char[] data, final Writer writer) throws IOException
    {
        if (data != null) {
            writer.write(data);
        }
    }

    public static void write(final CharSequence data, final OutputStream output, final Charset charset)
        throws IOException
    {
        if (data != null) {
            write(data.toString(), output, charset);
        }
    }

    public static void write(final CharSequence data, final OutputStream output, final String charsetName)
        throws IOException
    {
        write(data, output, Charsets.toCharset(charsetName));
    }

    public static void write(final CharSequence data, final Writer writer) throws IOException
    {
        if (data != null) {
            write(data.toString(), writer);
        }
    }

    public static void write(final String data, final OutputStream output, final Charset charset) throws IOException
    {
        if (data != null) {
            output.write(data.getBytes(Charsets.toCharset(charset)));
        }
    }

    public static void write(final String data, final OutputStream output, final String charsetName) throws IOException
    {
        write(data, output, Charsets.toCharset(charsetName));
    }

    public static void write(final String data, final Writer writer) throws IOException
    {
        if (data != null) {
            writer.write(data);
        }
    }

    public static void writeChunked(final byte[] data, final OutputStream output) throws IOException
    {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    public static void writeChunked(final char[] data, final Writer writer) throws IOException
    {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                writer.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
        final Charset charset) throws IOException
    {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        final Charset cs = Charsets.toCharset(charset);
        for (final Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes(cs));
            }
            output.write(lineEnding.getBytes(cs));
        }
    }

    public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output,
        final String charsetName) throws IOException
    {
        writeLines(lines, lineEnding, output, Charsets.toCharset(charsetName));
    }

    public static void writeLines(final Collection<?> lines, String lineEnding, final Writer writer) throws IOException
    {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    public static Writer writer(final Appendable appendable)
    {
        Objects.requireNonNull(appendable, "appendable");
        if (appendable instanceof Writer) {
            return (Writer) appendable;
        }
        if (appendable instanceof StringBuilder) {
            return new StringWriter((StringBuilder) appendable);
        }
        return new AppendableWriter<>(appendable);
    }

    public static BasicFileAttributes readBasicFileAttributes(final Path path) throws IOException
    {
        return Files.readAttributes(path, BasicFileAttributes.class);
    }

    public static BasicFileAttributes readBasicFileAttributesUnchecked(final Path path)
    {
        try {
            return readBasicFileAttributes(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Stream<Path> walk(final Path start, final PathFilter pathFilter, final int maxDepth,
        final boolean readAttributes, final FileVisitOption... options) throws IOException
    {
        return Files.walk(start, maxDepth, options).filter(path -> pathFilter.accept(path,
            readAttributes ? readBasicFileAttributesUnchecked(path) : null) == FileVisitResult.CONTINUE);
    }
}
