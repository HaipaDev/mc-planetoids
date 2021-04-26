 package tennox.planetoid;

 import net.minecraft.world.WorldServer;
 import net.minecraft.world.chunk.IChunkProvider;
 import net.minecraft.world.gen.IChunkGenerator;
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiCreateWorld;
 import net.minecraft.world.World;
 import net.minecraft.world.WorldType;
 import net.minecraft.world.chunk.Chunk;

 public class PlanetoidWorld extends WorldType {
   public PlanetoidWorld(String name) {
/* 14 */     super(name);
   }

   public Chunk getChunkManager(World world) {
/* 18 */     return new PlanetoidChunkManager(world);
   }

   public IChunkProvider getChunkGenerator(WorldServer world, String generatorOptions) {
/* 22 */     return new PlanetoidChunkManager(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
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