package com.dungeonderps.resourcefulbees.compat.jei;

import com.dungeonderps.resourcefulbees.ResourcefulBees;
import com.dungeonderps.resourcefulbees.config.BeeInfo;
import com.dungeonderps.resourcefulbees.data.BeeData;
import com.dungeonderps.resourcefulbees.lib.BeeConst;
import com.dungeonderps.resourcefulbees.registry.RegistryHandler;
import com.dungeonderps.resourcefulbees.utils.BeeInfoUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.CustomBeeEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@SuppressWarnings("NullableProblems")
public class BeeMutationCategory implements IRecipeCategory<BeeMutationCategory.Recipe> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/beemutation.png");
    public static final ResourceLocation ICONS = new ResourceLocation(ResourcefulBees.MOD_ID, "textures/gui/jei/icons.png");
    public static final ResourceLocation ID = new ResourceLocation(ResourcefulBees.MOD_ID, "mutation");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable info;
    private final IDrawable beeHive;
    private final String localizedName;
    private final CustomBeeEntity bee;

    public BeeMutationCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(GUI_BACK, -12, 0, 99, 75).addPadding(0, 0, 0, 0).build();
        this.icon = guiHelper.createDrawable(ICONS, 0,0,16,16);
        this.info = guiHelper.createDrawable(ICONS, 16, 0, 9, 9);
        this.beeHive = guiHelper.createDrawableIngredient(new ItemStack(RegistryHandler.IRON_BEEHIVE_ITEM.get()));
        this.localizedName = I18n.format("gui.resourcefulbees.jei.category.mutation");
        assert Minecraft.getInstance().world != null;
        bee = RegistryHandler.CUSTOM_BEE.get().create(Minecraft.getInstance().world);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends BeeMutationCategory.Recipe> getRecipeClass() {
        return BeeMutationCategory.Recipe.class;
    }

    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(BeeMutationCategory.Recipe recipe, IIngredients ingredients) {
        if (recipe.isBlockToBlock) {
            ingredients.setOutput(VanillaTypes.ITEM, recipe.mutationBlock);
            ingredients.setInput(VanillaTypes.ITEM, recipe.itemIn);
        }
        if (recipe.isFluidToBlock) {
            ingredients.setOutput(VanillaTypes.ITEM, recipe.mutationBlock);
            ingredients.setInput(VanillaTypes.FLUID, recipe.fluidIn);
        }
    }

    @Override
    public List<String> getTooltipStrings(Recipe recipe, double mouseX, double mouseY) {
        double infoX = 63D;
        double infoY = 8D;
        double beeX = 10D;
        double beeY = 6D;
        if (mouseX >= infoX && mouseX <= infoX + 9D && mouseY >= infoY && mouseY <= infoY + 9D){
            return Collections.singletonList(I18n.format("gui." + ResourcefulBees.MOD_ID + ".jei.category.mutation.info"));
        }
        if (mouseX >= beeX && mouseX <= beeX + 30D && mouseY >= beeY && mouseY <= beeY + 30D){
            return Collections.singletonList(I18n.format("entity." + ResourcefulBees.MOD_ID + "." + recipe.beeType + "_bee"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe,mouseX, mouseY);
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BeeMutationCategory.Recipe recipe, IIngredients ingredients) {
        if (recipe.isBlockToBlock) {
            IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
            itemStacks.init(0, false, 65, 48);
            itemStacks.init(1, true, 15, 57);
            itemStacks.set(ingredients);
        }

        if (recipe.isFluidToBlock) {
            IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
            itemStacks.init(0, false, 65, 48);
            //itemStacks.init(1, true, 15, 57);
            itemStacks.set(0, recipe.getMutationBlock());
            IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();
            fluidStacks.init(0,true,16,58);
            fluidStacks.set(0, recipe.getFluidIn());
        }
    }

    public void renderEntity(String beeType, Float rotation, Double xPos, Double yPos){
        RenderSystem.pushMatrix();

        RenderSystem.translatef(70, 24, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(30, 30, 30);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        matrixstack.rotate(quaternion);

        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager entityrenderermanager = mc.getRenderManager();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = mc.getRenderTypeBuffers().getBufferSource();

        assert mc.player != null;
        bee.ticksExisted = mc.player.ticksExisted;
        bee.renderYawOffset = rotation;
        bee.setBeeType(beeType);
        entityrenderermanager.renderEntityStatic(bee, xPos, yPos, 0.0D, mc.getRenderPartialTicks(), 1, matrixstack, irendertypebuffer$impl, 15728880);

        irendertypebuffer$impl.finish();

        RenderSystem.popMatrix();
    }

    @Override
    public void draw(BeeMutationCategory.Recipe recipe, double mouseX, double mouseY) {
        this.beeHive.draw(65, 10);
        this.info.draw(63, 8);
        renderEntity(recipe.beeType, 135.0F, 1.5D, -0.2D);
    }

    public static List<BeeMutationCategory.Recipe> getMutationRecipes(IIngredientManager ingredientManager) {
        List<BeeMutationCategory.Recipe> recipes = new ArrayList<>();
        for (Map.Entry<String, BeeData> bee : BeeInfo.BEE_INFO.entrySet()){
            if (bee.getValue().getBaseBlock().isEmpty() || bee.getValue().getMutationBlock().isEmpty()) { }
            else {
                String baseBlock = bee.getValue().getBaseBlock();
                if (BeeInfoUtils.TAG_RESOURCE_PATTERN.matcher(baseBlock).matches()){
                    ItemStack mutationBlock = new ItemStack(ForgeRegistries.ITEMS.getValue(BeeInfoUtils.getResource(bee.getValue().getMutationBlock())));
                    baseBlock = baseBlock.replace(BeeConst.TAG_PREFIX, "");
                    for (Item item:
                         ItemTags.getCollection().get(BeeInfoUtils.getResource(baseBlock)).getAllElements()) {
                        recipes.add(new Recipe(new ItemStack(item), mutationBlock, bee.getKey()));
                    }
                }
                else {
                    ItemStack mutationBlock = new ItemStack(ForgeRegistries.ITEMS.getValue(BeeInfoUtils.getResource(bee.getValue().getMutationBlock())));
                    if (ForgeRegistries.FLUIDS.getValue(BeeInfoUtils.getResource(baseBlock)) != null
                            && !Objects.equals(ForgeRegistries.FLUIDS.getValue(BeeInfoUtils.getResource(baseBlock)), Fluids.EMPTY)) {
                        FluidStack fluid = new FluidStack(ForgeRegistries.FLUIDS.getValue(BeeInfoUtils.getResource(baseBlock)), 1000);
                        recipes.add(new BeeMutationCategory.Recipe(fluid, mutationBlock, bee.getKey()));
                    }
                    else {

                        recipes.add(new Recipe(new ItemStack(ForgeRegistries.ITEMS.getValue(BeeInfoUtils.getResource(baseBlock))), mutationBlock, bee.getKey()));
                    }
                }
            }
        }
        return recipes;
    }

    public static class Recipe {
        private final FluidStack fluidIn;
        private final ItemStack itemIn;
        private final ItemStack mutationBlock;
        private final String beeType;

        private boolean isFluidToBlock = true;
        private boolean isBlockToBlock = true;

        public Recipe(ItemStack baseBlock, ItemStack mutationBlock, String beeType) {
            this.mutationBlock = mutationBlock;
            this.itemIn = baseBlock;
            this.beeType = beeType;
            this.fluidIn = null;
            isFluidToBlock = false;
        }

        public Recipe(FluidStack baseBlock, ItemStack mutationBlock, String beeType) {
            this.mutationBlock = mutationBlock;
            this.fluidIn = baseBlock;
            this.beeType = beeType;
            this.itemIn = null;
            isBlockToBlock = false;
        }

        public FluidStack getFluidIn() {return this.fluidIn;}
        //public ItemStack getItemIn() {return this.itemIn;}
        public ItemStack getMutationBlock() {
            return this.mutationBlock;
        }
        public String getBeeType() {
            return this.beeType;
        }
    }
}
