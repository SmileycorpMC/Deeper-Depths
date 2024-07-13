package net.smileycorp.deeperdepths.common.world.chambers;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.smileycorp.deeperdepths.common.world.base.ModRand;

import java.util.List;

public class TrialChambers {
    /**
     * The physical rules for how the structure is made
     */
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private int CORRIDOR_SIZE_LIMIT = 0;

    private int yAxel = 0;


    private int yAxel2Level = 0;

    //Make sure that you put the config option for size here too
    private static final int SIZE = 1;

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
        TrialChambersTemplate template = new TrialChambersTemplate(manager, "c_board", pos, rot, 0, true);
        String[] b_entrance_types = {"e_hall_big_entrance_1", "e_hall_big_entrance_2", "e_hall_big_entrance_3"};
        TrialChambersTemplate templateStart = new TrialChambersTemplate(manager, ModRand.choice(b_entrance_types), pos.add(0, 1, 0), rot, 0,true);
        components.add(template);
        components.add(templateStart);
        generateSecondBoard(template, BlockPos.ORIGIN, rot);
        CORRIDOR_SIZE_LIMIT++;
        System.out.println("Generated Trial Chambers at" + pos);
        TrialChambersTemplate.resetTemplateCount();

    }

    public boolean generateSecondBoard(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template = addAdjustedPieceWithoutCount(parent, pos, "c_board", rot);
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos.add(0, 1, 0), ModRand.choice(c_hall_types), rot);

        CORRIDOR_SIZE_LIMIT++;
        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(template);
        components.add(template_hall_1);
        //fills the two spots inbetween the Trial Chambers
        generateFillPlate2(template_hall_1, pos, rot);

        return true;
    }

    //Second Plate Begin
    public boolean generateFillPlate2(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generateFillPlate3(template_hall_1, pos, rot);
        return true;
    }

    public boolean generateFillPlate3(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);

        if(CORRIDOR_SIZE_LIMIT < 6 && world.rand.nextInt(2) == 0) {
            generateFillPlate4(template_hall_1, pos, rot);
        } else {
            generateEndCorridorPlate(template_hall_1, pos, rot);
        }
        return true;
    }

    public boolean generateFillPlate4(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generateThirdBoard(template_hall_1, pos, rot);
        return true;
    }

    public boolean generateEndCorridorPlate(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_entrance_1", "c_hall_entrance_2", "c_hall_entrance_3"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);

        return true;
    }
    //Second Plate End
    public boolean generateThirdBoard(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template = addAdjustedPieceWithoutCount(parent, pos.add(0, -1, 0), "c_board", rot);
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos.add(0, 1, 0), ModRand.choice(c_hall_types), rot);

        CORRIDOR_SIZE_LIMIT++;
        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(template);
        components.add(template_hall_1);
        //fills the two spots inbetween the Trial Chambers
        generate3FillPlate2(template_hall_1, pos, rot);

        return true;
    }
    //Parts for the 3rd Plate
    public boolean generate3FillPlate2(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generate3FillPlate3(template_hall_1, pos, rot);
        return true;
    }

    public boolean generate3FillPlate3(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generateEndCorridorPlate(template_hall_1, pos, rot);
        return true;
    }
    //End Parts for 3rd Plate

    public boolean generateCorridorBigEntrance(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] b_entrance_types = {"c_hall_big_entrance_1", "c_hall_big_entrance_2", "c_hall_big_entrance_3"};
        TrialChambersTemplate template = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(b_entrance_types), rot);
        components.add(template);

        return true;
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


    private TrialChambersTemplate addAdjustedPieceWithoutCount(TrialChambersTemplate parent, BlockPos pos, String type, Rotation rot) {
        TrialChambersTemplate newTemplate = new TrialChambersTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
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
