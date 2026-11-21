package com.deeperdepths.config;

import com.deeperdepths.common.DeeperDepths;
import com.deeperdepths.common.ISoundFrequency;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class VibrationsConfig {

    private static String[] occludesVibrationsConfig;
    private static List<StateMatch> occludesVibrations;

    private static String[] resonatesConfig;
    private static List<StateMatch> resonates;

    private static String[] vibrationFrequenciesConfig = new String[] {
            "block.cloth-0",
            "block.end_portal_frame.fill-0",
            "entity.endereye.death-0",
            "sculk_sensor.clicking-0",
            "step-1",
            "fall-2",
            "swim-1",
            "flap-1",
            "entity.arrow.hit-2",
            "hit-0",
            "splash-2",
            "shoot-3",
            "equip-5",
            "shake-4",
            "roar-4",
            "flying-4",
            "fill-12",
            "empty-13",
            "item-3",
            "armor-5",
            "saddle-5",
            "shear-6",
            "hurt-7",
            ".eat-8",
            "drink-8",
            "close-9",
            "click_off-9",
            "detach-9",
            "click_on-10",
            "attach-10",
            "lever-10",
            "primed-10",
            "note-10",
            "break-12",
            "place-13",
            "block-11",
            "cast_spell-14",
            "teleport-14",
            "death-15",
            "explode-15"
    };
    private static Map<String, Integer> vibrationFrequencies;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/deeperdepths/vibrations.cfg"));
        try{
            config.load();
            occludesVibrationsConfig = config.getStringList("occludesVibrations", "blocks", new String[]{"minecraft:wool:*", "minecraft:carpet:*"}, "Blocks that block vibrations, formatted as registry name:metadata, no meta or wildcard meta '*' makes any meta of the blocks top vibrations.");
            resonatesConfig = config.getStringList("resonates", "blocks", new String[]{"deeperdepths:amethyst:*"}, "Blocks that sculk sensors can resonate sounds through, formatted as registry name:metadata, no meta or wildcard meta '*' makes any meta of the blocks resonate.");
            vibrationFrequenciesConfig = config.getStringList("vibrationFrequencies", "sounds", vibrationFrequenciesConfig,
                   "Sound registry names and frequencies in the format <name>-<frequency>, any sounds that match the specified registry name have the specified frequency, the higher in the list the higher priority the frequency has");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

    public static boolean occludesVibrations(IBlockState state) {
        if (occludesVibrations == null) occludesVibrations = readBlockStates(occludesVibrationsConfig);
        for (StateMatch match : occludesVibrations) if (match.test(state)) return true;
        return false;
    }

    public static boolean resonatesVibrations(IBlockState state) {
        if (resonates == null) resonates = readBlockStates(resonatesConfig);
        for (StateMatch match : resonates) if (match.test(state)) return true;
        return false;
    }

    private static List<StateMatch> readBlockStates(String[] strings) {
        List<StateMatch> matches = Lists.newArrayList();
        for (String str : strings) {
            int meta = -1;
            try {
                String[] split = str.split(":");
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0], split[1]));
                if (block == null) throw new NullPointerException();
                if (split.length > 2) try {
                    meta = Integer.parseInt(split[2]);
                } catch (Exception e) {}
                matches.add(meta < 0 || meta > 15 ? StateMatch.block(block) : StateMatch.state(block.getStateFromMeta(meta)));
            } catch (Exception e) {
                DeeperDepths.error("Failed reading blockstate " + str, e);
            }
        }
        return matches;
    }

    private interface StateMatch extends Predicate<IBlockState> {

        static StateMatch block(Block block) {
            return state -> state.getBlock() == block;
        }

        static StateMatch state(IBlockState state) {
            return state::equals;
        }

    }

    public static int getVibrationFrequency(SoundEvent sound) {
        if (((ISoundFrequency)sound).isInitialized()) return ((ISoundFrequency) sound).getSoundFrequency();
        if (vibrationFrequencies == null) {
            vibrationFrequencies = Maps.newLinkedHashMap();
            for (String str : vibrationFrequenciesConfig) try {
                String[] split = str.split("-");
                vibrationFrequencies.put(split[0], Integer.parseInt(split[1]));
            } catch (Exception e) {
                DeeperDepths.error("Failed reading sound frequency " + str, e);
            }
        }
        String name = sound.getRegistryName().getResourcePath();
        for (Map.Entry<String, Integer> entry : vibrationFrequencies.entrySet()) if (name.contains(entry.getKey()))
            return ((ISoundFrequency) sound).setSoundFrequency(entry.getValue());
        return ((ISoundFrequency) sound).setSoundFrequency(0);
    }

}
