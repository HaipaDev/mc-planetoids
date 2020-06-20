/*    */ package tennox.planetoid;
/*    */ import org.apache.logging.log4j.Logger;

/*    */ 
/*    */ import cpw.mods.fml.common.FMLCommonHandler;
/*    */ import cpw.mods.fml.common.Mod;
/*    */ import cpw.mods.fml.common.Mod.EventHandler;
/*    */ import cpw.mods.fml.common.event.FMLPreInitializationEvent;
/*    */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraft.block.BlockSand;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.world.WorldType;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.common.config.Configuration;
/*    */ import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import tennox.planetoid.config.PlanetoidConfig;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mod(modid = "TeNNoX_Planetoid", name = "Planetoid", version = "0.9.10")
/*    */ public class Planetoid
/*    */ {
/*    */   public static final int ADDITIONAL_SPAWN_TRIES = 0;
/* 22 */   public static WorldType planetoid = new PlanetoidWorld("planetoid");
/*    */   
/*    */   static Configuration config;
/*    */   public static Logger logger;
/* 26 */   int tick = 0;
/*    */   
/*    */   @EventHandler
/*    */   public void preInit(FMLPreInitializationEvent event) {
/* 30 */     logger = event.getModLog();
/* 31 */     MinecraftForge.EVENT_BUS.register(this);
/* 32 */     FMLCommonHandler.instance().bus().register(this);

			PlanetoidConfig.init(event.getModConfigurationDirectory().toString());
			FMLCommonHandler.instance().bus().register(new PlanetoidConfig());
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void prePopulate(PopulateChunkEvent.Pre event) {
/* 37 */     BlockSand.fallInstantly = false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String translate(String string) {
/* 49 */     return I18n.format(string, new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\Planetoid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */