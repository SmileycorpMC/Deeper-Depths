package com.deeperdepths.common.commands;

import com.deeperdepths.config.WorldConfig;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class CommandLocateDD implements ICommand {
    private final List<String> aliases;

    public CommandLocateDD() {
        aliases = new ArrayList<>();
        aliases.add("locateDeeperDepths");
    }

    @Override
    public String getName() {
        return "locateDeeperDepths";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "locateDeeperDepths <mod location>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("locateDeeperDepths <mod location>");
        } else {
            String s = args[0];
            if(s.equals("TrialChambers")) {
                BlockPos blockpos = findNearestPos(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            } else if(s.equals("AncientCity")) {
                BlockPos blockpos = findNearestPosAncientCity(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            }
        }
    }

    public static BlockPos findNearestPos(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        //probably laggy as hell but hey it works
        for (int i = -190; i < 190 + 1; i++) {
            for (int j = -190; j < 190 + 1; j++) {
                boolean c = IsTrialChambersAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, WorldConfig.trial_chambers.getMinHeight(), (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    public static BlockPos findNearestPosAncientCity(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        //probably laggy as hell but hey it works
        for (int i = -190; i < 190 + 1; i++) {
            for (int j = -190; j < 190 + 1; j++) {
                boolean c = IsAncientCityAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 7, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsTrialChambersAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.trial_chambers.getSpawnChances();
        int separation = 16;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = world.setRandomSeed(k, l, 19930381);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }
    }

    protected static boolean IsAncientCityAtPos(World world, int chunkX, int chunkZ) {
        int spacing = 50;
        int separation = 16;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = world.setRandomSeed(k, l, 19220485);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && world.provider.getDimension() == 0) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHereAncientCity(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.trial_chambers.getDimensions()) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, BiomeDictionary.Type.OCEAN)) {
                return false;
            }
        return true;
    }

    public static boolean isAbleToSpawnHereAncientCity(BlockPos pos, World world) {
        Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
        if(BiomeDictionary.hasType(biomeCurrently, BiomeDictionary.Type.MOUNTAIN)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "TrialChambers", "AncientCity") : Collections.emptyList();
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        List<String> list = Lists.newArrayList();

        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            if (list.isEmpty()) {
                for (Object object : possibleCompletions) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getResourcePath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }

        return list;
    }

    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
