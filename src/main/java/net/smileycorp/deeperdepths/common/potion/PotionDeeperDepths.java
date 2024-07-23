package net.smileycorp.deeperdepths.common.potion;

import net.minecraft.potion.Potion;
import net.smileycorp.deeperdepths.common.Constants;

import java.util.Locale;

public class PotionDeeperDepths extends Potion {
    
    protected PotionDeeperDepths(String name, boolean isBad, int color) {
        super(isBad, color);
        setPotionName("effect.deeperdepths." + name.replace("_", ""));
        setRegistryName(Constants.loc(name.toLowerCase(Locale.US)));
    }
    
}
