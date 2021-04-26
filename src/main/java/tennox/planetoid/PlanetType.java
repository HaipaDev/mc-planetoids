 package tennox.planetoid;
 
 import net.minecraft.block.Block;
 import net.minecraft.util.WeightedRandom;
 
 class PlanetType extends WeightedRandom.Item {
   Block in;
   Block out;
   int total;
/* 10 */   Block bottom = null; Block top = null;
   String name;
   
   public PlanetType(Block b, int i, String n) {
/* 14 */     this(b, b, i, n);
   }
   
   PlanetType(Block o, Block i, int w, String n) {
/* 18 */     super(w);
/* 19 */     this.out = o;
/* 20 */     this.in = i;
/* 21 */     this.name = n;
   }
   
   public Block getTopBlock() {
/* 25 */     return (this.top != null) ? this.top : this.out;
   }
   
   public Block getBottomBlock() {
/* 29 */     return (this.bottom != null) ? this.bottom : this.out;
   }
   
   public PlanetType setTopBlock(Block i) {
/* 33 */     this.top = i;
/* 34 */     return this;
   }
   
   public PlanetType setBottomBlock(Block i) {
/* 38 */     this.bottom = i;
/* 39 */     return this;
   }
 }