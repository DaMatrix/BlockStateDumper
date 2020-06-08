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

import net.minecraft.block.Block;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Representation of a block which is serializable by Gson.
 *
 * @author DaPorkchop_
 */
@SuppressWarnings({
        "unchecked",
        "deprecation"
})
public class JsonBlock {
    public static JsonBlock fromMinecraft(Block block) {
        JsonBlock json = new JsonBlock();
        json.name = block.getRegistryName().toString();
        json.legacyId = Block.REGISTRY.getIDForObject(block);

        json.properties = block.getBlockState().getProperties().stream()
                .map(JsonProperty::fromMinecraft)
                .collect(Collectors.toList());

        json.states = block.getBlockState().getValidStates().stream()
                .map(JsonState::fromMinecraft)
                .collect(Collectors.toList());

        json.metas = IntStream.range(json.legacyId << 4, (json.legacyId + 1) << 4)
                .mapToObj(Block.BLOCK_STATE_IDS::getByValue)
                .map(JsonState::fromMinecraft)
                .map(state -> state == null ? null : state.properties)
                .toArray(Map[]::new);

        json.defaultState = JsonState.fromMinecraft(block.getDefaultState()).properties;

        return json;
    }

    public String name;
    public List<JsonProperty> properties;
    public List<JsonState> states;
    public Map<String, Object>[] metas;
    public Map<String, Object> defaultState;
    public int legacyId;
}
