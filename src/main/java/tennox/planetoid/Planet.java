 package tennox.planetoid;
 


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tennox.planetoid.config.PlanetoidConfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;









 public class Planet
 {
/*  13 */   static PlanetType DIRT = (new PlanetType(Blocks.DIRT, PlanetoidConfig.dirt, "Dirt")).setTopBlock((Block)Blocks.GRASS);
/*  14 */   static PlanetType WOOD = new PlanetType((Block)Blocks.LEAVES, Blocks.LOG, PlanetoidConfig.wood, "Wood");
/*  15 */   static PlanetType WATER = new PlanetType(Blocks.GLASS, Blocks.WATER, PlanetoidConfig.water, "Water");
/*  16 */   static PlanetType SAND = (new PlanetType(Blocks.SAND, PlanetoidConfig.sand, "sand")).setBottomBlock(Blocks.SANDSTONE);
/*  17 */   static PlanetType GLOWSTONE = new PlanetType(Blocks.GLOWSTONE, PlanetoidConfig.glowstone, "Glowstone");
/*  18 */   static PlanetType STONE = new PlanetType(Blocks.STONE, PlanetoidConfig.stone, "STONE");
   
/*  20 */   static PlanetType GRAVEL = new PlanetType(Blocks.STONE, Blocks.GRAVEL, PlanetoidConfig.gravel, "Gravel");
/*  21 */   static PlanetType COBBLESTONE = new PlanetType(Blocks.STONE, Blocks.COBBLESTONE, PlanetoidConfig.cobblestone, "Cobblestone");
/*  22 */   static PlanetType LAVA = new PlanetType(Blocks.STONE, Blocks.LAVA, PlanetoidConfig.lava, "Lava");
/*  23 */   static PlanetType COAL = new PlanetType(Blocks.STONE, Blocks.COAL_ORE, PlanetoidConfig.coal, "Coal");
/*  24 */   static PlanetType IRON = new PlanetType(Blocks.STONE, Blocks.IRON_ORE, PlanetoidConfig.iron, "Iron");
/*  25 */   static PlanetType GOLD = new PlanetType(Blocks.STONE, Blocks.GOLD_ORE, PlanetoidConfig.gold, "Gold");
/*  26 */   static PlanetType REDSTONE = new PlanetType(Blocks.STONE, Blocks.REDSTONE_ORE, PlanetoidConfig.redstone, "Redstone");
/*  27 */   static PlanetType LAPISLAZULI = new PlanetType(Blocks.STONE, Blocks.LAPIS_ORE, PlanetoidConfig.lapisLazuli, "Lapislazuli");
/*  28 */   static PlanetType TNT = new PlanetType(Blocks.STONE, Blocks.TNT, PlanetoidConfig.tnt, "TNT");
/*  29 */   static PlanetType DIAMOND = new PlanetType(Blocks.STONE, Blocks.DIAMOND_ORE, PlanetoidConfig.diamond, "Diamond");
/*  30 */   static PlanetType EMERALD = new PlanetType(Blocks.STONE, Blocks.EMERALD_ORE, PlanetoidConfig.emerald, "Emerald");
   
/*  32 */   static ArrayList<PlanetType> STONEtypes = new ArrayList<PlanetType>();
/*  33 */   static ArrayList<PlanetType> types = initTypes();
   
/*  35 */   Random rand = new Random();
   PlanetoidChunkManager chunkManager;
   World world2;
   int x;
   int y;
   int z;
   int radius;
   PlanetType type;
/*  43 */   ArrayList<Point> unfinished = new ArrayList<Point>();
/*  44 */   ArrayList<Point> finished = new ArrayList<Point>();
   
   public Planet(PlanetoidChunkManager cm, World w, int x, int y, int z, int r) {
/*  47 */     this.chunkManager = cm;
/*  48 */     this.world2 = w;
/*  49 */     this.x = x;
/*  50 */     this.y = y;
/*  51 */     this.z = z;
/*  52 */     this.radius = r;
     
/*  54 */     this.type = getRandomPlanet();
   }
   
   private static ArrayList<PlanetType> initTypes() {
/*  58 */     ArrayList<PlanetType> list = new ArrayList<PlanetType>();
/*  59 */     list.add(DIRT);
/*  60 */     list.add(WOOD);
/*  61 */     list.add(WATER);
/*  62 */     list.add(SAND);
/*  63 */     list.add(GLOWSTONE);
/*  64 */     list.add(STONE);
/*  65 */     STONEtypes.add(GRAVEL);
/*  66 */     STONEtypes.add(COBBLESTONE);
/*  67 */     STONEtypes.add(LAVA);
/*  68 */     STONEtypes.add(COAL);
/*  69 */     STONEtypes.add(IRON);
/*  70 */     STONEtypes.add(GOLD);
/*  71 */     STONEtypes.add(REDSTONE);
/*  72 */     STONEtypes.add(LAPISLAZULI);
/*  73 */     STONEtypes.add(TNT);
/*  74 */     STONEtypes.add(DIAMOND);
/*  75 */     STONEtypes.add(EMERALD);
/*  76 */     return list;
   }
   
   public Planet(PlanetoidChunkManager cm, World w, double x, double y, double z, double r) {
/*  80 */     this(cm, w, round(x), round(y), round(z), round(r));
   }
   
   public static void print() {
/*  84 */     System.out.println("---PREGENERATION: ---");
/*  85 */     for (PlanetType p : types) {
/*  86 */       System.out.println(p.name + ":\t" + p.total);
     }
     
/*  89 */     for (PlanetType p : STONEtypes) {
/*  90 */       System.out.println("-" + p.name + ":\t" + p.total);
     }
/*  92 */     System.out.println("---PREGENERATION END---");
   }
   
   public PlanetType getRandomPlanet() {
/*  96 */     this.rand.setSeed(this.x * 341873128712L + this.z * 132897987541L);
     
/*  98 */     ArrayList<PlanetType> list = new ArrayList<PlanetType>();
     
/* 100 */     list.addAll(types);
     
/* 102 */     PlanetType type = (PlanetType)WeightedRandom.getRandomItem(this.rand, list);
     
/* 104 */     if (type == STONE) {
/* 105 */       list.clear();
/* 106 */       list.addAll(STONEtypes);
       
/* 108 */       type = (PlanetType)WeightedRandom.getRandomItem(this.rand, list);
     } 
     
/* 111 */     type.total++;
/* 112 */     if (type.out == Blocks.STONE)
/* 113 */       STONE.total++; 
/* 114 */     return type;
   }
   
   public void generateChunk(int chunkX, int chunkZ, Block[] ablock, byte[] ameta) {
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
             else
/* 130 */             { setBlock(x2, y2, z2, this.type.out, 0, ablock, ameta); } 
/* 131 */           } else if (d < this.radius) {
/* 132 */             setBlock(x2, y2, z2, this.type.in, 0, ablock, ameta);
           } 
/* 134 */           generateSpecial(x2, y2, z2, ablock, ameta);
         } 
       } 
     } 
     
