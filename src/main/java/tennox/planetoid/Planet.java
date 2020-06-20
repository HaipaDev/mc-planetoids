/*     */ package tennox.planetoid;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;

/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.world.World;
import tennox.planetoid.config.PlanetoidConfig;
/*     */ 
/*     */ public class Planet
/*     */ {
/*  13 */   static PlanetType DIRT = (new PlanetType(Blocks.dirt, PlanetoidConfig.dirt, "Dirt")).setTopBlock((Block)Blocks.grass);
/*  14 */   static PlanetType WOOD = new PlanetType((Block)Blocks.leaves, Blocks.log, PlanetoidConfig.wood, "Wood");
/*  15 */   static PlanetType WATER = new PlanetType(Blocks.glass, Blocks.water, PlanetoidConfig.water, "Water");
/*  16 */   static PlanetType SAND = (new PlanetType(Blocks.sand, PlanetoidConfig.sand, "Sand")).setBottomBlock(Blocks.sandstone);
/*  17 */   static PlanetType GLOWSTONE = new PlanetType(Blocks.glowstone, PlanetoidConfig.glowstone, "Glowstone");
/*  18 */   static PlanetType STONE = new PlanetType(Blocks.stone, PlanetoidConfig.stone, "Stone");
/*     */   
/*  20 */   static PlanetType GRAVEL = new PlanetType(Blocks.stone, Blocks.gravel, PlanetoidConfig.gravel, "Gravel");
/*  21 */   static PlanetType COBBLESTONE = new PlanetType(Blocks.stone, Blocks.cobblestone, PlanetoidConfig.cobblestone, "Cobblestone");
/*  22 */   static PlanetType LAVA = new PlanetType(Blocks.stone, Blocks.lava, PlanetoidConfig.lava, "Lava");
/*  23 */   static PlanetType COAL = new PlanetType(Blocks.stone, Blocks.coal_ore, PlanetoidConfig.coal, "Coal");
/*  24 */   static PlanetType IRON = new PlanetType(Blocks.stone, Blocks.iron_ore, PlanetoidConfig.iron, "Iron");
/*  25 */   static PlanetType GOLD = new PlanetType(Blocks.stone, Blocks.gold_ore, PlanetoidConfig.gold, "Gold");
/*  26 */   static PlanetType REDSTONE = new PlanetType(Blocks.stone, Blocks.redstone_ore, PlanetoidConfig.redstone, "Redstone");
/*  27 */   static PlanetType LAPISLAZULI = new PlanetType(Blocks.stone, Blocks.lapis_ore, PlanetoidConfig.lapisLazuli, "Lapislazuli");
/*  28 */   static PlanetType TNT = new PlanetType(Blocks.stone, Blocks.tnt, PlanetoidConfig.tnt, "TNT");
/*  29 */   static PlanetType DIAMOND = new PlanetType(Blocks.stone, Blocks.diamond_ore, PlanetoidConfig.diamond, "Diamond");
/*  30 */   static PlanetType EMERALD = new PlanetType(Blocks.stone, Blocks.emerald_ore, PlanetoidConfig.emerald, "Emerald");
/*     */   
/*  32 */   static ArrayList<PlanetType> stonetypes = new ArrayList<PlanetType>();
/*  33 */   static ArrayList<PlanetType> types = initTypes();
/*     */   
/*  35 */   Random rand = new Random();
/*     */   PlanetoidChunkManager chunkManager;
/*     */   World world2;
/*     */   int x;
/*     */   int y;
/*     */   int z;
/*     */   int radius;
/*     */   PlanetType type;
/*  43 */   ArrayList<Point> unfinished = new ArrayList<Point>();
/*  44 */   ArrayList<Point> finished = new ArrayList<Point>();
/*     */   
/*     */   public Planet(PlanetoidChunkManager cm, World w, int x, int y, int z, int r) {
/*  47 */     this.chunkManager = cm;
/*  48 */     this.world2 = w;
/*  49 */     this.x = x;
/*  50 */     this.y = y;
/*  51 */     this.z = z;
/*  52 */     this.radius = r;
/*     */     
/*  54 */     this.type = getRandomPlanet();
/*     */   }
/*     */   
/*     */   private static ArrayList<PlanetType> initTypes() {
/*  58 */     ArrayList<PlanetType> list = new ArrayList<PlanetType>();
/*  59 */     list.add(DIRT);
/*  60 */     list.add(WOOD);
/*  61 */     list.add(WATER);
/*  62 */     list.add(SAND);
/*  63 */     list.add(GLOWSTONE);
/*  64 */     list.add(STONE);
/*  65 */     stonetypes.add(GRAVEL);
/*  66 */     stonetypes.add(COBBLESTONE);
/*  67 */     stonetypes.add(LAVA);
/*  68 */     stonetypes.add(COAL);
/*  69 */     stonetypes.add(IRON);
/*  70 */     stonetypes.add(GOLD);
/*  71 */     stonetypes.add(REDSTONE);
/*  72 */     stonetypes.add(LAPISLAZULI);
/*  73 */     stonetypes.add(TNT);
/*  74 */     stonetypes.add(DIAMOND);
/*  75 */     stonetypes.add(EMERALD);
/*  76 */     return list;
/*     */   }
/*     */   
/*     */   public Planet(PlanetoidChunkManager cm, World w, double x, double y, double z, double r) {
/*  80 */     this(cm, w, round(x), round(y), round(z), round(r));
/*     */   }
/*     */   
/*     */   public static void print() {
/*  84 */     System.out.println("---PREGENERATION: ---");
/*  85 */     for (PlanetType p : types) {
/*  86 */       System.out.println(p.name + ":\t" + p.total);
/*     */     }
/*     */     
/*  89 */     for (PlanetType p : stonetypes) {
/*  90 */       System.out.println("-" + p.name + ":\t" + p.total);
/*     */     }
/*  92 */     System.out.println("---PREGENERATION END---");
/*     */   }
/*     */   
/*     */   public PlanetType getRandomPlanet() {
/*  96 */     this.rand.setSeed(this.x * 341873128712L + this.z * 132897987541L);
/*     */     
/*  98 */     ArrayList<PlanetType> list = new ArrayList<PlanetType>();
/*     */     
/* 100 */     list.addAll(types);
/*     */     
/* 102 */     PlanetType type = (PlanetType)WeightedRandom.getRandomItem(this.rand, list);
/*     */     
/* 104 */     if (type == STONE) {
/* 105 */       list.clear();
/* 106 */       list.addAll(stonetypes);
/*     */       
/* 108 */       type = (PlanetType)WeightedRandom.getRandomItem(this.rand, list);
/*     */     } 
/*     */     
/* 111 */     type.total++;
/* 112 */     if (type.out == Blocks.stone)
/* 113 */       STONE.total++; 
/* 114 */     return type;
/*     */   }
/*     */   
/*     */   public void generateChunk(int chunkX, int chunkZ, Block[] ablock, byte[] ameta) {
/* 118 */     this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
/* 119 */     TimeAnalyzer.start("generateChunk");
/* 120 */     for (int x2 = Math.max(chunkX * 16, this.x - this.radius); x2 <= Math.min(chunkX * 16 + 15, this.x + this.radius); x2++) {
/* 121 */       for (int y2 = this.y - this.radius; y2 <= this.y + this.radius; y2++) {
/* 122 */         for (int z2 = Math.max(chunkZ * 16, this.z - this.radius); z2 <= Math.min(chunkZ * 16 + 15, this.z + this.radius); z2++) {
/* 123 */           int d = round(distance(this.x, this.y, this.z, x2, y2, z2));
/* 124 */           if (d == this.radius) {
/* 125 */             if (isBottomBlock(x2, y2, z2))
/* 126 */             { setBlock(x2, y2, z2, this.type.getBottomBlock(), 0, ablock, ameta); }
/* 127 */             else if (isTopBlock(x2, y2, z2))
/* 128 */             { setBlock(x2, y2, z2, this.type.getTopBlock(), 0, ablock, ameta); }
/*     */             else
/* 130 */             { setBlock(x2, y2, z2, this.type.out, 0, ablock, ameta); } 
/* 131 */           } else if (d < this.radius) {
/* 132 */             setBlock(x2, y2, z2, this.type.in, 0, ablock, ameta);
/*     */           } 
/* 134 */           generateSpecial(x2, y2, z2, ablock, ameta);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     if (!this.finished.contains(new Point(chunkX, chunkZ)))
/* 140 */       this.finished.add(new Point(chunkX, chunkZ)); 
/* 141 */     if (this.unfinished.contains(new Point(chunkX, chunkZ)))
/* 142 */       this.unfinished.remove(new Point(chunkX, chunkZ)); 
/* 143 */     TimeAnalyzer.end("generateChunk");
/*     */   }
/*     */   
/*     */   public void decorateChunk(World world, int chunkX, int chunkZ) {
/* 147 */     this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
/* 148 */     TimeAnalyzer.start("decorateChunk");
/* 149 */     for (int x2 = Math.max(chunkX * 16, this.x - this.radius); x2 <= Math.min(chunkX * 16 + 15, this.x + this.radius); x2++) {
/* 150 */       for (int y2 = this.y - this.radius; y2 <= this.y + this.radius; y2++) {
/* 151 */         for (int z2 = Math.max(chunkZ * 16, this.z - this.radius); z2 <= Math.min(chunkZ * 16 + 15, this.z + this.radius); z2++) {
/* 152 */           if (isTopBlock(x2, y2, z2)) {
/* 153 */             if (this.type == SAND && this.rand.nextDouble() <= 0.05D && Blocks.cactus.canPlaceBlockAt(world, x2, y2 + 1, z2)) {
/* 154 */               for (int i = 1; i <= 1 + this.rand.nextInt(3); i++) {
/* 155 */                 world.setBlock(x2, y2 + i, z2, Blocks.cactus);
/*     */               }
/* 157 */             } else if (this.type == DIRT) {
/* 158 */               if (this.rand.nextDouble() <= 0.1D && Blocks.tallgrass.canPlaceBlockAt(world, x2, y2 + 1, z2)) {
/* 159 */                 world.setBlock(x2, y2 + 1, z2, (Block)Blocks.tallgrass, 1, 3);
/* 160 */               } else if (this.rand.nextDouble() <= 0.004D) {
/* 161 */                 boolean flag1 = (world.getBlock(x2 + 1, y2, z2) == Blocks.grass && world.getBlock(x2 - 1, y2, z2) == Blocks.grass && world.getBlock(x2, y2, z2 + 1) == Blocks.grass && world.getBlock(x2, y2, z2 - 1) == Blocks.grass);
/*     */                 
/* 163 */                 boolean flag2 = (world.getBlock(x2 + 1, y2 + 1, z2) == Blocks.air && world.getBlock(x2 - 1, y2 + 1, z2) == Blocks.air && world.getBlock(x2, y2 + 1, z2 + 1) == Blocks.air && world.getBlock(x2, y2 + 1, z2 - 1) == Blocks.air);
/*     */ 
/*     */                 
/* 166 */                 if (flag1 && flag2) {
/* 167 */                   world.setBlock(x2, y2, z2, Blocks.water); int i;
/* 168 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 169 */                     world.setBlock(x2 + 1, y2 + i, z2, Blocks.reeds); 
/* 170 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 171 */                     world.setBlock(x2 - 1, y2 + i, z2, Blocks.reeds); 
/* 172 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 173 */                     world.setBlock(x2, y2 + i, z2 + 1, Blocks.reeds); 
/* 174 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 175 */                     world.setBlock(x2, y2 + i, z2 - 1, Blocks.reeds); 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 179 */           } else if (isBottomBlock(x2, y2, z2) && 
/* 180 */             this.type == WATER) {
/* 181 */             if (x2 == this.x && z2 == this.z) {
/* 182 */               world.setBlockToAir(x2, y2, z2);
/*     */             }
/* 184 */             else if (world.getBlock(x2, y2 + 1, z2) == Blocks.water || world.getBlock(x2, y2 + 1, z2) == Blocks.flowing_water) {
/* 185 */               for (int i = 1; i <= 1 + this.rand.nextInt(2); i++)
/* 186 */                 world.setBlock(x2, y2 + i, z2, Blocks.clay); 
/* 187 */             } else if (world.getBlock(x2, y2 + 2, z2) == Blocks.water || world.getBlock(x2, y2 + 2, z2) == Blocks.flowing_water) {
/* 188 */               for (int i = 2; i <= 2 + this.rand.nextInt(2); i++) {
/* 189 */                 world.setBlock(x2, y2 + i, z2, Blocks.clay);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     TimeAnalyzer.end("decorateChunk");
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateSpecial(int x, int y, int z, Block[] ablock, byte[] data) {}
/*     */   
/*     */   private boolean isTopBlock(int x2, int y2, int z2) {
/* 204 */     return (round(distance(this.x, this.y, this.z, x2, y2, z2)) == this.radius && round(distance(this.x, this.y, this.z, x2, (y2 + 1), z2)) > this.radius);
/*     */   }
/*     */   
/*     */   private boolean isBottomBlock(int x2, int y2, int z2) {
/* 208 */     return (round(distance(this.x, this.y, this.z, x2, y2, z2)) == this.radius && round(distance(this.x, this.y, this.z, x2, (y2 - 1), z2)) > this.radius);
/*     */   }
/*     */   
/*     */   public boolean shouldFinishChunk(int cx, int cz) {
/* 212 */     return this.unfinished.contains(new Point(cx, cz));
/*     */   }
/*     */   
/*     */   public boolean shouldDecorateChunk(int cx, int cz) {
/* 216 */     return this.finished.contains(new Point(cx, cz));
/*     */   }
/*     */   
/*     */   public boolean isAreaClear() {
/* 220 */     for (Planet p : this.chunkManager.unfinished) {
/* 221 */       if (p.intersects(this))
/* 222 */         return false; 
/*     */     } 
/* 224 */     for (Planet p : this.chunkManager.finished) {
/* 225 */       if (p.intersects(this))
/* 226 */         return false; 
/*     */     } 
/* 228 */     return true;
/*     */   }
/*     */   
/*     */   private boolean intersects(Planet planet) {
/* 232 */     return (distance(planet.x, planet.y, planet.z, this.x, this.y, this.z) <= (planet.radius + this.radius + 1));
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 236 */     return (this.unfinished.size() == 0);
/*     */   }
/*     */   
/*     */   public static int getBlockNum(int x, int y, int z) {
/* 240 */     if (x < 0)
/* 241 */       x = 16 + x; 
/* 242 */     if (z < 0)
/* 243 */       z = 16 + z; 
/* 244 */     return y + z * 256 + x * 256 * 16;
/*     */   }
/*     */   
/*     */   public static void setBlock(int x, int y, int z, Block block, int meta, Block[] ablock, byte[] ameta) {
/* 248 */     if (y < 0 || y >= 256) {
/*     */       return;
/*     */     }
/* 251 */     ablock[getBlockNum(x % 16, y, z % 16)] = block;
/* 252 */     ameta[getBlockNum(x % 16, y, z % 16)] = (byte)meta;
/*     */   }
/*     */   
/*     */   public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
/* 256 */     return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
/*     */   }
/*     */   
/*     */   public static int round(double d) {
/* 260 */     return (int)Math.round(d);
/*     */   }
/*     */ }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\Planet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */