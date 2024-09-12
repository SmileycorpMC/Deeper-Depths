package com.deeperdepths.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class DeeperDepthsSoundEvents {

    private static List<SoundEvent> soundList = new ArrayList<SoundEvent>();

    //amethyst
    public static final SoundEvent AMETHYST_BLOCK_BREAK = readyForRegistry(Constants.loc("block.amethyst_block.break"));
    public static final SoundEvent AMETHYST_BLOCK_CHIME = readyForRegistry(Constants.loc("block.amethyst_block.chime"));
    public static final SoundEvent AMETHYST_BLOCK_FALL = readyForRegistry(Constants.loc("block.amethyst_block.fall"));
    public static final SoundEvent AMETHYST_BLOCK_HIT = readyForRegistry(Constants.loc("block.amethyst_block.hit"));
    public static final SoundEvent AMETHYST_BLOCK_PLACE = readyForRegistry(Constants.loc("block.amethyst_block.place"));
    public static final SoundEvent AMETHYST_BLOCK_RESONATE = readyForRegistry(Constants.loc("block.amethyst_block.resonate"));
    public static final SoundEvent AMETHYST_BLOCK_STEP = readyForRegistry(Constants.loc("block.amethyst_block.step"));
    
    //amethyst cluster
    public static final SoundEvent AMETHYST_CLUSTER_BREAK = readyForRegistry(Constants.loc("block.amethyst_cluster.break"));
    public static final SoundEvent AMETHYST_CLUSTER_FALL = readyForRegistry(Constants.loc("block.amethyst_cluster.fall"));
    public static final SoundEvent AMETHYST_CLUSTER_HIT = readyForRegistry(Constants.loc("block.amethyst_cluster.hit"));
    public static final SoundEvent AMETHYST_CLUSTER_PLACE = readyForRegistry(Constants.loc("block.amethyst_cluster.place"));
    public static final SoundEvent AMETHYST_CLUSTER_STEP = readyForRegistry(Constants.loc("block.amethyst_cluster.step"));
    
    //calcite
    public static final SoundEvent CALCITE_BREAK = readyForRegistry(Constants.loc("block.calcite.break"));
    public static final SoundEvent CALCITE_FALL = readyForRegistry(Constants.loc("block.calcite.fall"));
    public static final SoundEvent CALCITE_HIT = readyForRegistry(Constants.loc("block.calcite.hit"));
    public static final SoundEvent CALCITE_PLACE = readyForRegistry(Constants.loc("block.calcite.place"));
    public static final SoundEvent CALCITE_STEP = readyForRegistry(Constants.loc("block.calcite.step"));
    
    //candles
    public static final SoundEvent CANDLE_AMBIENT = readyForRegistry(Constants.loc("block.candle.ambient"));
    public static final SoundEvent CANDLE_BREAK = readyForRegistry(Constants.loc("block.candle.break"));
    public static final SoundEvent CANDLE_EXTINGUISH = readyForRegistry(Constants.loc("block.candle.extinguish"));
    public static final SoundEvent CANDLE_FALL = readyForRegistry(Constants.loc("block.candle.fall"));
    public static final SoundEvent CANDLE_HIT = readyForRegistry(Constants.loc("block.candle.hit"));
    public static final SoundEvent CANDLE_PLACE = readyForRegistry(Constants.loc("block.candle.place"));
    public static final SoundEvent CANDLE_STEP = readyForRegistry(Constants.loc("block.candle.step"));
    
    //copper
    public static final SoundEvent COPPER_BREAK = readyForRegistry(Constants.loc("block.copper.break"));
    public static final SoundEvent COPPER_FALL = readyForRegistry(Constants.loc("block.copper.fall"));
    public static final SoundEvent COPPER_HIT = readyForRegistry(Constants.loc("block.copper.hit"));
    public static final SoundEvent COPPER_PLACE = readyForRegistry(Constants.loc("block.copper.place"));
    public static final SoundEvent COPPER_STEP = readyForRegistry(Constants.loc("block.copper.step"));
    public static final SoundEvent COPPER_SCRAPE = readyForRegistry(Constants.loc("block.copper.scrape"));
    public static final SoundEvent COPPER_WAX_OFF = readyForRegistry(Constants.loc("block.copper.wax_off"));
    public static final SoundEvent COPPER_WAX_ON = readyForRegistry(Constants.loc("block.copper.wax_on"));
    
    //copper bulb
    public static final SoundEvent COPPER_BULB_BREAK = readyForRegistry(Constants.loc("block.copper_bulb.break"));
    public static final SoundEvent COPPER_BULB_FALL = readyForRegistry(Constants.loc("block.copper_bulb.fall"));
    public static final SoundEvent COPPER_BULB_HIT = readyForRegistry(Constants.loc("block.copper_bulb.hit"));
    public static final SoundEvent COPPER_BULB_PLACE = readyForRegistry(Constants.loc("block.copper_bulb.place"));
    public static final SoundEvent COPPER_BULB_STEP = readyForRegistry(Constants.loc("block.copper_bulb.step"));
    public static final SoundEvent COPPER_BULB_TURN_OFF = readyForRegistry(Constants.loc("block.copper_bulb.turn_off"));
    public static final SoundEvent COPPER_BULB_TURN_ON = readyForRegistry(Constants.loc("block.copper_bulb.turn_on"));
    
    //copper door
    public static final SoundEvent COPPER_DOOR_CLOSE = readyForRegistry(Constants.loc("block.copper_door.close"));
    public static final SoundEvent COPPER_DOOR_OPEN = readyForRegistry(Constants.loc("block.copper_door.open"));
    
    //copper grate
    public static final SoundEvent COPPER_GRATE_BREAK = readyForRegistry(Constants.loc("block.copper_grate.break"));
    public static final SoundEvent COPPER_GRATE_FALL = readyForRegistry(Constants.loc("block.copper_grate.fall"));
    public static final SoundEvent COPPER_GRATE_HIT = readyForRegistry(Constants.loc("block.copper_grate.hit"));
    public static final SoundEvent COPPER_GRATE_PLACE = readyForRegistry(Constants.loc("block.copper_grate.place"));
    public static final SoundEvent COPPER_GRATE_STEP = readyForRegistry(Constants.loc("block.copper_grate.step"));
    
    //copper trapdoor
    public static final SoundEvent COPPER_TRAPDOOR_CLOSE = readyForRegistry(Constants.loc("block.copper_trapdoor.close"));
    public static final SoundEvent COPPER_TRAPDOOR_OPEN = readyForRegistry(Constants.loc("block.copper_trapdoor.open"));
    
    //trial pot
    public static final SoundEvent TRIAL_POT_BREAK = readyForRegistry(Constants.loc("block.trial_pot.break"));
    public static final SoundEvent TRIAL_POT_FALL = readyForRegistry(Constants.loc("block.trial_pot.fall"));
    public static final SoundEvent TRIAL_POT_HIT = readyForRegistry(Constants.loc("block.trial_pot.hit"));
    public static final SoundEvent TRIAL_POT_INSERT = readyForRegistry(Constants.loc("block.trial_pot.insert"));
    public static final SoundEvent TRIAL_POT_INSERT_FAIL = readyForRegistry(Constants.loc("block.trial_pot.insert_fail"));
    public static final SoundEvent TRIAL_POT_PLACE = readyForRegistry(Constants.loc("block.trial_pot.place"));
    public static final SoundEvent TRIAL_POT_SHATTER = readyForRegistry(Constants.loc("block.trial_pot.shatter"));
    public static final SoundEvent TRIAL_POT_STEP = readyForRegistry(Constants.loc("block.trial_pot.step"));
    
    //deepslate
    public static final SoundEvent DEEPSLATE_BREAK = readyForRegistry(Constants.loc("block.deepslate.break"));
    public static final SoundEvent DEEPSLATE_FALL = readyForRegistry(Constants.loc("block.deepslate.fall"));
    public static final SoundEvent DEEPSLATE_HIT = readyForRegistry(Constants.loc("block.deepslate.hit"));
    public static final SoundEvent DEEPSLATE_PLACE = readyForRegistry(Constants.loc("block.deepslate.place"));
    public static final SoundEvent DEEPSLATE_STEP = readyForRegistry(Constants.loc("block.deepslate.step"));
    
    //deepslate bricks
    public static final SoundEvent DEEPSLATE_BRICKS_BREAK = readyForRegistry(Constants.loc("block.deepslate_bricks.break"));
    public static final SoundEvent DEEPSLATE_BRICKS_FALL = readyForRegistry(Constants.loc("block.deepslate_bricks.fall"));
    public static final SoundEvent DEEPSLATE_BRICKS_HIT = readyForRegistry(Constants.loc("block.deepslate_bricks.hit"));
    public static final SoundEvent DEEPSLATE_BRICKS_PLACE = readyForRegistry(Constants.loc("block.deepslate_bricks.place"));
    public static final SoundEvent DEEPSLATE_BRICKS_STEP = readyForRegistry(Constants.loc("block.deepslate_bricks.step"));
    
    //deepslate tiles
    public static final SoundEvent DEEPSLATE_TILES_BREAK = readyForRegistry(Constants.loc("block.deepslate_tiles.break"));
    public static final SoundEvent DEEPSLATE_TILES_FALL = readyForRegistry(Constants.loc("block.deepslate_tiles.fall"));
    public static final SoundEvent DEEPSLATE_TILES_HIT = readyForRegistry(Constants.loc("block.deepslate_tiles.hit"));
    public static final SoundEvent DEEPSLATE_TILES_PLACE = readyForRegistry(Constants.loc("block.deepslate_tiles.place"));
    public static final SoundEvent DEEPSLATE_TILES_STEP = readyForRegistry(Constants.loc("block.deepslate_tiles.step"));
    
    //heavy core
    public static final SoundEvent HEAVY_CORE_BREAK = readyForRegistry(Constants.loc("block.heavy_core.break"));
    public static final SoundEvent HEAVY_CORE_FALL = readyForRegistry(Constants.loc("block.heavy_core.fall"));
    public static final SoundEvent HEAVY_CORE_HIT = readyForRegistry(Constants.loc("block.heavy_core.hit"));
    public static final SoundEvent HEAVY_CORE_PLACE = readyForRegistry(Constants.loc("block.heavy_core.place"));
    public static final SoundEvent HEAVY_CORE_STEP = readyForRegistry(Constants.loc("block.heavy_core.step"));
    
    //sculk
    public static final SoundEvent SCULK_BREAK = readyForRegistry(Constants.loc("block.sculk.break"));
    public static final SoundEvent SCULK_CHARGE = readyForRegistry(Constants.loc("block.sculk.charge"));
    public static final SoundEvent SCULK_HIT = readyForRegistry(Constants.loc("block.sculk.hit"));
    public static final SoundEvent SCULK_PLACE = readyForRegistry(Constants.loc("block.sculk.place"));
    public static final SoundEvent SCULK_SPREAD = readyForRegistry(Constants.loc("block.sculk.spread"));
    public static final SoundEvent SCULK_STEP = readyForRegistry(Constants.loc("block.sculk.step"));
    
    //sculk catalyst
    public static final SoundEvent SCULK_CATALYST_BLOOM = readyForRegistry(Constants.loc("block.sculk_catalyst.bloom"));
    public static final SoundEvent SCULK_CATALYST_BREAK = readyForRegistry(Constants.loc("block.sculk_catalyst.break"));
    public static final SoundEvent SCULK_CATALYST_FALL = readyForRegistry(Constants.loc("block.sculk_catalyst.fall"));
    public static final SoundEvent SCULK_CATALYST_HIT = readyForRegistry(Constants.loc("block.sculk_catalyst.hit"));
    public static final SoundEvent SCULK_CATALYST_PLACE = readyForRegistry(Constants.loc("block.sculk_catalyst.place"));
    public static final SoundEvent SCULK_CATALYST_STEP = readyForRegistry(Constants.loc("block.sculk_catalyst.step"));
    
    //sculk sensor
    public static final SoundEvent SCULK_SENSOR_BREAK = readyForRegistry(Constants.loc("block.sculk_sensor.break"));
    public static final SoundEvent SCULK_SENSOR_CLICKING = readyForRegistry(Constants.loc("block.sculk_sensor.clicking"));
    public static final SoundEvent SCULK_SENSOR_CLICKING_STOP = readyForRegistry(Constants.loc("block.sculk_sensor.clicking_stop"));
    public static final SoundEvent SCULK_SENSOR_FALL = readyForRegistry(Constants.loc("block.sculk_sensor.fall"));
    public static final SoundEvent SCULK_SENSOR_HIT = readyForRegistry(Constants.loc("block.sculk_sensor.hit"));
    public static final SoundEvent SCULK_SENSOR_PLACE = readyForRegistry(Constants.loc("block.sculk_sensor.place"));
    public static final SoundEvent SCULK_SENSOR_STEP = readyForRegistry(Constants.loc("block.sculk_sensor.step"));
    
    //sculk shrieker
    public static final SoundEvent SCULK_SHRIEKER_BREAK = readyForRegistry(Constants.loc("block.sculk_shrieker.break"));
    public static final SoundEvent SCULK_SHRIEKER_FALL = readyForRegistry(Constants.loc("block.sculk_shrieker.fall"));
    public static final SoundEvent SCULK_SHRIEKER_HIT = readyForRegistry(Constants.loc("block.sculk_shrieker.hit"));
    public static final SoundEvent SCULK_SHRIEKER_PLACE = readyForRegistry(Constants.loc("block.sculk_shrieker.place"));
    public static final SoundEvent SCULK_SHRIEKER_SHRIEK = readyForRegistry(Constants.loc("block.sculk_shrieker.shriek"));
    public static final SoundEvent SCULK_SHRIEKER_STEP = readyForRegistry(Constants.loc("block.sculk_shrieker.step"));
    
    //sculk vein
    public static final SoundEvent SCULK_VEIN_BREAK = readyForRegistry(Constants.loc("block.sculk_vein.break"));
    public static final SoundEvent SCULK_VEIN_FALL = readyForRegistry(Constants.loc("block.sculk_vein.fall"));
    public static final SoundEvent SCULK_VEIN_HIT = readyForRegistry(Constants.loc("block.sculk_vein.hit"));
    public static final SoundEvent SCULK_VEIN_PLACE = readyForRegistry(Constants.loc("block.sculk_vein.place"));
    public static final SoundEvent SCULK_VEIN_STEP = readyForRegistry(Constants.loc("block.sculk_vein.step"));
    
    //trial spawner
    public static final SoundEvent TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM = readyForRegistry(Constants.loc("block.trial_spawner.about_to_spawn_item"));
    public static final SoundEvent TRIAL_SPAWNER_AMBIENT = readyForRegistry(Constants.loc("block.trial_spawner.ambient"));
    public static final SoundEvent TRIAL_SPAWNER_AMBIENT_OMINOUS = readyForRegistry(Constants.loc("block.trial_spawner.ambient_ominous"));
    public static final SoundEvent TRIAL_SPAWNER_BREAK = readyForRegistry(Constants.loc("block.trial_spawner.break"));
    public static final SoundEvent TRIAL_SPAWNER_CLOSE_SHUTTER = readyForRegistry(Constants.loc("block.trial_spawner.close_shutter"));
    public static final SoundEvent TRIAL_SPAWNER_DETECT_PLAYER = readyForRegistry(Constants.loc("block.trial_spawner.detect_player"));
    public static final SoundEvent TRIAL_SPAWNER_EJECT_ITEM = readyForRegistry(Constants.loc("block.trial_spawner.eject_item"));
    public static final SoundEvent TRIAL_SPAWNER_FALL = readyForRegistry(Constants.loc("block.trial_spawner.fall"));
    public static final SoundEvent TRIAL_SPAWNER_HIT = readyForRegistry(Constants.loc("block.trial_spawner.hit"));
    public static final SoundEvent TRIAL_SPAWNER_OMINOUS_ACTIVATE = readyForRegistry(Constants.loc("block.trial_spawner.ominous_activate"));
    public static final SoundEvent TRIAL_SPAWNER_OPEN_SHUTTER = readyForRegistry(Constants.loc("block.trial_spawner.open_shutter"));
    public static final SoundEvent TRIAL_SPAWNER_PLACE = readyForRegistry(Constants.loc("block.trial_spawner.place"));
    public static final SoundEvent TRIAL_SPAWNER_SPAWN_ITEM = readyForRegistry(Constants.loc("block.trial_spawner.spawn_item"));
    public static final SoundEvent TRIAL_SPAWNER_SPAWN_ITEM_BEGIN = readyForRegistry(Constants.loc("block.trial_spawner.spawn_item_begin"));
    public static final SoundEvent TRIAL_SPAWNER_SPAWN_MOB = readyForRegistry(Constants.loc("block.trial_spawner.spawn_mob"));
    public static final SoundEvent TRIAL_SPAWNER_STEP = readyForRegistry(Constants.loc("block.trial_spawner.step"));
    
    //tuff
    public static final SoundEvent TUFF_BREAK = readyForRegistry(Constants.loc("block.tuff.break"));
    public static final SoundEvent TUFF_FALL = readyForRegistry(Constants.loc("block.tuff.fall"));
    public static final SoundEvent TUFF_HIT = readyForRegistry(Constants.loc("block.tuff.hit"));
    public static final SoundEvent TUFF_PLACE = readyForRegistry(Constants.loc("block.tuff.place"));
    public static final SoundEvent TUFF_STEP = readyForRegistry(Constants.loc("block.tuff.step"));
    
    //tuff bricks
    public static final SoundEvent TUFF_BRICKS_BREAK = readyForRegistry(Constants.loc("block.tuff_bricks.break"));
    public static final SoundEvent TUFF_BRICKS_FALL = readyForRegistry(Constants.loc("block.tuff_bricks.fall"));
    public static final SoundEvent TUFF_BRICKS_HIT = readyForRegistry(Constants.loc("block.tuff_bricks.hit"));
    public static final SoundEvent TUFF_BRICKS_PLACE = readyForRegistry(Constants.loc("block.tuff_bricks.place"));
    public static final SoundEvent TUFF_BRICKS_STEP = readyForRegistry(Constants.loc("block.tuff_bricks.step"));
    
    //vault
    public static final SoundEvent VAULT_ACTIVATE = readyForRegistry(Constants.loc("block.vault.activate"));
    public static final SoundEvent VAULT_AMBIENT = readyForRegistry(Constants.loc("block.vault.ambient"));
    public static final SoundEvent VAULT_BREAK = readyForRegistry(Constants.loc("block.vault.break"));
    public static final SoundEvent VAULT_CLOSE_SHUTTER = readyForRegistry(Constants.loc("block.vault.close_shutter"));
    public static final SoundEvent VAULT_DEACTIVATE = readyForRegistry(Constants.loc("block.vault.deactivate"));
    public static final SoundEvent VAULT_EJECT_ITEM = readyForRegistry(Constants.loc("block.vault.eject_item"));
    public static final SoundEvent VAULT_FALL = readyForRegistry(Constants.loc("block.vault.fall"));
    public static final SoundEvent VAULT_HIT = readyForRegistry(Constants.loc("block.vault.hit"));
    public static final SoundEvent VAULT_INSERT_ITEM = readyForRegistry(Constants.loc("block.vault.insert_item"));
    public static final SoundEvent VAULT_INSERT_ITEM_FAIL = readyForRegistry(Constants.loc("block.vault.insert_item_fail"));
    public static final SoundEvent VAULT_OPEN_SHUTTER = readyForRegistry(Constants.loc("block.vault.open_shutter"));
    public static final SoundEvent VAULT_PLACE = readyForRegistry(Constants.loc("block.vault.place"));
    public static final SoundEvent VAULT_REJECT_REWARDED_PLAYER = readyForRegistry(Constants.loc("block.vault.reject_rewarded_player"));
    public static final SoundEvent VAULT_STEP = readyForRegistry(Constants.loc("block.vault.step"));
    
    //bogged
    public static final SoundEvent BOGGED_AMBIENT = readyForRegistry(Constants.loc("entity.bogged.ambient"));
    public static final SoundEvent BOGGED_DEATH = readyForRegistry(Constants.loc("entity.bogged.death"));
    public static final SoundEvent BOGGED_HURT = readyForRegistry(Constants.loc("entity.bogged.hurt"));
    public static final SoundEvent BOGGED_SHEAR = readyForRegistry(Constants.loc("entity.bogged.shear"));
    public static final SoundEvent BOGGED_STEP = readyForRegistry(Constants.loc("entity.bogged.step"));
    
    //breeze
    public static final SoundEvent BREEZE_CHARGE = readyForRegistry(Constants.loc("entity.breeze.charge"));
    public static final SoundEvent BREEZE_DEATH = readyForRegistry(Constants.loc("entity.breeze.death"));
    public static final SoundEvent BREEZE_DEFLECT = readyForRegistry(Constants.loc("entity.breeze.deflect"));
    public static final SoundEvent BREEZE_HURT = readyForRegistry(Constants.loc("entity.breeze.hurt"));
    public static final SoundEvent BREEZE_IDLE_AIR = readyForRegistry(Constants.loc("entity.breeze.idle_air"));
    public static final SoundEvent BREEZE_IDLE_GROUND = readyForRegistry(Constants.loc("entity.breeze.idle_ground"));
    public static final SoundEvent BREEZE_INHALE = readyForRegistry(Constants.loc("entity.breeze.inhale"));
    public static final SoundEvent BREEZE_JUMP = readyForRegistry(Constants.loc("entity.breeze.jump"));
    public static final SoundEvent BREEZE_LAND = readyForRegistry(Constants.loc("entity.breeze.land"));
    public static final SoundEvent BREEZE_SHOOT = readyForRegistry(Constants.loc("entity.breeze.shoot"));
    public static final SoundEvent BREEZE_SLIDE = readyForRegistry(Constants.loc("entity.breeze.slide"));
    public static final SoundEvent BREEZE_WHIRL = readyForRegistry(Constants.loc("entity.breeze.whirl"));
    public static final SoundEvent BREEZE_WIND_BURST = readyForRegistry(Constants.loc("entity.breeze.wind_burst"));
    
    //warden
    public static final SoundEvent WARDEN_AGITATED = readyForRegistry(Constants.loc("entity.warden.agitated"));
    public static final SoundEvent WARDEN_AMBIENT = readyForRegistry(Constants.loc("entity.warden.ambient"));
    public static final SoundEvent WARDEN_ANGRY = readyForRegistry(Constants.loc("entity.warden.angry"));
    public static final SoundEvent WARDEN_ATTACK_IMPACT = readyForRegistry(Constants.loc("entity.warden.attack_impact"));
    public static final SoundEvent WARDEN_DEATH = readyForRegistry(Constants.loc("entity.warden.death"));
    public static final SoundEvent WARDEN_DIG = readyForRegistry(Constants.loc("entity.warden.dig"));
    public static final SoundEvent WARDEN_EMERGE = readyForRegistry(Constants.loc("entity.warden.emerge"));
    public static final SoundEvent WARDEN_HEARTBEAT = readyForRegistry(Constants.loc("entity.warden.heartbeat"));
    public static final SoundEvent WARDEN_HURT = readyForRegistry(Constants.loc("entity.warden.hurt"));
    public static final SoundEvent WARDEN_LISTENING = readyForRegistry(Constants.loc("entity.warden.listening"));
    public static final SoundEvent WARDEN_LISTENING_ANGRY = readyForRegistry(Constants.loc("entity.warden.listening_angry"));
    public static final SoundEvent WARDEN_NEARBY_CLOSE = readyForRegistry(Constants.loc("entity.warden.nearby_close"));
    public static final SoundEvent WARDEN_NEARBY_CLOSER = readyForRegistry(Constants.loc("entity.warden.nearby_closer"));
    public static final SoundEvent WARDEN_NEARBY_CLOSEST = readyForRegistry(Constants.loc("entity.warden.nearby_closest"));
    public static final SoundEvent WARDEN_ROAR = readyForRegistry(Constants.loc("entity.warden.roar"));
    public static final SoundEvent WARDEN_SNIFF = readyForRegistry(Constants.loc("entity.warden.sniff"));
    public static final SoundEvent WARDEN_SONIC_BOOM = readyForRegistry(Constants.loc("entity.warden.sonic_boom"));
    public static final SoundEvent WARDEN_SONIC_CHARGE = readyForRegistry(Constants.loc("entity.warden.sonic_charge"));
    public static final SoundEvent WARDEN_STEP = readyForRegistry(Constants.loc("entity.warden.step"));
    public static final SoundEvent WARDEN_TENDRIL_CLICKS = readyForRegistry(Constants.loc("entity.warden.tendril_clicks"));
    
    //wind charge
    public static final SoundEvent WIND_CHARGE_THROW = readyForRegistry(Constants.loc("entity.wind_charge.throw"));
    public static final SoundEvent WIND_CHARGE_WIND_BURST = readyForRegistry(Constants.loc("entity.wind_charge.wind_burst"));
    
    //bad omen
    public static final SoundEvent MOB_EFFECT_BAD_OMEN = readyForRegistry(Constants.loc("event.mob_effect.bad_omen"));
    public static final SoundEvent MOB_EFFECT_TRIAL_OMEN = readyForRegistry(Constants.loc("event.mob_effect.trial_omen"));
    
    //mace
    public static final SoundEvent MACE_SMASH_AIR = readyForRegistry(Constants.loc("item.mace.smash_air"));
    public static final SoundEvent MACE_SMASH_GROUND = readyForRegistry(Constants.loc("item.mace.smash_ground"));
    public static final SoundEvent MACE_SMASH_GROUND_HEAVY = readyForRegistry(Constants.loc("item.mace.smash_ground_heavy"));
    
    //spyglass
    public static final SoundEvent SPYGLASS_STOP_USING = readyForRegistry(Constants.loc("item.spyglass.stop_using"));
    public static final SoundEvent SPYGLASS_USE = readyForRegistry(Constants.loc("item.spyglass.use"));
    
    //ominous bottle
    public static final SoundEvent OMINOUS_BOTTLE_DISPOSE = readyForRegistry(Constants.loc("item.ominous_bottle.dispose"));

    public static void registerSounds()
    { for (SoundEvent sounds : soundList) ForgeRegistries.SOUND_EVENTS.register(sounds); }

    public static SoundEvent readyForRegistry(ResourceLocation resourceIn)
    {
        SoundEvent event = new SoundEvent(resourceIn);
        event.setRegistryName(resourceIn.toString());
        soundList.add(event);
        return event;
    }
}