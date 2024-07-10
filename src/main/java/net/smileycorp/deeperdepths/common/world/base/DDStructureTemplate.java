package net.smileycorp.deeperdepths.common.world.base;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.smileycorp.deeperdepths.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class DDStructureTemplate extends StructureComponentTemplate {

    private static final PlacementSettings OVERWRITE = (new PlacementSettings()).setIgnoreEntities(true);

    private static final PlacementSettings INSERT = (new PlacementSettings()).setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);

    private String pieceName;

    private Rotation rotation;

    private int distance;

    private static int numTemplates = 0;

    private int templateId;

    //Boolean for if the template should overwrite existing blocks
    private boolean overwrite;

    public DDStructureTemplate() {

    }

    public DDStructureTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rotation, boolean overWriteIn) {
    super(0);
    this.pieceName = type;
    this.templatePosition = pos;
    this.rotation = rotation;
    templateId = numTemplates++;

    }


    public DDStructureTemplate(TemplateManager manager, String type, BlockPos pos, int distance, Rotation rotation, boolean overwriteIn) {
        this(manager, type, pos, rotation, overwriteIn);
        this.distance = distance;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getId() {
        return this.templateId;
    }

    public static void resetTemplateCount() {
        numTemplates = 0;
    }

    public PlacementSettings getPlacementSettings() {
        return this.placeSettings;
    }

    public BlockPos getTemplatePosition() {
        return this.templatePosition;
    }

    public Template getTemplate() {
        return this.template;
    }


    /**
     * Discover if bounding box can fit within the current bounding box object.
     * Excludes bounding boxes that touch each other and are not actually inside
     */
    public static StructureComponent findIntersectingExclusive(List<StructureComponent> listIn, StructureBoundingBox box) {
        for (StructureComponent structurecomponent : listIn) {
            if (structurecomponent.getBoundingBox() != null && structurecomponent.getBoundingBox().intersectsWith(box.minX + 1, box.minZ + 1, box.maxX - 1, box.maxZ - 1)) {
                return structurecomponent;
            }
        }

        return null;
    }


    private void loadTemplate(TemplateManager manager) {
        Template template = manager.getTemplate((MinecraftServer) null, new ResourceLocation(Constants.MODID, this.templateLocation() + "/" + this.pieceName));
        PlacementSettings placementsettings = (this.overwrite ? OVERWRITE : INSERT).copy().setRotation(this.rotation);
        this.setup(template, this.templatePosition, placementsettings);
    }

    public abstract String templateLocation();


    /**
     * (abstract) Helper method to write subclass data to NBT
     */
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setString("Template", this.pieceName);
        tagCompound.setString("Rot", this.rotation.name());
        tagCompound.setBoolean("OW", this.overwrite);
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        super.readStructureFromNBT(tagCompound, p_143011_2_);
        this.pieceName = tagCompound.getString("Template");
        this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
        this.overwrite = tagCompound.getBoolean("OW");
        this.loadTemplate(p_143011_2_);
    }

    /**
     * Determines if the new template is overlapping with another template,
     * excluding the parent
     */
    public boolean isCollidingExcParent(TemplateManager manager, DDStructureTemplate parent, List<StructureComponent> structures) {
        List<StructureComponent> collisions = findAllIntersecting(structures);

        boolean foundCollision = false;

        for (StructureComponent collision : collisions) {
            if (((DDStructureTemplate) collision).getId() != parent.getId()) {
                foundCollision = true;
            }
        }

        return foundCollision;
    }

    /**
     * Loads structure block data markers and handles them by their name
     */
    protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand, StructureBoundingBox sbb) {
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public List<StructureComponent> findAllIntersecting(List<StructureComponent> listIn) {
        List<StructureComponent> list = new ArrayList<StructureComponent>();
        for (StructureComponent structurecomponent : listIn) {
            StructureBoundingBox intersection = new StructureBoundingBox(this.boundingBox.minX + 1, this.boundingBox.minY + 1, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.maxY - 1, this.boundingBox.maxZ - 1);
            if (structurecomponent.getBoundingBox() != null && structurecomponent.getBoundingBox().intersectsWith(intersection)) {
                list.add(structurecomponent);
            }
        }

        return list;
    }
}
