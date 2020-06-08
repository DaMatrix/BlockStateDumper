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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mod(modid = BlockStateDumper.MODID,
        name = BlockStateDumper.NAME,
        version = BlockStateDumper.VERSION,
        acceptableRemoteVersions = "*")
public class BlockStateDumper {
    public static final String MODID = "blockstatedumper";
    public static final String NAME = "BlockStateDumper";
    public static final String VERSION = "0.0.1-1.12.2";

    public static Logger logger;

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T, K, V> Collector<T, ?, Map<K, V>> toLinkedHashMap(Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        return Collectors.toMap(
                keyExtractor,
                valueExtractor,
                (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); });
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        try {
            logger.info("Saving block states...");
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("blocks.json")), StandardCharsets.UTF_8))) {
                GSON.toJson(StreamSupport.stream(Block.REGISTRY.spliterator(), false)
                        .collect(toLinkedHashMap(b -> b.getRegistryName().toString(), JsonBlock::fromMinecraft)), writer);
            }
            logger.info("Saving registries...");
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("registries.json")), StandardCharsets.UTF_8))) {
                GSON.toJson(new JsonRegistries(), writer);
            }
            logger.info("Done.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
