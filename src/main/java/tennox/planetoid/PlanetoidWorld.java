 package tennox.planetoid;
 
 import net.minecraftforge.fml.relauncher.Side;
 import net.minecraftforge.fml.relauncher.SideOnly;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.GuiCreateWorld;
 import net.minecraft.world.World;
 import net.minecraft.world.WorldServer;
 import net.minecraft.world.WorldType;
 import net.minecraft.world.biome.Biome;
 import net.minecraft.world.chunk.Chunk;
 import net.minecraft.world.chunk.IChunkProvider;
 
 public class PlanetoidWorld extends WorldType {
   public PlanetoidWorld(String name) {
/* 14 */     super(name);
   }
   
   public Chunk getChunkManager(WorldServer world) {
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


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\PlanetoidWorld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */