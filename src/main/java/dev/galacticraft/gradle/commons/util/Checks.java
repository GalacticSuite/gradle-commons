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

package dev.galacticraft.gradle.commons.util;

import java.util.Collection;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.gradle.api.GradleException;

public class Checks
{

    public static void checkArgument(final boolean b, final String string)
    {
        if (!b) {
            throw new ExtGradleException(IllegalStateException.class, string);
        }
    }

    public static boolean isNullOrEmpty(final String it)
    {
        return it == null || it.isEmpty();
    }

    public static String nullToEmpty(final String it)
    {
        if (it == null) {
            return "";
        }
        return it;
    }

    public static String emptyToNull(final String from)
    {
        if (from != null && !from.trim().isEmpty()) {
            return from;
        }
        return null;
    }

    public static <T> T firstNonNull(final T t1, final T t2)
    {
        if (t1 == null) {
            return t2;
        }
        return t1;
    }

    public static void isTrue(boolean expression, String message)
    {
        if (!expression) {
            throw new ExtGradleException(IllegalArgumentException.class, message);
        }
    }

    public static void isTrue(boolean expression, Supplier<String> messageSupplier)
    {
        if (!expression) {
            throw new ExtGradleException(IllegalArgumentException.class, nullSafeGet(messageSupplier));
        }
    }

    public static void isNull(@Nullable Object object, String message)
    {
        if (object != null) {
            throw new ExtGradleException(IllegalArgumentException.class, message);
        }
    }

    public static void isNull(@Nullable Object object, Supplier<String> messageSupplier)
    {
        if (object != null) {
            throw new ExtGradleException(IllegalArgumentException.class, nullSafeGet(messageSupplier));
        }
    }

    public static <T extends Collection<?>> T notNullOrEmpty(@Nullable T collection,
        @Nullable Object ifNullErrorMessage, @Nullable Object ifEmptyErrorMessage)
    {
        if (collection == null) {
            throw new ExtGradleException(NullPointerException.class, String.valueOf(ifNullErrorMessage));
        } else if (collection.isEmpty()) {
            throw new CollectionEmptyException(String.valueOf(ifEmptyErrorMessage));
        }
        return collection;
    }

    public static <T extends Collection<?>> T notNullOrEmpty(@Nullable T collection,
        @Nullable Object ifNullErrorMessage)
    {
        if (collection == null) {
            throw new ExtGradleException(NullPointerException.class, String.valueOf(ifNullErrorMessage));
        } else if (collection.isEmpty()) {
            throw new CollectionEmptyException(collection + " cannot be empty");
        }
        return collection;
    }

    public static <T extends Collection<?>> T notNullOrEmpty(@Nullable T collection)
    {
        if (collection == null) {
            throw new ExtGradleException(NullPointerException.class, collection + " must not be null");
        } else if (collection.isEmpty()) {
            throw new CollectionEmptyException(collection + " cannot be empty");
        }
        return collection;
    }

    public static <T extends Object> T notNull(@Nullable T object, @Nullable String format, Object... args)
    {
        if (object == null) {
            throw new ExtGradleException(NullPointerException.class, String.format(format, args));
        }
        return object;
    }
    
    public static <T extends Object> T notNull(@Nullable T object, @Nullable Object errorMessage)
    {
        if (object == null) {
            throw new ExtGradleException(NullPointerException.class, String.valueOf(errorMessage));
        }
        return object;
    }

    public static <T extends Object> boolean allNotNull(@Nullable Collection<T> objects)
    {
        for (T obj : objects) {
            if (obj == null) {
                return false;
            }
        }

        return true;
    }

    public static <T extends Object> T notNull(@Nullable T object)
    {
        if (object == null) {
            throw new ExtGradleException(NullPointerException.class, object + " must not be null");
        }
        return object;
    }

    public static void notNull(@Nullable Object object, Supplier<String> messageSupplier)
    {
        if (object == null) {
            throw new ExtGradleException(IllegalArgumentException.class, nullSafeGet(messageSupplier));
        }
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier)
    {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

    private static class CollectionEmptyException extends ExtGradleException
    {
        private static final long serialVersionUID = 1L;

        public CollectionEmptyException()
        {
            super(CollectionEmptyException.class, "Collection cannot be empty");
        }

        public CollectionEmptyException(String message)
        {
            super(CollectionEmptyException.class, message);
        }
    }

    private static class ExtGradleException extends GradleException
    {
        private static final long serialVersionUID = 1L;

        public ExtGradleException(Class<?> exceptionClass, String message)
        {
            this(exceptionClass.getSimpleName(), message);
        }

        public ExtGradleException(String exceptionClass, String message)
        {
            super(String.format("[%s] - %s", exceptionClass, message));
        }
    }
}
