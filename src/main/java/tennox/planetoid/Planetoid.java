 package tennox.planetoid;


import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.BlockSand;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import org.apache.logging.log4j.Logger;
import tennox.planetoid.config.PlanetoidConfig;
 

 @Mod(modid = "planetoid", name = "Planetoid", version = "0.10.0")

 public class Planetoid
 {
   public static final int ADDITIONAL_SPAWN_TRIES = 0;
/* 22 */   public static WorldType planetoid = new PlanetoidWorld("planetoid");
   
   static Configuration config;
   public static Logger logger;
/* 26 */   int tick = 0;
   
   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
/* 30 */     logger = event.getModLog();
/* 31 */     MinecraftForge.EVENT_BUS.register(this);
/* 32 */     FMLCommonHandler.instance().bus().register(this);

			PlanetoidConfig.init(event.getModConfigurationDirectory().toString());
			FMLCommonHandler.instance().bus().register(new PlanetoidConfig());
   }
   
   @SubscribeEvent
   public void prePopulate(PopulateChunkEvent.Pre event) {
/* 37 */     BlockSand.fallInstantly = false;
   }

   
   public static String translate(String string) {
/* 49 */     return I18n.format(string);
   }
 }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\Planetoid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */