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

import net.minecraft.block.properties.IProperty;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representation of a block property which is serializable by Gson.
 *
 * @author DaPorkchop_
 */
@SuppressWarnings("unchecked")
public class JsonProperty {
    public static JsonProperty fromMinecraft(IProperty property)    {
        JsonProperty json = new JsonProperty();
        if (property.getValueClass() == Integer.class)  {
            json.type = "int";
        } else if (property.getValueClass() == Boolean.class)   {
            json.type = "boolean";
        } else if (Enum.class.isAssignableFrom(property.getValueClass()))   {
            json.type = "enum";
        } else {
            throw new IllegalArgumentException(property.getValueClass().getCanonicalName());
        }

        json.values = ((Collection<Object>) property.getAllowedValues()).stream()
                .map(v -> toJsonSerializable(property, v))
                .collect(Collectors.toList());

        return json;
    }

    public static Object toJsonSerializable(IProperty property, Object value)   {
        if (property.getValueClass() == Integer.class || property.getValueClass() == Boolean.class) {
            return value;
        } else if (value instanceof Enum)   {
            return property.getName((Enum) value);
        } else {
            throw new IllegalArgumentException(property.getValueClass() + ": " + value);
        }
    }

    public String type;
    public List<Object> values;
}
