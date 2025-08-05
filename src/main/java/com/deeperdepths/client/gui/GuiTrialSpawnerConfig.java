package com.deeperdepths.client.gui;

import com.deeperdepths.common.blocks.tiles.TileTrialSpawner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;

public class GuiTrialSpawnerConfig extends GuiScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation("textures/gui/demo_background.png");

    protected static int guiWidth = 168;
    protected static int guiHeight = 180;

    private final TileTrialSpawner.Config config, ominousConfig;
    private boolean ominous = false;

    private GuiButton cancel, confirm;

    protected final int x;
    protected final int y;

    public GuiTrialSpawnerConfig(TileTrialSpawner.Config config, TileTrialSpawner.Config ominousConfig) {
        this.config = config;
        this.ominousConfig = ominousConfig;
        x = (width - guiWidth) / 2;
        y = (height - guiHeight) / 2;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        cancel = addButton(new GuiButton(1, x, y + guiHeight, 150, 20, I18n.format("gui.cancel")));
        confirm = addButton(new GuiButton(2, x + guiWidth - 150, y + guiHeight, 150, 20, I18n.format("gui.confirm")));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GuiUtils.drawContinuousTexturedBox(BG_TEXTURE, x, y, 4, 0, width, height, 255, 183, 0, 4, 4, 4, 1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private TileTrialSpawner.Config getActiveConfig() {
        return ominous ? ominousConfig : config;
    }

}
