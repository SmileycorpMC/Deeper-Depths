import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class DataGen {
    
    static String[] blocks = {
            "block.amethyst_block.break",
            "block.amethyst_block.chime",
            "block.amethyst_block.fall",
            "block.amethyst_block.hit",
            "block.amethyst_block.place",
            "block.amethyst_block.resonate",
            "block.amethyst_block.step",
            "block.amethyst_cluster.break",
            "block.amethyst_cluster.fall",
            "block.amethyst_cluster.hit",
            "block.amethyst_cluster.place",
            "block.amethyst_cluster.step",
            "block.calcite.break",
            "block.calcite.fall",
            "block.calcite.hit",
            "block.calcite.place",
            "block.calcite.step",
            "block.candle.ambient",
            "block.candle.break",
            "block.candle.extinguish",
            "block.candle.fall",
            "block.candle.hit",
            "block.candle.place",
            "block.candle.step",
            "block.copper.break",
            "block.copper.fall",
            "block.copper.hit",
            "block.copper.place",
            "block.copper.step",
            "block.copper_bulb.break",
            "block.copper_bulb.fall",
            "block.copper_bulb.hit",
            "block.copper_bulb.place",
            "block.copper_bulb.step",
            "block.copper_bulb.turn_off",
            "block.copper_bulb.turn_on",
            "block.copper_door.close",
            "block.copper_door.open",
            "block.copper_grate.break",
            "block.copper_grate.fall",
            "block.copper_grate.hit",
            "block.copper_grate.place",
            "block.copper_grate.step",
            "block.copper_trapdoor.close",
            "block.copper_trapdoor.open",
            "block.copper.scrape",
            "block.copper.wax_off",
            "block.trial_pot.break",
            "block.trial_pot.fall",
            "block.trial_pot.hit",
            "block.trial_pot.insert",
            "block.trial_pot.insert_fail",
            "block.trial_pot.place",
            "block.trial_pot.shatter",
            "block.trial_pot.step",
            "block.deepslate.break",
            "block.deepslate.fall",
            "block.deepslate.hit",
            "block.deepslate.place",
            "block.deepslate.step",
            "block.deepslate_bricks.break",
            "block.deepslate_bricks.fall",
            "block.deepslate_bricks.hit",
            "block.deepslate_bricks.place",
            "block.deepslate_bricks.step",
            "block.deepslate_tiles.break",
            "block.deepslate_tiles.fall",
            "block.deepslate_tiles.hit",
            "block.deepslate_tiles.place",
            "block.deepslate_tiles.step",
            "block.heavy_core.break",
            "block.heavy_core.fall",
            "block.heavy_core.hit",
            "block.heavy_core.place",
            "block.heavy_core.step",
            "block.sculk.break",
            "block.sculk.charge",
            "block.sculk.hit",
            "block.sculk.place",
            "block.sculk.spread",
            "block.sculk.step",
            "block.sculk_catalyst.bloom",
            "block.sculk_catalyst.break",
            "block.sculk_catalyst.fall",
            "block.sculk_catalyst.hit",
            "block.sculk_catalyst.place",
            "block.sculk_catalyst.step",
            "block.sculk_sensor.break",
            "block.sculk_sensor.clicking",
            "block.sculk_sensor.clicking_stop",
            "block.sculk_sensor.fall",
            "block.sculk_sensor.hit",
            "block.sculk_sensor.place",
            "block.sculk_sensor.step",
            "block.sculk_shrieker.break",
            "block.sculk_shrieker.fall",
            "block.sculk_shrieker.hit",
            "block.sculk_shrieker.place",
            "block.sculk_shrieker.shriek",
            "block.sculk_shrieker.step",
            "block.sculk_vein.break",
            "block.sculk_vein.fall",
            "block.sculk_vein.hit",
            "block.sculk_vein.place",
            "block.sculk_vein.step",
            "block.trial_spawner.about_to_spawn_item",
            "block.trial_spawner.ambient",
            "block.trial_spawner.ambient_ominous",
            "block.trial_spawner.break",
            "block.trial_spawner.close_shutter",
            "block.trial_spawner.detect_player",
            "block.trial_spawner.eject_item",
            "block.trial_spawner.fall",
            "block.trial_spawner.hit",
            "block.trial_spawner.ominous_activate",
            "block.trial_spawner.open_shutter",
            "block.trial_spawner.place",
            "block.trial_spawner.spawn_item",
            "block.trial_spawner.spawn_item_begin",
            "block.trial_spawner.spawn_mob",
            "block.trial_spawner.step",
            "block.tuff.break",
            "block.tuff.fall",
            "block.tuff.hit",
            "block.tuff.place",
            "block.tuff.step",
            "block.tuff_bricks.break",
            "block.tuff_bricks.fall",
            "block.tuff_bricks.hit",
            "block.tuff_bricks.place",
            "block.tuff_bricks.step",
            "block.vault.activate",
            "block.vault.ambient",
            "block.vault.break",
            "block.vault.close_shutter",
            "block.vault.deactivate",
            "block.vault.eject_item",
            "block.vault.fall",
            "block.vault.hit",
            "block.vault.insert_item",
            "block.vault.insert_item_fail",
            "block.vault.open_shutter",
            "block.vault.place",
            "block.vault.reject_rewarded_player",
            "block.vault.step",
            "entity.bogged.ambient",
            "entity.bogged.death",
            "entity.bogged.hurt",
            "entity.bogged.shear",
            "entity.bogged.step",
            "entity.breeze.charge",
            "entity.breeze.death",
            "entity.breeze.deflect",
            "entity.breeze.hurt",
            "entity.breeze.idle_air",
            "entity.breeze.idle_ground",
            "entity.breeze.inhale",
            "entity.breeze.jump",
            "entity.breeze.land",
            "entity.breeze.shoot",
            "entity.breeze.slide",
            "entity.breeze.whirl",
            "entity.breeze.wind_burst",
            "entity.warden.agitated",
            "entity.warden.ambient",
            "entity.warden.angry",
            "entity.warden.attack_impact",
            "entity.warden.death",
            "entity.warden.dig",
            "entity.warden.emerge",
            "entity.warden.heartbeat",
            "entity.warden.hurt",
            "entity.warden.listening",
            "entity.warden.listening_angry",
            "entity.warden.nearby_close",
            "entity.warden.nearby_closer",
            "entity.warden.nearby_closest",
            "entity.warden.roar",
            "entity.warden.sniff",
            "entity.warden.sonic_boom",
            "entity.warden.sonic_charge",
            "entity.warden.step",
            "entity.warden.tendril_clicks",
            "entity.wind_charge.throw",
            "entity.wind_charge.wind_burst",
            "event.mob_effect.bad_omen",
            "event.mob_effect.trial_omen",
            "item.mace.smash_air",
            "item.mace.smash_ground",
            "item.mace.smash_ground_heavy",
            "item.spyglass.stop_using",
            "item.spyglass.use",
            "item.ominous_bottle.dispose"
    };
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static JsonParser parser = new JsonParser();
    
    public static void main(String... args) throws IOException, URISyntaxException {
       try {
           File file = new File("D:/data/misc/sounds.txt");
           file.createNewFile();
           try(FileWriter writer = new FileWriter(file)) {
               for (String sound : blocks) {
                   String[] split = sound.split("\\.");
                   writer.append("public static final SoundEvent " + (split[1] + "_" + split[2]).toUpperCase() + " = new SoundEvent(Constants.loc(" + sound + "));\n");
               }
           }
           /*for (EnumWeatherStage stage : EnumWeatherStage.values()) {
               String name = "copper_trapdoor";
               if (stage != EnumWeatherStage.NORMAL) name = stage.getName() + "_" + name.replace("_block", "");
               for (String shape : new String[]{"_top", "_bottom", "_open"}) generateModel(name, shape);
               //generateModel("waxed_" + name);
           }
           /*for (String block : blocks) {
               generateState(block.toLowerCase());
               generateModel(block, "post", false);
               generateModel(block, "side", false);
               generateModel(block, "", true);
           }*/
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    static void generateModel(String name, String shape) throws IOException {
        String path = "D:/data/trapdoors/models/block" + "/" + name + shape + ".json";
        File file = new File(path);
        file.createNewFile();
        JsonObject json = new JsonObject();
        json.addProperty("parent", "block/trapdoor" + shape);
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", "deeperdepths:blocks/" + name);
        json.add("textures", textures);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(json));
        }
    }
    
    static void generateState(String name) throws IOException {
        String path = "D:/data/walls/blockstates/" + name + "_wall.json";
        System.out.println(path);
        File file = new File(path);
        file.createNewFile();
        JsonObject json = new JsonObject();
        JsonArray multipart = new JsonArray();
        multipart.add(getProperty("up", "deeperdepths:" + name + "_wall_post", 0));
        multipart.add(getProperty("north", "deeperdepths:" + name + "_wall_side", 0));
        multipart.add(getProperty("east", "deeperdepths:" + name + "_wall_side", 90));
        multipart.add(getProperty("south", "deeperdepths:" + name + "_wall_side", 180));
        multipart.add(getProperty("west", "deeperdepths:" + name + "_wall_side", 270));
        json.add("multipart", multipart);
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(json));
        }
    }
    
    static JsonObject getProperty(String property, String model, int rotation) {
        JsonObject obj = new JsonObject();
        JsonObject when = new JsonObject();
        when.addProperty(property, true);
        obj.add("when", when);
        JsonObject apply = new JsonObject();
        apply.addProperty("model",  model);
        if (rotation != 0) apply.addProperty("y", rotation);
        if (!model.contains("post")) apply.addProperty("uvlock", true);
        obj.add("apply", apply);
        return obj;
    }
    
}
