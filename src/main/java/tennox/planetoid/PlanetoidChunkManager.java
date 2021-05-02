 package tennox.planetoid;

 import net.minecraft.block.Block;
 import net.minecraft.client.multiplayer.ChunkProviderClient;
 import net.minecraft.entity.EnumCreatureType;
 import net.minecraft.entity.monster.*;
 import net.minecraft.entity.passive.EntitySheep;
 import net.minecraft.entity.passive.EntitySquid;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.IProgressUpdate;
 //import net.minecraft.world.ChunkPosition;
 //import net.minecraft.world.SpawnerAnimals;
 import net.minecraft.world.WorldEntitySpawner;
 import net.minecraft.util.math.ChunkPos;
 import net.minecraft.world.World;
 import net.minecraft.world.WorldProvider;
 //import net.minecraft.world.biome.BiomeProvider;
 //import net.minecraft.world.biome.WorldChunkManager;
 import net.minecraft.world.biome.Biome;
 import net.minecraftforge.common.BiomeManager;
 import net.minecraftforge.common.BiomeManager.BiomeEntry;
 import net.minecraftforge.event.terraingen.WorldTypeEvent;
 import net.minecraft.world.biome.BiomeProvider;
 //import net.minecraft.world.WorldChunkManager;
 import net.minecraft.world.chunk.Chunk;
 import net.minecraft.world.chunk.ChunkPrimer;
 import net.minecraft.world.chunk.IChunkProvider;
 import net.minecraft.world.gen.IChunkGenerator;
 //import net.minecraft.world.gen.ChunkProviderGenerate;
 import net.minecraft.world.gen.MapGenBase;
 import net.minecraft.world.gen.MapGenCaves;
 import net.minecraft.world.gen.MapGenRavine;
 import net.minecraft.world.gen.structure.MapGenMineshaft;
 import net.minecraft.world.gen.structure.MapGenScatteredFeature;
 import net.minecraft.world.gen.structure.MapGenStronghold;
 import net.minecraft.world.gen.structure.MapGenVillage;
 import net.minecraftforge.event.terraingen.InitMapGenEvent;
 import net.minecraftforge.event.terraingen.TerrainGen;
 import net.minecraft.util.math.BlockPos;
 import org.jetbrains.annotations.NotNull;

 import javax.annotation.Nullable;
 import java.awt.*;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;




 public class PlanetoidChunkManager
         implements IChunkGenerator,IChunkProvider {
    Random rand = new Random();
   long seed;
   World world;
   boolean mapFeaturesEnabled;
   String generatorOptions;
/*  51 */   ArrayList<Planet> finished = new ArrayList<>();
/*  52 */   ArrayList<Planet> unfinished = new ArrayList<>();
/*  53 */   ArrayList<Point> pregen = new ArrayList<>();
/*  54 */   int pregenChunkSize = 4;
   
   PlanetoidGeneratorInfo generatorInfo;
   
   IChunkProvider defaultProvider;
/*  59 */   private MapGenBase caveGenerator = new MapGenCaves();
/*  60 */   private MapGenStronghold strongholdGenerator = new MapGenStronghold();
/*  61 */   private MapGenVillage villageGenerator = new MapGenVillage();
/*  62 */   private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
/*  63 */   private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
/*  64 */   private MapGenBase ravineGenerator = new MapGenRavine();




   private List<Biome> biomesForGeneration;


 public PlanetoidChunkManager(World w) {
     /*  76 */     this(w, w.getSeed(), true, w.getWorldInfo().getGeneratorOptions());
     /*     */   }
   public PlanetoidChunkManager(World w, long s, boolean mapfeatures, String generatorOptions) {
/*  80 */     this.caveGenerator = TerrainGen.getModdedMapGen(this.caveGenerator, InitMapGenEvent.EventType.CAVE); this.strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen((MapGenBase)this.strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD); this.villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen((MapGenBase)this.villageGenerator, InitMapGenEvent.EventType.VILLAGE); this.mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen((MapGenBase)this.mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT); this.scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen((MapGenBase)this.scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE); this.ravineGenerator = TerrainGen.getModdedMapGen(this.ravineGenerator, InitMapGenEvent.EventType.RAVINE);
/*  81 */     this.world = w;
/*  82 */     this.seed = s;
/*  83 */     this.mapFeaturesEnabled = true;
/*  84 */     this.defaultProvider = new ChunkProviderClient(w);
                this.generatorOptions=w.getWorldInfo().getGeneratorOptions();
/*  85 */     this.generatorInfo = PlanetoidGeneratorInfo.createGeneratorFromString(generatorOptions);
/*  86 */     if (!generatorOptions.equals(""))
/*  87 */       Planetoid.logger.info("PlanetoidChunkManager initialized with these settings: \"" + this.generatorInfo.toString() + "\""); 
   }

   public boolean chunkExists(int i, int j) {
/*  91 */     return true;
   }


	 @Nullable
	 //@Override
	 public Chunk getLoadedChunk(int i, int i1) {
		 return new Chunk(world,i,i1);
	 }

	 public Chunk provideChunk(int par1, int par2) {
/*  96 */     TimeAnalyzer.start("provideChunk");
/*  97 */     this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
/*  98 */     preGenerate(par1, par2);
     
/* 100 */     Block[] ablock = new Block[65536];
/* 101 */     byte[] abyte = new byte[65536];
                //this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeProvider[])null, par1 * 16, par2 * 16, 16, 16);
/* 102 */     this.biomesForGeneration = this.world.getBiomeProvider().getBiomesToSpawnIn();

/* 103 */     generateChunk(par1, par2, ablock, abyte);
     
/* 105 */     TimeAnalyzer.start("provide_default");
     
/* 107 */     this.caveGenerator.generate(this.world, par1, par2, new ChunkPrimer());
 
     
/* 110 */     if (this.mapFeaturesEnabled) {
       
/* 112 */       this.villageGenerator.generate(this.world, par1, par2, new ChunkPrimer());
/* 113 */       this.strongholdGenerator.generate(this.world, par1, par2, new ChunkPrimer());
/* 114 */       this.scatteredFeatureGenerator.generate(this.world, par1, par2, new ChunkPrimer());
     } 
/* 116 */     TimeAnalyzer.end("provide_default");
     
/* 118 */     Chunk chunk = new Chunk(this.world, par1, par2);
/* 119 */     byte[] abyte1 = chunk.getBiomeArray();
     
             for (int k = 0; k < abyte1.length; k++) {
            abyte1[k] = (byte)Biome.getIdForBiome(this.biomesForGeneration.get(k));
             }
     
/* 125 */     chunk.generateSkylightMap();
 
     
/* 128 */     TimeAnalyzer.end("provideChunk");
/* 129 */     return chunk;
   }

	 public void preGenerate(int cx, int cz) {
/* 133 */     TimeAnalyzer.start("pregenerate");
/* 134 */     int x = round((cx / this.pregenChunkSize));
/* 135 */     int z = round((cz / this.pregenChunkSize));
     
/* 137 */     preGenerate2(x - 1, z - 1);
/* 138 */     preGenerate2(x - 1, z);
/* 139 */     preGenerate2(x - 1, z + 1);
/* 140 */     preGenerate2(x, z - 1);
/* 141 */     preGenerate2(x, z);
/* 142 */     preGenerate2(x, z + 1);
/* 143 */     preGenerate2(x + 1, z - 1);
/* 144 */     preGenerate2(x + 1, z);
/* 145 */     preGenerate2(x + 1, z + 1);
     
/* 147 */     TimeAnalyzer.end("pregenerate");
   }
   
   private void preGenerate2(int x, int z) {
/* 151 */     if (!this.pregen.contains(new Point(x, z))) {
/* 152 */       this.rand.setSeed(x * 341873128712L + z * 132897987541L);
/* 153 */       int x2 = x * this.pregenChunkSize * 16;
/* 154 */       int z2 = z * this.pregenChunkSize * 16;
       
/* 156 */       preGenerate_do(x2, z2, x2 + this.pregenChunkSize * 16, z2 + this.pregenChunkSize * 16);
       
/* 158 */       this.pregen.add(new Point(x, z));
     } 
   }
   
   private void preGenerate_do(int x1, int z1, int x2, int z2) {
/* 163 */     TimeAnalyzer.start("pregenerate_do");
     
/* 165 */     for (int l = 0; l < Option.SPAWNTRIES.getValue(this.generatorInfo); l++) {
/* 166 */       double min = Option.MIN_RADIUS.getValue(this.generatorInfo);
/* 167 */       double max = Option.MAX_RADIUS.getValue(this.generatorInfo);
/* 168 */       double r = round(this.rand.nextDouble() * (max - min) + min);
/* 169 */       double x = (x1 + this.rand.nextInt(x2 - x1));
/* 170 */       double y = round(r + (256.0D - 2.0D * r) * this.rand.nextDouble());
/* 171 */       double z = (z1 + this.rand.nextInt(z2 - z1));
       
/* 173 */       Planet p = new Planet(this, this.world, x, y, z, round(r));
       
/* 175 */       if (p.isAreaClear()) {
/* 176 */         for (int i = round(x) - round(r); i <= round(x) + round(r); i++) {
/* 177 */           for (int k = round(z) - round(r); k <= round(z) + round(r); k++) {
/* 178 */             int cx = (int)Math.floor(i / 16.0D);
/* 179 */             int cz = (int)Math.floor(k / 16.0D);
             
/* 181 */             if (!p.unfinished.contains(new Point(cx, cz))) {
/* 182 */               p.unfinished.add(new Point(cx, cz));
             }
           } 
         } 
/* 186 */         this.unfinished.add(p);
       } 
     } 
/* 189 */     TimeAnalyzer.end("pregenerate_do");
   }

     public Chunk generateChunk(int x,int z){return getLoadedChunk(x,z);}
     public Chunk generateChunk(int chunkX,int chunkZ, Block[] ablock, byte[] ameta){
/* 193 */     TimeAnalyzer.start("generate");
     
/* 195 */     TimeAnalyzer.start("finishPlanets");
/* 196 */     for (int i = 0; i < this.unfinished.size(); i++) {
/* 197 */       Planet p = this.unfinished.get(i);
/* 198 */       if (p.shouldFinishChunk(chunkX, chunkZ))
/* 199 */         p.generateChunk(chunkX, chunkZ, ablock, ameta);
/* 200 */       if (p.isFinished()) {
/* 201 */         this.unfinished.remove(p);
/* 202 */         this.finished.add(p);
         
/* 204 */         i--;
       } 
     } 
/* 207 */     TimeAnalyzer.end("finishPlanets");
     
/* 209 */     TimeAnalyzer.start("generateWater");
/* 210 */     for (int x = 0; x < 16; x++) {
/* 211 */       for (int y = 0; y < 4; y++) {
/* 212 */         for (int z = 0; z < 16; z++) {
/* 213 */           if(this.generatorInfo.waterFloor)Planet.setBlock(x, y, z, (y == 0) ? Blocks.BEDROCK : Blocks.WATER, 0, ablock, ameta);
					else {Planet.setBlock(x, y, z, (y == -1) ? Blocks.BEDROCK : Blocks.AIR, 0, ablock, ameta);}
            }
            }
            }
/* 217 */     TimeAnalyzer.end("generateWater");
     
/* 219 */     TimeAnalyzer.end("generate");

         return getLoadedChunk(chunkX,chunkZ);
   }
   
   public void populate(int x, int z) {
/* 227 */     TimeAnalyzer.start("populate"); int i;
/* 228 */     for (i = 0; i < this.unfinished.size(); i++) {
/* 229 */       Planet p = this.unfinished.get(i);
/* 230 */       if (p.shouldDecorateChunk(x, z))
/* 231 */         p.decorateChunk(this.world, x, z); 
     } 
/* 233 */     for (i = 0; i < this.finished.size(); i++) {
/* 234 */       Planet p = this.finished.get(i);
/* 235 */       if (p.shouldDecorateChunk(x, z)) {
/* 236 */         p.decorateChunk(this.world, x, z);
       }
     } 
/* 239 */     TimeAnalyzer.start("populate2");
/* 240 */     if (this.generatorInfo.defaultGeneration) {
/* 241 */       this.defaultProvider.provideChunk(x, z);
     }
/* 243 */     int k = x * 16;
/* 244 */     int l = z * 16;
/* 245 */     Biome biome = this.world.getBiomeForCoordsBody(new BlockPos(k + 16, 0, l + 16));
/* 246 */     for (int s = 0; s < 0; s++) {
/* 247 */       WorldEntitySpawner.performWorldGenSpawning(this.world, biome, k + 8, l + 8, 16, 16, this.rand);
     }
/* 249 */     TimeAnalyzer.end("populate2");
     
/* 251 */     Chunk c = this.world.getChunkFromChunkCoords(x, z);
/* 252 */     c.generateSkylightMap();
     
/* 254 */     c.enqueueRelightChecks();
/* 255 */     TimeAnalyzer.end("populate");
   }

	 public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos blockPos) {
/* 275 */     ArrayList<Biome.SpawnListEntry> list = new ArrayList<Biome.SpawnListEntry>();
     
/* 277 */     if (type == EnumCreatureType.MONSTER) {
/* 278 */       list.add(new Biome.SpawnListEntry(EntitySpider.class, 10, 4, 4));
/* 279 */       list.add(new Biome.SpawnListEntry(EntityZombie.class, 10, 4, 4));
/* 280 */       list.add(new Biome.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
/* 281 */       list.add(new Biome.SpawnListEntry(EntityCreeper.class, 10, 4, 4));
/* 282 */       list.add(new Biome.SpawnListEntry(EntitySlime.class, 10, 4, 4));
/* 283 */       list.add(new Biome.SpawnListEntry(EntityEnderman.class, 1, 1, 4));
/* 284 */     } else if (type == EnumCreatureType.CREATURE) {
/* 285 */       list.add(new Biome.SpawnListEntry(EntitySheep.class, 12, 4, 4));
/* 286 */     } else if (type == EnumCreatureType.WATER_CREATURE) {
/* 287 */       list.add(new Biome. SpawnListEntry(EntitySquid.class, 10, 4, 4));
     } 
/* 289 */     return list;
   }

     @org.jetbrains.annotations.Nullable
     @Override
     public BlockPos getNearestStructurePos(World world, String s, BlockPos blockPos, boolean b) {
         return null;
     }

    public boolean isInsideStructure(World w, String structureName, BlockPos blockPos){return false;}
    public boolean generateStructures(Chunk c, int i, int j){return false;}
    public void recreateStructures(Chunk c,int i, int j){}

     public boolean tick(){return false;}
     public String makeString(){return "Planetoid";}
     public boolean isChunkGeneratedAt(int x,int z){return true;}

   public static int round(double d) {
/* 311 */     return (int)Math.round(d);
   }
   
   public void saveExtraData() {}
     void getChunkGenerator(World world, String generatorOptions){}
 }