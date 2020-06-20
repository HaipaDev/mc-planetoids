/*     */ package tennox.planetoid;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;

/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.world.ChunkPosition;
/*     */ import net.minecraft.world.SpawnerAnimals;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderGenerate;
/*     */ import net.minecraft.world.gen.MapGenBase;
/*     */ import net.minecraft.world.gen.MapGenCaves;
/*     */ import net.minecraft.world.gen.MapGenRavine;
/*     */ import net.minecraft.world.gen.structure.MapGenMineshaft;
/*     */ import net.minecraft.world.gen.structure.MapGenScatteredFeature;
/*     */ import net.minecraft.world.gen.structure.MapGenStronghold;
/*     */ import net.minecraft.world.gen.structure.MapGenVillage;
/*     */ import net.minecraftforge.event.terraingen.InitMapGenEvent;
/*     */ import net.minecraftforge.event.terraingen.TerrainGen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlanetoidChunkManager
/*     */   extends WorldChunkManager
/*     */   implements IChunkProvider
/*     */ {
/*  46 */   Random rand = new Random();
/*     */   long seed;
/*     */   World world;
/*     */   boolean mapFeaturesEnabled;
/*     */   private double[] noiseField;
/*  51 */   ArrayList<Planet> finished = new ArrayList<Planet>();
/*  52 */   ArrayList<Planet> unfinished = new ArrayList<Planet>();
/*  53 */   ArrayList<Point> pregen = new ArrayList<Point>();
/*  54 */   int pregenChunkSize = 4;
/*     */   
/*     */   PlanetoidGeneratorInfo generatorInfo;
/*     */   
/*     */   ChunkProviderGenerate defaultProvider;
/*  59 */   private MapGenBase caveGenerator = (MapGenBase)new MapGenCaves();
/*  60 */   private MapGenStronghold strongholdGenerator = new MapGenStronghold();
/*  61 */   private MapGenVillage villageGenerator = new MapGenVillage();
/*  62 */   private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
/*  63 */   private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
/*  64 */   private MapGenBase ravineGenerator = (MapGenBase)new MapGenRavine();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BiomeGenBase[] biomesForGeneration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlanetoidChunkManager(World w) {
/*  76 */     this(w, w.getSeed(), true, w.getWorldInfo().getGeneratorOptions());
/*     */   }
/*     */   
/*     */   public PlanetoidChunkManager(World w, long s, boolean mapfeatures, String generatorOptions) {
/*  80 */     super(w); this.caveGenerator = TerrainGen.getModdedMapGen(this.caveGenerator, InitMapGenEvent.EventType.CAVE); this.strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen((MapGenBase)this.strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD); this.villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen((MapGenBase)this.villageGenerator, InitMapGenEvent.EventType.VILLAGE); this.mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen((MapGenBase)this.mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT); this.scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen((MapGenBase)this.scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE); this.ravineGenerator = TerrainGen.getModdedMapGen(this.ravineGenerator, InitMapGenEvent.EventType.RAVINE);
/*  81 */     this.world = w;
/*  82 */     this.seed = s;
/*  83 */     this.mapFeaturesEnabled = mapfeatures;
/*  84 */     this.defaultProvider = new ChunkProviderGenerate(w, w.getSeed(), mapfeatures);
/*  85 */     this.generatorInfo = PlanetoidGeneratorInfo.createGeneratorFromString(generatorOptions);
/*  86 */     if (!generatorOptions.equals(""))
/*  87 */       Planetoid.logger.info("PlanetoidChunkManager initialized with these settings: \"" + this.generatorInfo.toString() + "\""); 
/*     */   }
/*     */   
/*     */   public boolean chunkExists(int i, int j) {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int par1, int par2) {
/*  96 */     TimeAnalyzer.start("provideChunk");
/*  97 */     this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
/*  98 */     preGenerate(par1, par2);
/*     */     
/* 100 */     Block[] ablock = new Block[65536];
/* 101 */     byte[] abyte = new byte[65536];
/* 102 */     this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, par1 * 16, par2 * 16, 16, 16);
/* 103 */     generatePlanetoid(par1, par2, ablock, abyte);
/*     */     
/* 105 */     TimeAnalyzer.start("provide_default");
/*     */     
/* 107 */     this.caveGenerator.func_151539_a(this, this.world, par1, par2, ablock);
/*     */ 
/*     */     
/* 110 */     if (this.mapFeaturesEnabled) {
/*     */       
/* 112 */       this.villageGenerator.func_151539_a(this, this.world, par1, par2, ablock);
/* 113 */       this.strongholdGenerator.func_151539_a(this, this.world, par1, par2, ablock);
/* 114 */       this.scatteredFeatureGenerator.func_151539_a(this, this.world, par1, par2, ablock);
/*     */     } 
/* 116 */     TimeAnalyzer.end("provide_default");
/*     */     
/* 118 */     Chunk chunk = new Chunk(this.world, ablock, abyte, par1, par2);
/* 119 */     byte[] abyte1 = chunk.getBiomeArray();
/*     */     
/* 121 */     for (int k = 0; k < abyte1.length; k++) {
/* 122 */       abyte1[k] = (byte)(this.biomesForGeneration[k]).biomeID;
/*     */     }
/*     */     
/* 125 */     chunk.generateSkylightMap();
/*     */ 
/*     */     
/* 128 */     TimeAnalyzer.end("provideChunk");
/* 129 */     return chunk;
/*     */   }
/*     */   
/*     */   public void preGenerate(int cx, int cz) {
/* 133 */     TimeAnalyzer.start("pregenerate");
/* 134 */     int x = round((cx / this.pregenChunkSize));
/* 135 */     int z = round((cz / this.pregenChunkSize));
/*     */     
/* 137 */     preGenerate2(x - 1, z - 1);
/* 138 */     preGenerate2(x - 1, z);
/* 139 */     preGenerate2(x - 1, z + 1);
/* 140 */     preGenerate2(x, z - 1);
/* 141 */     preGenerate2(x, z);
/* 142 */     preGenerate2(x, z + 1);
/* 143 */     preGenerate2(x + 1, z - 1);
/* 144 */     preGenerate2(x + 1, z);
/* 145 */     preGenerate2(x + 1, z + 1);
/*     */     
/* 147 */     TimeAnalyzer.end("pregenerate");
/*     */   }
/*     */   
/*     */   private void preGenerate2(int x, int z) {
/* 151 */     if (!this.pregen.contains(new Point(x, z))) {
/* 152 */       this.rand.setSeed(x * 341873128712L + z * 132897987541L);
/* 153 */       int x2 = x * this.pregenChunkSize * 16;
/* 154 */       int z2 = z * this.pregenChunkSize * 16;
/*     */       
/* 156 */       preGenerate_do(x2, z2, x2 + this.pregenChunkSize * 16, z2 + this.pregenChunkSize * 16);
/*     */       
/* 158 */       this.pregen.add(new Point(x, z));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void preGenerate_do(int x1, int z1, int x2, int z2) {
/* 163 */     TimeAnalyzer.start("pregenerate_do");
/*     */     
/* 165 */     for (int l = 0; l < Option.SPAWNTRIES.getValue(this.generatorInfo); l++) {
/* 166 */       double min = Option.MIN_RADIUS.getValue(this.generatorInfo);
/* 167 */       double max = Option.MAX_RADIUS.getValue(this.generatorInfo);
/* 168 */       double r = round(this.rand.nextDouble() * (max - min) + min);
/* 169 */       double x = (x1 + this.rand.nextInt(x2 - x1));
/* 170 */       double y = round(r + (256.0D - 2.0D * r) * this.rand.nextDouble());
/* 171 */       double z = (z1 + this.rand.nextInt(z2 - z1));
/*     */       
/* 173 */       Planet p = new Planet(this, this.world, x, y, z, round(r));
/*     */       
/* 175 */       if (p.isAreaClear()) {
/* 176 */         for (int i = round(x) - round(r); i <= round(x) + round(r); i++) {
/* 177 */           for (int k = round(z) - round(r); k <= round(z) + round(r); k++) {
/* 178 */             int cx = (int)Math.floor(i / 16.0D);
/* 179 */             int cz = (int)Math.floor(k / 16.0D);
/*     */             
/* 181 */             if (!p.unfinished.contains(new Point(cx, cz))) {
/* 182 */               p.unfinished.add(new Point(cx, cz));
/*     */             }
/*     */           } 
/*     */         } 
/* 186 */         this.unfinished.add(p);
/*     */       } 
/*     */     } 
/* 189 */     TimeAnalyzer.end("pregenerate_do");
/*     */   }
/*     */   
/*     */   public void generatePlanetoid(int chunkX, int chunkZ, Block[] ablock, byte[] ameta) {
/* 193 */     TimeAnalyzer.start("generate");
/*     */     
/* 195 */     TimeAnalyzer.start("finishPlanets");
/* 196 */     for (int i = 0; i < this.unfinished.size(); i++) {
/* 197 */       Planet p = this.unfinished.get(i);
/* 198 */       if (p.shouldFinishChunk(chunkX, chunkZ))
/* 199 */         p.generateChunk(chunkX, chunkZ, ablock, ameta); 
/* 200 */       if (p.isFinished()) {
/* 201 */         this.unfinished.remove(p);
/* 202 */         this.finished.add(p);
/*     */         
/* 204 */         i--;
/*     */       } 
/*     */     } 
/* 207 */     TimeAnalyzer.end("finishPlanets");
/*     */     
/* 209 */     TimeAnalyzer.start("generateWater");
/* 210 */     for (int x = 0; x < 16; x++) {
/* 211 */       for (int y = 0; y < 4; y++) {
/* 212 */         for (int z = 0; z < 16; z++) {
/* 213 */           if(this.generatorInfo.waterFloor)Planet.setBlock(x, y, z, (y == 0) ? Blocks.bedrock : Blocks.water, 0, ablock, ameta);
					else {Planet.setBlock(x, y, z, (y == -1) ? Blocks.bedrock : Blocks.air, 0, ablock, ameta);}
/*     */         }
/*     */       } 
/*     */     } 
/* 217 */     TimeAnalyzer.end("generateWater");
/*     */     
/* 219 */     TimeAnalyzer.end("generate");
/*     */   }
/*     */   
/*     */   public Chunk loadChunk(int par1, int par2) {
/* 223 */     return provideChunk(par1, par2);
/*     */   }
/*     */   
/*     */   public void populate(IChunkProvider ichunkprovider, int x, int z) {
/* 227 */     TimeAnalyzer.start("populate"); int i;
/* 228 */     for (i = 0; i < this.unfinished.size(); i++) {
/* 229 */       Planet p = this.unfinished.get(i);
/* 230 */       if (p.shouldDecorateChunk(x, z))
/* 231 */         p.decorateChunk(this.world, x, z); 
/*     */     } 
/* 233 */     for (i = 0; i < this.finished.size(); i++) {
/* 234 */       Planet p = this.finished.get(i);
/* 235 */       if (p.shouldDecorateChunk(x, z)) {
/* 236 */         p.decorateChunk(this.world, x, z);
/*     */       }
/*     */     } 
/* 239 */     TimeAnalyzer.start("populate2");
/* 240 */     if (this.generatorInfo.defaultGeneration) {
/* 241 */       this.defaultProvider.populate(ichunkprovider, x, z);
/*     */     }
/* 243 */     int k = x * 16;
/* 244 */     int l = z * 16;
/* 245 */     BiomeGenBase biomegenbase = this.world.getBiomeGenForCoords(k + 16, l + 16);
/* 246 */     for (int s = 0; s < 0; s++) {
/* 247 */       SpawnerAnimals.performWorldGenSpawning(this.world, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
/*     */     }
/* 249 */     TimeAnalyzer.end("populate2");
/*     */     
/* 251 */     Chunk c = this.world.getChunkFromChunkCoords(x, z);
/* 252 */     c.generateSkylightMap();
/*     */     
/* 254 */     c.enqueueRelightChecks();
/* 255 */     TimeAnalyzer.end("populate");
/*     */   }
/*     */   
/*     */   public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 263 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canSave() {
/* 267 */     return true;
/*     */   }
/*     */   
/*     */   public String makeString() {
/* 271 */     return "Planetoid";
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, int i, int j, int k) {
/* 275 */     ArrayList<BiomeGenBase.SpawnListEntry> list = new ArrayList<BiomeGenBase.SpawnListEntry>();
/*     */     
/* 277 */     if (type == EnumCreatureType.monster) {
/* 278 */       list.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 10, 4, 4));
/* 279 */       list.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 10, 4, 4));
/* 280 */       list.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
/* 281 */       list.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 10, 4, 4));
/* 282 */       list.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 10, 4, 4));
/* 283 */       list.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 1, 1, 4));
/* 284 */     } else if (type == EnumCreatureType.creature) {
/* 285 */       list.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 12, 4, 4));
/* 286 */     } else if (type == EnumCreatureType.waterCreature) {
/* 287 */       list.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 10, 4, 4));
/*     */     } 
/* 289 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkPosition func_147416_a(World world, String s, int i, int j, int k) {
/* 294 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 299 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateStructures(int i, int j) {}
/*     */ 
/*     */   
/*     */   public void cleanupCache() {}
/*     */ 
/*     */   
/*     */   public static int round(double d) {
/* 311 */     return (int)Math.round(d);
/*     */   }
/*     */   
/*     */   public void saveExtraData() {}
/*     */ }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\PlanetoidChunkManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */