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
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.Map;

/**
 * Representation of a block state which is serializable by Gson.
 *
 * @author DaPorkchop_
 */
@SuppressWarnings({
        "unchecked",
        "deprecation"
})
public class JsonState {
    public static JsonState fromMinecraft(IBlockState state) {
        int id = Block.BLOCK_STATE_IDS.get(state);
        boolean virtual = Block.BLOCK_STATE_IDS.getByValue(id) != state;
        boolean def = state.getBlock().getDefaultState() == state;

        JsonState json = def
                ? virtual ? new DefaultAndVirtual() : new Default()
                : virtual ? new Virtual() : new JsonState();

        json.properties = state.getProperties().entrySet().stream()
                .collect(BlockStateDumper.toLinkedHashMap(e -> e.getKey().getName(), e -> ((IProperty) e.getKey()).getName(e.getValue())));
        json.id = id;

        return json;
    }

    public Map<String, Object> properties;
    public int id;

    public static class Default extends JsonState {
        @SerializedName("default")
        public boolean defaultFlag = true;
    }

    public static class Virtual extends JsonState {
        public boolean virtual = true;
    }

    public static class DefaultAndVirtual extends JsonState {
        @SerializedName("default")
        public boolean defaultFlag = true;

        public boolean virtual = true;
    }
}
