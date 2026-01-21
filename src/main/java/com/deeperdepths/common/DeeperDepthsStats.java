package com.deeperdepths.common;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;

public class DeeperDepthsStats {

    public static void init() {}

    public static final StatBase DUPED_COPPER = (new StatBasic("stat.deeperdepths.dupedCopper", new TextComponentTranslation("stat.deeperdepths.duped_copper"))).registerStat();

}
