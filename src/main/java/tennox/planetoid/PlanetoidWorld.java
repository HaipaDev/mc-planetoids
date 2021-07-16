 package tennox.planetoid;

 //import net.minecraft.world.WorldServer;
 import net.minecraft.world.chunk.IChunkProvider;
 import net.minecraft.world.gen.IChunkGenerator;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiCreateWorld;
 import net.minecraft.world.World;
 import net.minecraft.world.WorldType;
 import net.minecraftforge.event.terraingen.WorldTypeEvent;
 import net.minecraft.world.chunk.Chunk;
 import net.minecraft.world.WorldProvider;

 public class PlanetoidWorld extends WorldType {
   public PlanetoidWorld(String name) {
    super(name);
   }

   /*public IChunkGenerator getChunkManager(World world) {
         return new PlanetoidChunkManager(world);
   }*/
    @Override
   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
/* 22 */     return new PlanetoidChunkManager(world, generatorOptions);//, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
   }

   public int getSpawnFuzz() {
/* 26 */     return 3;
   }

   public boolean isCustomizable() {
/* 30 */     return true;
   }


   @SideOnly(Side.CLIENT)
   public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
/* 36 */     mc.displayGuiScreen(new GuiCreatePlanetoidWorld(guiCreateWorld, guiCreateWorld.chunkProviderSettingsJson));
   }
 }