/* 139 */     if (!this.finished.contains(new Point(chunkX, chunkZ)))
/* 140 */       this.finished.add(new Point(chunkX, chunkZ)); 
/* 141 */     if (this.unfinished.contains(new Point(chunkX, chunkZ)))
/* 142 */       this.unfinished.remove(new Point(chunkX, chunkZ)); 
/* 143 */     TimeAnalyzer.end("generateChunk");
   }
   
   public void decorateChunk(World world, int chunkX, int chunkZ) {
/* 147 */     this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
/* 148 */     TimeAnalyzer.start("decorateChunk");
/* 149 */     for (int x2 = Math.max(chunkX * 16, this.x - this.radius); x2 <= Math.min(chunkX * 16 + 15, this.x + this.radius); x2++) {
/* 150 */       for (int y2 = this.y - this.radius; y2 <= this.y + this.radius; y2++) {
/* 151 */         for (int z2 = Math.max(chunkZ * 16, this.z - this.radius); z2 <= Math.min(chunkZ * 16 + 15, this.z + this.radius); z2++) {
/* 152 */           if (isTopBlock(x2, y2, z2)) {
/* 153 */             if (this.type == SAND && this.rand.nextDouble() <= 0.05D && Blocks.CACTUS.canPlaceBlockAt(world, new BlockPos(x2, y2 + 1, z2))) {
/* 154 */               for (int i = 1; i <= 1 + this.rand.nextInt(3); i++) {
/* 155 */                 world.setBlockState(new BlockPos(x2, y2 + i, z2), Blocks.CACTUS.getDefaultState());
               }
/* 157 */             } else if (this.type == DIRT) {
/* 158 */               if (this.rand.nextDouble() <= 0.1D && Blocks.TALLGRASS.canPlaceBlockAt(world, new BlockPos(x2, y2 + 1, z2))) {
/* 159 */                 world.setBlockState(new BlockPos(x2, y2 + 1, z2), Blocks.TALLGRASS.getDefaultState());//, 1, 3);
/* 160 */               } else if (this.rand.nextDouble() <= 0.004D) {
/* 161 */                 boolean flag1 = (world.getBlockState(new BlockPos(x2 + 1, y2, z2)) == Blocks.GRASS && world.getBlockState(new BlockPos(x2 - 1, y2, z2)) == Blocks.GRASS && world.getBlockState(new BlockPos(x2, y2, z2 + 1)) == Blocks.GRASS && world.getBlockState(new BlockPos(x2, y2, z2 - 1)) == Blocks.GRASS);
                 
/* 163 */                 boolean flag2 = (world.getBlockState(new BlockPos(x2 + 1, y2 + 1, z2)) == Blocks.AIR && world.getBlockState(new BlockPos(x2 - 1, y2 + 1, z2)) == Blocks.AIR && world.getBlockState(new BlockPos(x2, y2 + 1, z2 + 1)) == Blocks.AIR && world.getBlockState(new BlockPos(x2, y2 + 1, z2 - 1)) == Blocks.AIR);
 
                 
/* 166 */                 if (flag1 && flag2) {
/* 167 */                   world.setBlockState(new BlockPos(x2, y2, z2), Blocks.WATER.getDefaultState()); int i;
/* 168 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 169 */                     world.setBlockState(new BlockPos(x2 + 1, y2 + i, z2), Blocks.REEDS.getDefaultState());
/* 170 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 171 */                     world.setBlockState(new BlockPos(x2 - 1, y2 + i, z2), Blocks.REEDS.getDefaultState());
/* 172 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 173 */                     world.setBlockState(new BlockPos(x2, y2 + i, z2 + 1), Blocks.REEDS.getDefaultState());
/* 174 */                   for (i = 1; i <= 1 + this.rand.nextInt(3); i++)
/* 175 */                     world.setBlockState(new BlockPos(x2, y2 + i, z2 - 1), Blocks.REEDS.getDefaultState());
                 } 
               } 
             } 
/* 179 */           } else if (isBottomBlock(x2, y2, z2) && 
/* 180 */             this.type == WATER) {
/* 181 */             if (x2 == this.x && z2 == this.z) {
/* 182 */               world.setBlockToAir(new BlockPos(x2, y2, z2));
             }
/* 184 */             else if (world.getBlockState(new BlockPos(x2, y2 + 1, z2)) == Blocks.WATER || world.getBlockState(new BlockPos(x2, y2 + 1, z2)) == Blocks.FLOWING_WATER) {
/* 185 */               for (int i = 1; i <= 1 + this.rand.nextInt(2); i++)
/* 186 */                 world.setBlockState(new BlockPos(x2, y2 + i, z2), Blocks.CLAY.getDefaultState());
/* 187 */             } else if (world.getBlockState(new BlockPos(x2, y2 + 2, z2)) == Blocks.WATER || world.getBlockState(new BlockPos(x2, y2 + 2, z2)) == Blocks.FLOWING_WATER) {
/* 188 */               for (int i = 2; i <= 2 + this.rand.nextInt(2); i++) {
/* 189 */                 world.setBlockState(new BlockPos(x2, y2 + i, z2), Blocks.CLAY.getDefaultState());
               }
             } 
           } 
         } 
       } 
     } 
     
/* 197 */     TimeAnalyzer.end("decorateChunk");
   }
 
   
   private void generateSpecial(int x, int y, int z, Block[] ablock, byte[] data) {}
   
   private boolean isTopBlock(int x2, int y2, int z2) {
/* 204 */     return (round(distance(this.x, this.y, this.z, x2, y2, z2)) == this.radius && round(distance(this.x, this.y, this.z, x2, (y2 + 1), z2)) > this.radius);
   }
   
   private boolean isBottomBlock(int x2, int y2, int z2) {
/* 208 */     return (round(distance(this.x, this.y, this.z, x2, y2, z2)) == this.radius && round(distance(this.x, this.y, this.z, x2, (y2 - 1), z2)) > this.radius);
   }
   
   public boolean shouldFinishChunk(int cx, int cz) {
/* 212 */     return this.unfinished.contains(new Point(cx, cz));
   }
   
   public boolean shouldDecorateChunk(int cx, int cz) {
/* 216 */     return this.finished.contains(new Point(cx, cz));
   }
   
   public boolean isAreaClear() {
/* 220 */     for (Planet p : this.chunkManager.unfinished) {
/* 221 */       if (p.intersects(this))
/* 222 */         return false; 
     } 
/* 224 */     for (Planet p : this.chunkManager.finished) {
/* 225 */       if (p.intersects(this))
/* 226 */         return false; 
     } 
/* 228 */     return true;
   }
   
   private boolean intersects(Planet planet) {
/* 232 */     return (distance(planet.x, planet.y, planet.z, this.x, this.y, this.z) <= (planet.radius + this.radius + 1));
   }
   
   public boolean isFinished() {
/* 236 */     return (this.unfinished.size() == 0);
   }
   
   public static int getBlockNum(int x, int y, int z) {
/* 240 */     if (x < 0)
/* 241 */       x = 16 + x; 
/* 242 */     if (z < 0)
/* 243 */       z = 16 + z; 
/* 244 */     return y + z * 256 + x * 256 * 16;
   }
   
   public static void setBlock(int x, int y, int z, Block block, int meta, Block[] ablock, byte[] ameta) {
/* 248 */     if (y < 0 || y >= 256) {
       return;
     }
/* 251 */     ablock[getBlockNum(x % 16, y, z % 16)] = block;
/* 252 */     ameta[getBlockNum(x % 16, y, z % 16)] = (byte)meta;
   }
   
   public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
/* 256 */     return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
   }
   
   public static int round(double d) {
/* 260 */     return (int)Math.round(d);
   }
 }


/* Location:              C:\Minecraft Modding\DecompPlanetoid\Planetoid-Mod-1.7.10-deobf.zip!\tennox\planetoid\Planet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */