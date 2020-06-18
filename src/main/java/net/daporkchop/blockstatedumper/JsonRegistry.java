/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2020-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.blockstatedumper;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * @author DaPorkchop_
 */
public class JsonRegistry {
    public Map<String, RegistryEntry> entries = new LinkedHashMap<>();

    public <V> JsonRegistry(Iterable<V> values, Function<V, String> keyExtractor, ToIntFunction<V> idExtractor) {
        for (V v : values) {
            RegistryEntry entry = new RegistryEntry();
            entry.protocol_id = idExtractor.applyAsInt(v);
            this.entries.put(keyExtractor.apply(v), entry);
        }
    }

    public static class WithDefault extends JsonRegistry {
        @SerializedName("default")
        public String defaultField;

        public <V> WithDefault(V defaultValue, Iterable<V> values, Function<V, String> keyExtractor, ToIntFunction<V> idExtractor) {
            super(values, keyExtractor, idExtractor);

            this.defaultField = keyExtractor.apply(defaultValue);
        }
    }

    public static class RegistryEntry {
        public int protocol_id;
    }
}
