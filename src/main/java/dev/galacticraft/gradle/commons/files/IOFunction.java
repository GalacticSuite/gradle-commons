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

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOFunction<T, R>
{

    R apply(final T t) throws IOException;

    default <V> IOFunction<V, R> compose(final IOFunction<? super V, ? extends T> before)
    {
        Objects.requireNonNull(before, "before");
        return (final V v) -> apply(before.apply(v));
    }

    default <V> IOFunction<V, R> compose(final Function<? super V, ? extends T> before)
    {
        Objects.requireNonNull(before, "before");
        return (final V v) -> apply(before.apply(v));
    }

    default IOSupplier<R> compose(final IOSupplier<? extends T> before)
    {
        Objects.requireNonNull(before, "before");
        return () -> apply(before.get());
    }

    default IOSupplier<R> compose(final Supplier<? extends T> before)
    {
        Objects.requireNonNull(before, "before");
        return () -> apply(before.get());
    }

    default <V> IOFunction<T, V> andThen(final IOFunction<? super R, ? extends V> after)
    {
        Objects.requireNonNull(after, "after");
        return (final T t) -> after.apply(apply(t));
    }

    default <V> IOFunction<T, V> andThen(final Function<? super R, ? extends V> after)
    {
        Objects.requireNonNull(after, "after");
        return (final T t) -> after.apply(apply(t));
    }

    default IOConsumer<T> andThen(final IOConsumer<? super R> after)
    {
        Objects.requireNonNull(after, "after");
        return (final T t) -> after.accept(apply(t));
    }

    default IOConsumer<T> andThen(final Consumer<? super R> after)
    {
        Objects.requireNonNull(after, "after");
        return (final T t) -> after.accept(apply(t));
    }

    static <T> IOFunction<T, T> identity()
    {
        return t -> t;
    }
}
