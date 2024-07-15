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


    private boolean isSecondMainCorridor = false;

    private boolean hasGeneratedAConnect = false;
    private boolean isLeft = false;
    private boolean isRight = false;

    private int yAxel2Level = 0;

    //Make sure that you put the config option for size here too
    private static final int SIZE = 3;

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

        if(!hasGeneratedAConnect) {
            if(this.isRight && !this.isLeft) {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos.add(0, 1, 0), rot, ModRand.range(1, 4));
            } else if (this.isLeft && !this.isRight) {
                components.remove(template_hall_1);
                generateRightConnect(parent, pos.add(0, 1, 0), rot, ModRand.range(1, 4));
            }
            //Fail Safe
            else {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos.add(0, 1, 0), rot, ModRand.range(1, 4));
            }
            hasGeneratedAConnect = true;
        }
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
        hasGeneratedAConnect = false;
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

        if(!hasGeneratedAConnect) {
            if(this.isRight && !this.isLeft) {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            } else if (this.isLeft && !this.isRight) {
                components.remove(template_hall_1);
                generateRightConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            //Fail Safe
            else {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            hasGeneratedAConnect = true;
        }

        return true;
    }

    public boolean generateFillPlate4(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generateThirdBoard(template_hall_1, pos, rot);
        hasGeneratedAConnect = false;
        return true;
    }

    public boolean generateEndCorridorPlate(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_entrance_1", "c_hall_entrance_2", "c_hall_entrance_3"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        hasGeneratedAConnect = false;
        if(CORRIDOR_SIZE_LIMIT < 6 && !isSecondMainCorridor) {
            generateCorridorCross(template_hall_1, pos, rot);
        }

        int chamberVar = ModRand.range(1, 3);
        if(isSecondMainCorridor) {
            generateRegularChamber(template_hall_1, pos, rot,chamberVar);
        }
        return true;
    }
    //Second Plate End
    public boolean generateThirdBoard(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template = addAdjustedPieceWithoutCount(parent, pos.add(0, -1, 0), "c_board", rot);
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);

        CORRIDOR_SIZE_LIMIT++;
        if(template.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(template);
        components.add(template_hall_1);

        if(!hasGeneratedAConnect) {
            if(this.isRight && !this.isLeft) {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            } else if (this.isLeft && !this.isRight) {
                components.remove(template_hall_1);
                generateRightConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            //Fail Safe
            else {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            hasGeneratedAConnect = true;
        }

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
        hasGeneratedAConnect = false;
        return true;
    }

    public boolean generate3FillPlate3(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        String[] c_hall_types = {"c_hall_1", "c_hall_2", "c_hall_3", "c_hall_4", "c_hall_5", "c_hall_6", "c_hall_m_1"};
        TrialChambersTemplate template_hall_1 = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(c_hall_types), rot);
        components.add(template_hall_1);
        generateEndCorridorPlate(template_hall_1, pos, rot);

        if(!hasGeneratedAConnect) {
            if(this.isRight && !this.isLeft) {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            } else if (this.isLeft && !this.isRight) {
                components.remove(template_hall_1);
                generateRightConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            //Fail Safe
            else {
                components.remove(template_hall_1);
                generateLeftConnect(parent, pos, rot, ModRand.range(1, 4));
            }
            hasGeneratedAConnect = true;
        }

        return true;
    }
    //End Parts for 3rd Plate

    //handles generation of the Corridor 2 part and other chamber
    public boolean generateCorridorCross(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate template_cross = addAdjustedPiece(parent, pos, "c_cross_1", rot);
        components.add(template_cross);

        if(world.rand.nextInt(2) == 0) {
            generateSecondCorridor(template_cross, pos.add(18, 0, 0), rot.add(Rotation.CLOCKWISE_90));
        } else {
            generateSecondCorridor(template_cross, pos.add(0, 0, 19), rot.add(Rotation.COUNTERCLOCKWISE_90));
        }
        return true;
    }

    public boolean generateSecondCorridor(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate first_board = addAdjustedPieceWithoutCount(parent, pos.add(0, -8, 0), "c_board", rot);
        String[] b_entrance_types = {"e_hall_big_entrance_1", "e_hall_big_entrance_2", "e_hall_big_entrance_3"};
        TrialChambersTemplate bigEntryRoom = addAdjustedPieceWithoutCount(parent, pos.add(0, -7, 0), ModRand.choice(b_entrance_types), rot);
        isSecondMainCorridor = true;
        components.add(first_board);
        components.add(bigEntryRoom);
        generateSecondBoard(first_board, BlockPos.ORIGIN, rot);
        CORRIDOR_SIZE_LIMIT++;

    return true;
    }

    //Overrides the previous entry and places a entryway Left Side
    public boolean generateLeftConnect(TrialChambersTemplate parent, BlockPos pos, Rotation rot, int levelsUp) {
        isRight = false;
        if(levelsUp == 1) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cl_hall_connect_1", rot);
            components.add(first_floor);
            isLeft = true;
            int randomDeter = ModRand.range(1, 4);

            if(randomDeter == 1) {
                connectPieceOne(first_floor, pos.add(-7,2,4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            } else if (randomDeter == 2) {
                connectPieceTwo(first_floor, pos.add(-7, 2, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            } else if (randomDeter == 3) {
                connectPieceThree(first_floor, pos.add(-7, -5, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            }

        } if (levelsUp == 2) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cl_hall_connect_2", rot);
            components.add(first_floor);
            isLeft = true;

            int randomDeter = ModRand.range(1, 4);

            if(randomDeter == 1) {
                connectPieceOne(first_floor, pos.add(-7,7,4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            } else if (randomDeter == 2) {
                connectPieceTwo(first_floor, pos.add(-7, 7, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            } else if (randomDeter == 3) {
                connectPieceThree(first_floor, pos.add(-7, 0, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            }

        } else if(levelsUp == 3) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cl_hall_connect_3", rot);
            components.add(first_floor);
            isLeft = true;

            if(world.rand.nextInt(2)== 0) {
                connectPieceOne(first_floor, pos.add(-7,12,4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            } else {
                connectPieceThree(first_floor, pos.add(-7, 5, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
            }
        }
        return true;
    }


    //Overrides the previous entry and places a entryway Right Side
    public boolean generateRightConnect(TrialChambersTemplate parent, BlockPos pos, Rotation rot, int levelsUp) {
        isLeft = false;
        if(levelsUp == 1) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cr_hall_connect_1", rot);
            components.add(first_floor);
            isRight = true;

            int randomDeter = ModRand.range(1, 4);

            if(randomDeter == 1) {
                connectPieceOne(first_floor, pos.add(11,2,14), rot.add(Rotation.CLOCKWISE_90));
            } else if (randomDeter == 2) {
                connectPieceTwo(first_floor, pos.add(11, 2, 14), rot.add(Rotation.CLOCKWISE_90));
            } else if (randomDeter == 3) {
                connectPieceThree(first_floor, pos.add(11, -5, 14), rot.add(Rotation.CLOCKWISE_90));
            }
        } if (levelsUp == 2) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cr_hall_connect_2", rot);
            components.add(first_floor);
            isRight = true;

            int randomDeter = ModRand.range(1, 4);

            if(randomDeter == 1) {
                connectPieceOne(first_floor, pos.add(11,7,14), rot.add(Rotation.CLOCKWISE_90));
            } else if (randomDeter == 2) {
                connectPieceTwo(first_floor, pos.add(11, 7, 14), rot.add(Rotation.CLOCKWISE_90));
            } else if (randomDeter == 3) {
                connectPieceThree(first_floor, pos.add(11, 0, 14), rot.add(Rotation.CLOCKWISE_90));
            }
        } else if(levelsUp == 3) {
            TrialChambersTemplate first_floor = addAdjustedPieceWithoutCount(parent, pos, "connect/cr_hall_connect_3", rot);
            components.add(first_floor);
            isRight = true;

            if(world.rand.nextInt(2)==0) {
                connectPieceOne(first_floor, pos.add(11,12,14), rot.add(Rotation.CLOCKWISE_90));
            }  else {
                connectPieceThree(first_floor, pos.add(11, 5, 14), rot.add(Rotation.CLOCKWISE_90));
            }
           // connectPieceTwo(first_floor, pos.add(11, 12, 14), rot.add(Rotation.CLOCKWISE_90));
        }
        return true;
    }

    //Connect Piece 1
    public boolean connectPieceOne(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate connect_piece = addAdjustedPieceWithoutCount(parent, pos, "connect/connect_piece_1", rot);
        if(connect_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(connect_piece);
        int chamberVar = ModRand.range(1, 5);
        //generate Chamber
       if(!generateRegularChamber(connect_piece, BlockPos.ORIGIN, rot, chamberVar)) {
            if(!secondChanceToGenerateChamber(connect_piece, BlockPos.ORIGIN, rot)) {
                generateChamberEnd(connect_piece, BlockPos.ORIGIN, rot);
            }
        }

        return true;
    }

    //End Connect Piece 1

    public boolean connectPieceTwo(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate connect_piece = addAdjustedPieceWithoutCount(parent, pos, "connect/connect_piece_2", rot);
        if(connect_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(connect_piece);
        int chamberVar = ModRand.range(1, 5);
        //generate Chamber
        if(!generateRegularChamberForConnectTwo(connect_piece, BlockPos.ORIGIN, rot, chamberVar)) {
            if(!secondChanceToGenerateChamber(connect_piece, BlockPos.ORIGIN.add(0, 7, 0), rot)) {
                generateChamberEnd(connect_piece, BlockPos.ORIGIN.add(0, 7, 0), rot);
            }
        }
        return true;
    }

    public boolean connectPieceThree(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate connect_piece = addAdjustedPieceWithoutCount(parent, pos, "connect/connect_piece_3", rot);
        if(connect_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(connect_piece);
        int chamberVar = ModRand.range(1, 5);
        //generate Chamber
        if(!generateRegularChamber(connect_piece, BlockPos.ORIGIN, rot, chamberVar)) {
            if(!secondChanceToGenerateChamber(connect_piece, BlockPos.ORIGIN, rot)) {
                generateChamberEnd(connect_piece, BlockPos.ORIGIN, rot);
            }
        }

        return true;
    }

    //Generates a simple door at the end of the hall, I'll add more pieces that can generate later
    public boolean generateChamberEnd(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate end = addAdjustedPieceWithoutCount(parent, pos, "chamber/chamber_end", rot);
        components.add(end);
        return true;
    }

    //A second hall is added to try to generate the Chamber
    public boolean secondChanceToGenerateChamber(TrialChambersTemplate parent, BlockPos pos, Rotation rot) {
        TrialChambersTemplate chamer_connect = addAdjustedPieceWithoutCount(parent, pos, "chamber/chamber_connect", rot);
        if(chamer_connect.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(chamer_connect);
        int chamberVar = ModRand.range(1, 5);
        if(!generateRegularChamber(chamer_connect, BlockPos.ORIGIN, rot, chamberVar)) {
            generateChamberEnd(chamer_connect, BlockPos.ORIGIN, rot);
        }
        return true;
    }

    public boolean generateRegularChamberForConnectTwo(TrialChambersTemplate parent, BlockPos pos, Rotation rot, int id) {
        TrialChambersTemplate chamber = null;
        if(id == 1) {
            chamber = addAdjustedPiece(parent, pos.add(0,2,0), "chamber/chamber_1", rot);
        } else if(id == 2) {
            chamber = addAdjustedPiece(parent, pos.add(0, 7, 0), "chamber/chamber_2", rot);
        } else if(id ==3 ) {
            chamber = addAdjustedPiece(parent, pos.add(0, -2, -2), "chamber/chamber_3", rot);
        } else if(id == 4) {
            chamber = addAdjustedPiece(parent, pos.add(0, 7, 0), "chamber/chamber_4", rot);
        }

        if(chamber != null) {
            if(chamber.isCollidingExcParent(manager, parent, components)) {
                return false;
            }
            components.add(chamber);
        }

        return true;
    }

    //generates a regular chamber, more to come later
    public boolean generateRegularChamber(TrialChambersTemplate parent, BlockPos pos, Rotation rot, int id) {

        TrialChambersTemplate chamber = null;
        if(id == 1) {
            chamber = addAdjustedPiece(parent, pos.add(0, -5, 0), "chamber/chamber_1", rot);
        } else if (id == 2) {
            chamber = addAdjustedPiece(parent, pos, "chamber/chamber_2", rot);
       } else if(id == 3) {
            chamber = addAdjustedPiece(parent, pos.add(0, -9, -2), "chamber/chamber_3",rot);
        } else if(id == 4) {
            chamber = addAdjustedPiece(parent, pos, "chamber/chamber_4", rot);
        }



        if(chamber != null) {
            if (chamber.isCollidingExcParent(manager, parent, components)) {
                return false;
            }
            components.add(chamber);
        }


        return true;
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
