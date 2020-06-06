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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
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

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        logger.info("Dumping block states...");
        List<JsonBlock> blocks = StreamSupport.stream(Block.REGISTRY.spliterator(), false).map(JsonBlock::fromMinecraft).collect(Collectors.toList());
        logger.info("Saving block states...");
        try (Writer prettyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("blockstates.json")), StandardCharsets.UTF_8));
             Writer minWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("blockstates-min.json")), StandardCharsets.UTF_8))) {
            new GsonBuilder().setPrettyPrinting().create().toJson(blocks, prettyWriter);
            new Gson().toJson(blocks, minWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Done.");
    }
}
