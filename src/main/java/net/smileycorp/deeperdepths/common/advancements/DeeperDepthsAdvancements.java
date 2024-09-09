package net.smileycorp.deeperdepths.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class DeeperDepthsAdvancements {
    
    public static DeeperDepthsCriterionTrigger AW_DANG_IT = new DeeperDepthsCriterionTrigger("aw_dang_it");
    public static DeeperDepthsCriterionTrigger LETS_GO_GAMBLING = new DeeperDepthsCriterionTrigger("lets_go_gambling");
    public static DeeperDepthsCriterionTrigger WAX_ON = new DeeperDepthsCriterionTrigger("wax_on");
    public static DeeperDepthsCriterionTrigger WAX_OFF = new DeeperDepthsCriterionTrigger("wax_off");
    public static DeeperDepthsCriterionTrigger OVER_OVERKILL = new DeeperDepthsCriterionTrigger("over_overkill");
    
    public static void init() {
        CriteriaTriggers.register(LETS_GO_GAMBLING);
        CriteriaTriggers.register(AW_DANG_IT);
        CriteriaTriggers.register(WAX_ON);
        CriteriaTriggers.register(WAX_OFF);
        CriteriaTriggers.register(OVER_OVERKILL);
    }
    
}
