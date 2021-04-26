package tennox.planetoid.config;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class PlanetoidConfig {
	public static Configuration config;
	
	public static final String CATEGORY_NAME_PLANETS="Planet Chances";
	public static final String CATEGORY_NAME_STONEPLANETS="Stone Planet Chances";

	public static int dirt;
	public static int wood;
	public static int water;
	public static int sand;
	public static int glowstone;
	public static int stone;

	public static int gravel;
	public static int cobblestone;
	public static int lava;
	public static int coal;
	public static int iron;
	public static int gold;
	public static int redstone;
	public static int lapisLazuli;
	public static int tnt;
	public static int diamond;
	public static int emerald;

	public static void init(String cfgDir) {
		if(config==null) {
			File path = new File(cfgDir+"/Planetoid.cfg");

			config=new Configuration(path);
			loadConfig();
		}
	}

	private static void loadConfig() {
		dirt=config.getInt("dirt", CATEGORY_NAME_PLANETS, 20,0,100,"");
		wood=config.getInt("wood", CATEGORY_NAME_PLANETS, 10,0,100,"");
		water=config.getInt("water", CATEGORY_NAME_PLANETS, 5,0,100,"");
		sand=config.getInt("sand", CATEGORY_NAME_PLANETS, 10,0,100,"");
		glowstone=config.getInt("glowstone", CATEGORY_NAME_PLANETS, 3,0,100,"");
		stone=config.getInt("stone", CATEGORY_NAME_PLANETS, 20,0,100,"");

		gravel=config.getInt("gravel", CATEGORY_NAME_STONEPLANETS, 40,0,100,"");
		cobblestone=config.getInt("cobblestone", CATEGORY_NAME_STONEPLANETS, 60,0,100,"");
		lava=config.getInt("lava", CATEGORY_NAME_STONEPLANETS, 60,0,100,"");
		coal=config.getInt("coal", CATEGORY_NAME_STONEPLANETS, 60,0,100,"");
		iron=config.getInt("iron", CATEGORY_NAME_STONEPLANETS, 60,0,100,"");
		gold=config.getInt("gold", CATEGORY_NAME_STONEPLANETS, 30,0,100,"");
		redstone=config.getInt("redstone", CATEGORY_NAME_STONEPLANETS, 30,0,100,"");
		lapisLazuli=config.getInt("lapisLazuli", CATEGORY_NAME_STONEPLANETS, 15,0,100,"");
		tnt=config.getInt("tnt", CATEGORY_NAME_STONEPLANETS, 2,0,100,"");
		diamond=config.getInt("diamond", CATEGORY_NAME_STONEPLANETS, 2,0,100,"");
		emerald=config.getInt("emerald", CATEGORY_NAME_STONEPLANETS, 1,0,100,"");

		if(config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public void onCongfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equalsIgnoreCase("TeNNoX_Planetoid")) {
			loadConfig();
		}
	}
	public static Configuration getConfig() {return config;}
	/*public static int dirtChance;

	public void initConfig(FMLInitializationEvent event) {
		Configuration config = new Configuration(new File("config/Planetoid.cfg"));
		config.load();

		dirtChance=config.get("PlanetChances", "dirt", 20).getInt();

		config.save();
	}
	@EventHandler
	public void load(FMLInitializationEvent event) {
		this.initConfig(event);
	}*/
	/*private static Configuration config=null;
	public static final String CATEGORY_NAME_PLANETS="planets";

	public static int dirtPlanet;

	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "Planetoid.cfg");
		config=new Configuration(configFile);
	}
	public static Configuration getConfig() {
		return config;
	}
	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}
	public static void syncFromFiles() {
		syncConfig(true,true);
	}
	public static void syncFromGui() {
		syncConfig(false,true);
	}
	public static void syncFromFields() {
		syncConfig(false,false);
	}
	private static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {
		if(loadFromConfigFile)config.load();

		Property propDirt=config.get(CATEGORY_NAME_PLANETS, "dirt", 20);
		propDirt.setMinValue(0);

		ArrayList<String> propertySpawnPlanets=new ArrayList<String>();
		propertySpawnPlanets.add(propDirt.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_PLANETS, propertySpawnPlanets);

		if(readFieldsFromConfig) {
			dirtPlanet=propDirt.getInt();
		}

		propDirt.set(dirtPlanet);
		
		if(config.hasChanged()) {
			config.save();
		}
	}
	public static class ConfigEventHandler{
		@SubscribeEvent(priority=EventPriority.LOWEST)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			//if(event.getModID().equals(Reference.MODID)) {
			//if(event.modID().equals(Reference.ModID)) {
				syncFromGui();
			//}
		}
	}*/
}
