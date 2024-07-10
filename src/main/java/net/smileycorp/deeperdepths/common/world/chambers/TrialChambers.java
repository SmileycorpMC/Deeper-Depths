package net.smileycorp.deeperdepths.common.world.chambers;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class TrialChambers {
    /**
     * The physical rules for how the structure is made
     */
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;


    private int yAxel = 0;


    private int yAxel2Level = 0;

    //Make sure that you put the config option for size here too
    private static final int SIZE = 5;

    //this is just for a second floor used by my structures, you can realistically go as infinite with floors and levels as you'd like
    private static final int SECOND_SIZE = SIZE * 2;

    private boolean hasSpawnedSecondlevel = false;

    private boolean hasGeneratedBossRooms = false;



    public TrialChambers(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startChambers(BlockPos pos, Rotation rot) {

    }


    public boolean generateCross(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        return false;
    }

    private boolean generateCorridor(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        return false;
    }

    private boolean generateEnd(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        return false;
    }

    private boolean generateConnectPiece(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        return false;
    }




    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private TrialChambersTemplate addAdjustedPiece(TrialChambersTemplate parent, BlockPos pos, String type, Rotation rot) {
        TrialChambersTemplate newTemplate = new TrialChambersTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(TrialChambersTemplate parent, TrialChambersTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }


}
