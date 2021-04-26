 package tennox.planetoid;

 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraft.client.gui.GuiButton;
 import net.minecraft.client.gui.GuiCreateWorld;
 import net.minecraft.client.gui.GuiScreen;
 import net.minecraft.client.resources.I18n;

 @SideOnly(Side.CLIENT)
 public class GuiCreatePlanetoidWorld
       extends GuiScreen {
//private GuiSlider slider1;
   private final GuiCreateWorld createWorldGui;
/* 14 */   PlanetoidGeneratorInfo generatorInfo = PlanetoidGeneratorInfo.getDefaultGenerator();


   public GuiCreatePlanetoidWorld(GuiCreateWorld guiCreateWorld, String str) {
    /* 18 */     this.createWorldGui = guiCreateWorld;
    /* 19 */     this.generatorInfo = PlanetoidGeneratorInfo.createGeneratorFromString(str);
       }





   public void initGui() {
    /* 27 */     this.buttonList.clear();
    /* 28 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, Planetoid.translate("gui.done")));
    /* 29 */     this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, Planetoid.translate("gui.cancel")));
    /* 30 */     this.buttonList.add(new GuiButton(2, this.width / 2 - 155, 52, 150, 20, Planetoid.translate("planetoid.gui.defaultgeneration") + ": " + (this.generatorInfo.defaultGeneration ? "on" : "off")));
    this.buttonList.add(new GuiButton(6, this.width / 2 + 5, 52, 150, 20, Planetoid.translate("planetoid.gui.waterfloor") + ": " + (this.generatorInfo.waterFloor ? "on" : "off")));

    /* 32 */     //slider1 = new GuiSlider(3, this.width / 2 - 155, 74, "Min. Radius","",0,1,Option.MIN_RADIUS,false,true);
    this.buttonList.add(new PlanetoidSlider(3, this, this.width / 2 - 155, 74, Option.MIN_RADIUS));
    /* 33 */     this.buttonList.add(new PlanetoidSlider(4, this, this.width / 2 - 155, 98, Option.MAX_RADIUS));
    /* 34 */     this.buttonList.add(new PlanetoidSlider(5, this, this.width / 2 + 5, 74, Option.SPAWNTRIES));
       }

   protected void actionPerformed(GuiButton button) {
    /* 38 */     if (button.id == 1) {
        /* 39 */       this.mc.displayGuiScreen((GuiScreen)this.createWorldGui);
        /* 40 */     } else if (button.id == 0) {
        /* 41 */       this.createWorldGui.chunkProviderSettingsJson = this.generatorInfo.toString();
        /* 42 */       this.mc.displayGuiScreen((GuiScreen)this.createWorldGui);
        /* 43 */     } else if (button.id == 2) {
        /* 44 */       this.generatorInfo.defaultGeneration = !this.generatorInfo.defaultGeneration;
        /* 45 */       button.displayString = I18n.format("planetoid.gui.defaultgeneration", new Object[0]) + ": " + (this.generatorInfo.defaultGeneration ? "on" : "off");
             } else if (button.id == 6) {
        /* 44 */       this.generatorInfo.waterFloor = !this.generatorInfo.waterFloor;
        /* 45 */       button.displayString = I18n.format("planetoid.gui.waterfloor", new Object[0]) + ": " + (this.generatorInfo.waterFloor ? "on" : "off");
             }
       }





   public void drawScreen(int par1, int par2, float par3) {
    /* 54 */     drawDefaultBackground();
    /* 55 */     drawCenteredString(this.fontRenderer, "Planetoid World", this.width / 2, 8, 16777215);
    /* 56 */     super.drawScreen(par1, par2, par3);
       }
 }