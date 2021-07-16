package tennox.planetoid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import sun.awt.HKSCS;

// TODO: Auto-generated Javadoc
public class PlanetoidChunkManager implements IChunkGenerator, IChunkProvider {
    protected  World world;

    protected ChunkPrimer chunkPrimer = new ChunkPrimer();
    protected int chunkX;
    protected int chunkZ;
    protected ChunkPos chunkPos;
    protected boolean mapFeaturesEnabled = true;

    protected  Random rand;

    protected  WorldType terrainType;
    protected Biome biome = Biomes.DEFAULT;
    protected IBlockState baseBlock = Blocks.GRASS.getDefaultState();
    protected IBlockState oceanBlock = Blocks.WATER.getDefaultState();

    protected NoiseGeneratorOctaves minLimitPerlinNoise;
    protected NoiseGeneratorOctaves maxLimitPerlinNoise;
    protected NoiseGeneratorOctaves mainPerlinNoise;
    protected NoiseGeneratorPerlin surfaceNoise;
    protected NoiseGeneratorOctaves scaleNoise;
    protected NoiseGeneratorOctaves depthNoise;
    protected NoiseGeneratorOctaves forestNoise;

    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    protected double[] heightMap = new double[825];
    protected float[] biomeWeights = new float[25];
    protected double[] depthBuffer = new double[256];

    protected MapGenBase caveGenerator = new MapGenCaves();
    protected MapGenStronghold strongholdGenerator = new MapGenStronghold();
    //protected MapGenVillage villageGenerator = new MapGenVillage();
    protected MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    protected MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    protected MapGenBase ravineGenerator = new MapGenRavine();
    protected StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
    protected WorldGenDungeons dungeonGenerator = new WorldGenDungeons();
    protected WorldGenLakes lakeGenerator = new WorldGenLakes(Blocks.WATER);
    protected WorldGenLakes lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);

    /*
     * These values are explained here: https://minecraft.gamepedia.com/Customized#Customization
     */
    private boolean useRavines = false;
    private boolean useMineShafts = false;
    private boolean useVillages = false;
    private boolean useStrongholds = false;
    private boolean useTemples = false;
    private boolean useMonuments = false;
    private boolean useCaves = false;
    private boolean useWaterLakes = false;
    private boolean useLavaLakes = false;
    private boolean useDungeons = false;
    private double depthNoiseScaleX = 200.0D;
    private double depthNoiseScaleZ = 200.0D;
    private double depthNoiseScaleExponent = 0.5D;
    private int coordScale = 684;
    private int mainNoiseScaleX = 80;
    private int mainNoiseScaleY = 160;
    private int mainNoiseScaleZ = 80;
    private int heightScale = 684;
    private int biomeDepthOffSet = 0;
    private int biomeScaleOffset = 0;
    private double heightStretch = 12;
    private double baseSize = 8.5D;
    private double lowerLimitScale = 512D;
    private double upperLimitScale = 512D;
    private float biomeDepthWeight = 1.0F;
    private float biomeScaleWeight = 1.0F;
    private int waterLakeChance =4;
    private int dungeonChance = 7;
    private int lavaLakeChance = 80;

    private PlanetoidGeneratorInfo generatorInfo;
    public ArrayList<Planet> unfinished=new ArrayList<Planet>();
    public ArrayList<Planet> finished=new ArrayList<Planet>();
    private int pregenChunkSize=4;
    private ArrayList<Point> pregen=new ArrayList<Point>();
    private List<Biome> biomesForGeneration;

    /**
     * Instantiates a new chunk generator cloud.
     *
     * @param worldIn the world in
     */
    public PlanetoidChunkManager(World worldIn, String generatorOptions)
    {
        // DEBUG
        System.out.println("Constructing ChunkGeneratorCloud");

        world = worldIn;
        rand = new Random(world.getSeed());
        terrainType = world.getWorldInfo().getTerrainType();
        mapFeaturesEnabled = world.getWorldInfo().isMapFeaturesEnabled();
        //world.setSeaLevel(63);
        //MapGenVillage.VILLAGE_SPAWN_BIOMES.add(Biomes.DEFAULT);

        this.generatorInfo = PlanetoidGeneratorInfo.createGeneratorFromString(generatorOptions);

        //initNoiseGenerators();
        //postTerrainGenEvents();
        //setBiomeWeights();
        //postNoiseEvent();
    }

    private void postNoiseEvent()
    {
        InitNoiseGensEvent.ContextOverworld ctx = new InitNoiseGensEvent.ContextOverworld(minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise, forestNoise);
        ctx = TerrainGen.getModdedNoiseGenerators(world, rand, ctx);
        minLimitPerlinNoise = ctx.getLPerlin1();
        maxLimitPerlinNoise = ctx.getLPerlin2();
        mainPerlinNoise = ctx.getPerlin();
        surfaceNoise = ctx.getHeight();
        scaleNoise = ctx.getScale();
        depthNoise = ctx.getDepth();
        forestNoise = ctx.getForest();
    }

    private void setBiomeWeights()
    {
        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt(i * i + j * j + 0.2F);
                biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }
    }

    /**
     * Inits the noise generators.
     */
    protected void initNoiseGenerators()
    {
        minLimitPerlinNoise = new NoiseGeneratorOctaves(rand, 16);
        maxLimitPerlinNoise = new NoiseGeneratorOctaves(rand, 16);
        mainPerlinNoise = new NoiseGeneratorOctaves(rand, 8);
        surfaceNoise = new NoiseGeneratorPerlin(rand, 4);
        scaleNoise = new NoiseGeneratorOctaves(rand, 10);
        depthNoise = new NoiseGeneratorOctaves(rand, 16);
        forestNoise = new NoiseGeneratorOctaves(rand, 8);
    }

    /**
     * Post terrain gen events.
     */
    protected void postTerrainGenEvents()
    {
        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, InitMapGenEvent.EventType.CAVE);
        strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator, InitMapGenEvent.EventType.STRONGHOLD);
        //villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, InitMapGenEvent.EventType.VILLAGE);
        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, InitMapGenEvent.EventType.MINESHAFT);
        scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator, InitMapGenEvent.EventType.SCATTERED_FEATURE);
        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, InitMapGenEvent.EventType.RAVINE);
        oceanMonumentGenerator = (StructureOceanMonument) TerrainGen.getModdedMapGen(oceanMonumentGenerator, InitMapGenEvent.EventType.OCEAN_MONUMENT);
    }

    @Nullable
    @Override
    public Chunk getLoadedChunk(int i, int i1) { return new Chunk(world,i,i1); }

    @Override
    public Chunk provideChunk(int par1, int par2) {
        /*  96 */     TimeAnalyzer.start("provideChunk");
        /*  97 */     this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
        /*  98 */     preGenerate(par1, par2);


        //this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData((BiomeProvider[])null, par1 * 16, par2 * 16, 16, 16);
        /* 102 */     this.biomesForGeneration = this.world.getBiomeProvider().getBiomesToSpawnIn();

        /* 103 */     generateChunk(par1, par2);//, ablock, abyte);

        /* 105 */     TimeAnalyzer.start("provide_default");

                 /*this.caveGenerator.generate(this.world, par1, par2, new ChunkPrimer());
                 if (this.mapFeaturesEnabled) {
                   this.villageGenerator.generate(this.world, par1, par2, new ChunkPrimer());
                   this.strongholdGenerator.generate(this.world, par1, par2, new ChunkPrimer());
                   this.scatteredFeatureGenerator.generate(this.world, par1, par2, new ChunkPrimer());
                }*/
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

    /**
     * Generates the chunk at the specified position, from scratch.
     *
     * @param parChunkX the par chunk X
     * @param parChunkZ the par chunk Z
     * @return the chunk
     */
    //@Override
    public Chunk generateChunk(int parChunkX, int parChunkZ){
        TimeAnalyzer.start("generate");
        TimeAnalyzer.start("finishPlanets");
        chunkX = parChunkX;
        chunkZ = parChunkZ;
        rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
        //generateHeightmap();
        //setBlocksInChunk();
        //replaceBiomeBlocks();

        Block[] ablock = new Block[65536];
        byte[] ameta = new byte[65536];
        for (int i = 0; i < this.unfinished.size(); i++) {
           Planet p = this.unfinished.get(i);
               if (p.shouldFinishChunk(chunkX, chunkZ))
                   p.generateChunk(chunkX, chunkZ, ablock, ameta);
               if (p.isFinished()) {
                 this.unfinished.remove(p);
                 this.finished.add(p);
                 i--;
                }
        }

        TimeAnalyzer.end("finishPlanets");

        Chunk chunk = new Chunk(world, chunkPrimer, parChunkX, parChunkZ);
        /*byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) { abyte[i] = (byte) Biome.getIdForBiome(biome); }
        chunk.generateSkylightMap();*/
        return chunk;
    }

    //public Chunk loadChunk(int par1, int par2){return provideChunk(par1,par2);}


    /**
     * Sets the blocks in chunk.
     */
    public void setBlocksInChunk()
    {
        for (int i = 0; i < 4; ++i)
        {
            int j = i * 5;
            for (int l = 0; l < 4; ++l)
            {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                for (int i2 = 0; i2 < 32; ++i2)
                {
                    for (int j2 = 0; j2 < 8; ++j2)
                    {
                        double d10 = heightMap[i1 + i2];
                        double d11 = heightMap[j1 + i2];

                        for (int k2 = 0; k2 < 4; ++k2)
                        {
                            double d16 = (d11 - d10) * 0.25D;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                            {
                                if ((lvt_45_1_ += d16) > 0.0D)
                                {
                                    chunkPrimer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, baseBlock);
                                }
                                else if (i2 * 8 + j2 <  world.getSeaLevel())
                                {
                                    chunkPrimer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, oceanBlock);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Replace biome blocks.
     */
    public void replaceBiomeBlocks()
    {
        if (!ForgeEventFactory.onReplaceBiomeBlocks(this, chunkX, chunkZ, chunkPrimer, world))
        {
            return;
        }
        depthBuffer = surfaceNoise.getRegion(depthBuffer, chunkX * 16, chunkZ * 16, 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int xInChunk = 0; xInChunk < 16; ++xInChunk)
        {
            for (int zInChunk = 0; zInChunk < 16; ++zInChunk)
            {
                biome.genTerrainBlocks(world, rand, chunkPrimer, chunkX * 16 + xInChunk, chunkZ * 16 + zInChunk, depthBuffer[zInChunk + xInChunk * 16]);
            }
        }
    }

    /**
     * The Minecraft world generator uses many Perlin noise functions to generate the
     * surface terrain. Three Perlin noise functions are combined to form the standard
     * hills: a main function (Main Noise), a lower limit (Lower Limit) and a ceiling
     * (Upper Limit). The world generator is calculated for each coordinate (X, Z) by
     * comparing the average value between the lower limit and upper limit to the value
     * of the main function. The base height (Depth Base) determines the separation
     * between the standard hills and valleys and is independent from sea level.[1]
     *
     * The default scenery is not seen in the finished world, because each biome has
     * specific properties. Plains are flat, hills have small to medium elevations,
     * extreme mountains are high mountain ranges, oceans have deep valleys, savannas
     * and mesas have low mountains with flat plateaus, etc. Each biome type has an
     * individual biome depth (Biome Depth) and an individual biome factor (Biome Scale)
     * in order to perform the biome specific deformations.
     */
    protected void generateHeightmap()
    {
        int xOffset = chunkX * 4;
        int zOffset = chunkZ * 4;
        depthRegion = depthNoise.generateNoiseOctaves(
                depthRegion,
                xOffset,
                zOffset,
                5,
                5,
                depthNoiseScaleX,
                depthNoiseScaleZ,
                depthNoiseScaleExponent
        );
        mainNoiseRegion = mainPerlinNoise.generateNoiseOctaves(mainNoiseRegion, xOffset, 0, zOffset, 5, 33, 5,
                coordScale / mainNoiseScaleX, heightScale / mainNoiseScaleY, coordScale / mainNoiseScaleZ);
        minLimitRegion = minLimitPerlinNoise.generateNoiseOctaves(minLimitRegion, xOffset, 0, zOffset, 5, 33, 5, coordScale,
                heightScale, coordScale);
        maxLimitRegion = maxLimitPerlinNoise.generateNoiseOctaves(maxLimitRegion, xOffset, 0, zOffset, 5, 33, 5, coordScale,
                heightScale, coordScale);

        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float accumulatedHeightVariation = 0.0F;
                float accumulatedHeight = 0.0F;
                float accumulatedWeightedHeightFactor = 0.0F;

                for (int j1 = -2; j1 <= 2; ++j1)
                {
                    for (int k1 = -2; k1 <= 2; ++k1)
                    {
                        float baseHeight = biomeDepthOffSet + biome.getBaseHeight() * biomeDepthWeight;
                        float heightVariation = biomeScaleOffset + biome.getHeightVariation() * biomeScaleWeight;

                        float weightedHeightFactor = biomeWeights[j1 + 2 + (k1 + 2) * 5] / (baseHeight + 2.0F);

                        accumulatedHeightVariation += heightVariation * weightedHeightFactor;
                        accumulatedHeight += baseHeight * weightedHeightFactor;
                        accumulatedWeightedHeightFactor += weightedHeightFactor;
                    }
                }

                accumulatedHeightVariation = accumulatedHeightVariation / accumulatedWeightedHeightFactor * 0.9F + 0.1F;
                accumulatedHeight = (accumulatedHeight / accumulatedWeightedHeightFactor * 4.0F - 1.0F) / 8.0F;
                double depthBy8k = depthRegion[j] / 8000.0D;

                if (depthBy8k < 0.0D)
                {
                    depthBy8k = -depthBy8k * 0.3D;
                }

                depthBy8k = depthBy8k * 3.0D - 2.0D;

                if (depthBy8k < 0.0D)
                {
                    depthBy8k = depthBy8k / 2.0D;

                    if (depthBy8k < -1.0D)
                    {
                        depthBy8k = -1.0D;
                    }

                    depthBy8k = depthBy8k / 2.8D;
                }
                else
                {
                    if (depthBy8k > 1.0D)
                    {
                        depthBy8k = 1.0D;
                    }

                    depthBy8k = depthBy8k / 8.0D;
                }

                ++j;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = (l1 - (baseSize + ((accumulatedHeight + depthBy8k * 0.2D) * baseSize / 8.0D) * 4.0D)) * heightStretch / 2.0D / accumulatedHeightVariation;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double minLimitScaled = minLimitRegion[i] / lowerLimitScale;
                    double maxLimitScaled = maxLimitRegion[i] / upperLimitScale;
                    double noiseValue = (mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
                    double linearInterpHeight = MathHelper.clampedLerp(minLimitScaled, maxLimitScaled, noiseValue) - d1;

                    if (l1 > 29)
                    {
                        linearInterpHeight = linearInterpHeight * (1.0D - ((l1 - 29) / 3.0F)) + -10.0D * ((l1 - 29) / 3.0F);
                    }

                    heightMap[i] = linearInterpHeight;
                    ++i;
                }
            }
        }
    }

    /**
     * Generate initial structures in this chunk, e.g. mineshafts, temples, lakes, and dungeons
     *
     * @param parChunkX
     *            Chunk x coordinate
     * @param parChunkZ
     *            Chunk z coordinate
     */
    @Override
    public void populate(int parChunkX, int parChunkZ)
    {
        BlockFalling.fallInstantly = true;

        int chunkStartXInWorld = parChunkX * 16;
        int chunkStartZInWorld = parChunkZ * 16;
        BlockPos blockpos = new BlockPos(chunkStartXInWorld, 0, chunkStartZInWorld);
        rand.setSeed(world.getSeed());
        long k = rand.nextLong() / 2L * 2L + 1L;
        long l = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed(parChunkX * k + parChunkZ * l ^ world.getSeed());
        chunkPos = new ChunkPos(parChunkX, parChunkZ);
        boolean villageHasGenerated = false;

        ForgeEventFactory.onChunkPopulate(true, this, world, rand, parChunkX, parChunkZ, villageHasGenerated);

        if (mapFeaturesEnabled)
        {
            villageHasGenerated = generateMapFeatures();
        }

        if (useWaterLakes && !villageHasGenerated && rand.nextInt(waterLakeChance) == 0)
            if (TerrainGen.populate(this, world, rand, parChunkX, parChunkZ, villageHasGenerated, PopulateChunkEvent.Populate.EventType.LAKE))
            {
                int lakeStartX = rand.nextInt(16) + 8;
                int lakeStartY = rand.nextInt(256);
                int lakeStartZ = rand.nextInt(16) + 8;

                lakeGenerator.generate(world, rand, blockpos.add(lakeStartX, lakeStartY, lakeStartZ));
            }

        if (useLavaLakes && !villageHasGenerated && rand.nextInt(lavaLakeChance / 10) == 0)
            if (TerrainGen.populate(this, world, rand, parChunkX, parChunkZ, villageHasGenerated, PopulateChunkEvent.Populate.EventType.LAVA))
            {
                int lavaStartX = rand.nextInt(16) + 8;
                int lavaStartY = rand.nextInt(rand.nextInt(248) + 8);
                int lavaStartZ = rand.nextInt(16) + 8;

                if (lavaStartY < world.getSeaLevel() || rand.nextInt(lavaLakeChance / 8) == 0)
                {
                    lavaLakeGenerator.generate(world, rand, blockpos.add(lavaStartX, lavaStartY, lavaStartZ));
                }
            }

        if (useDungeons)
            if (TerrainGen.populate(this, world, rand, parChunkX, parChunkZ, villageHasGenerated, PopulateChunkEvent.Populate.EventType.DUNGEON))
            {
                for (int dungeonAttempt = 0; dungeonAttempt < dungeonChance; ++dungeonAttempt)
                {
                    int dungeonStartX = rand.nextInt(16) + 8;
                    int dungeonStartY = rand.nextInt(256);
                    int dungeonStartZ = rand.nextInt(16) + 8;
                    dungeonGenerator.generate(world, rand, blockpos.add(dungeonStartX, dungeonStartY, dungeonStartZ));
                }
            }

        biome.decorate(world, rand, new BlockPos(chunkStartXInWorld, 0, chunkStartZInWorld));

        /*
         * Spawn creatures
         */
        if (TerrainGen.populate(this, world, rand, parChunkX, parChunkZ, villageHasGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM))
        {
            WorldEntitySpawner.performWorldGenSpawning(world, biome, chunkStartXInWorld + 8, chunkStartZInWorld + 8, 16, 16, rand);
        }

        ForgeEventFactory.onChunkPopulate(false, this, world, rand, parChunkX, parChunkZ, villageHasGenerated);

        BlockFalling.fallInstantly = false;
    }

    private boolean generateMapFeatures()
    {
        boolean villageHasGenerated = false;

        if (useMineShafts)
        {
            mineshaftGenerator.generateStructure(world, rand, chunkPos);
        }

        /*if (useVillages)
        {
            villageHasGenerated = villageGenerator.generateStructure(world, rand, chunkPos);
        }*/

        if (useStrongholds)
        {
            strongholdGenerator.generateStructure(world, rand, chunkPos);
        }

        if (useTemples)
        {
            scatteredFeatureGenerator.generateStructure(world, rand, chunkPos);
        }

        if (useMonuments)
        {
            oceanMonumentGenerator.generateStructure(world, rand, chunkPos);
        }

        return villageHasGenerated;
    }

    /**
     * Called to generate additional structures after initial worldgen, used by ocean monuments.
     *
     * @param chunkIn the chunk in
     * @param x the x
     * @param z the z
     * @return true, if successful
     */
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        boolean structureHasGenerated = false;

        if (useMonuments && mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L)
        {
            structureHasGenerated |= oceanMonumentGenerator.generateStructure(world, rand, new ChunkPos(x, z));
        }

        return structureHasGenerated;
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.gen.IChunkGenerator#getPossibleCreatures(net.minecraft.entity.EnumCreatureType, net.minecraft.util.math.BlockPos)
     */
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = world.getBiome(pos);

        if (mapFeaturesEnabled)
        {
            if (creatureType == EnumCreatureType.MONSTER && scatteredFeatureGenerator.isSwampHut(pos))
            {
                return scatteredFeatureGenerator.getMonsters();
            }

            if (creatureType == EnumCreatureType.MONSTER && useMonuments && oceanMonumentGenerator.isPositionInStructure(world, pos))
            {
                return oceanMonumentGenerator.getMonsters();
            }
        }

        return biome.getSpawnableList(creatureType);
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.gen.IChunkGenerator#isInsideStructure(net.minecraft.world.World, java.lang.String, net.minecraft.util.math.BlockPos)
     */
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        if (!mapFeaturesEnabled)
        {
            return false;
        }
        else if ("Stronghold".equals(structureName) && strongholdGenerator != null)
        {
            return strongholdGenerator.isInsideStructure(pos);
        }
        else if ("Monument".equals(structureName) && oceanMonumentGenerator != null)
        {
            return oceanMonumentGenerator.isInsideStructure(pos);
        }
        /*else if ("Village".equals(structureName) && villageGenerator != null)
        {
            return villageGenerator.isInsideStructure(pos);
        }*/
        else if ("Mineshaft".equals(structureName) && mineshaftGenerator != null)
        {
            return mineshaftGenerator.isInsideStructure(pos);
        }
        else
        {
            return "Temple".equals(structureName) && scatteredFeatureGenerator != null ? scatteredFeatureGenerator.isInsideStructure(pos) : false;
        }
    }

    /* (non-Javadoc)
     * @see net.minecraft.world.gen.IChunkGenerator#getNearestStructurePos(net.minecraft.world.World, java.lang.String, net.minecraft.util.math.BlockPos, boolean)
     */
    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        if (!mapFeaturesEnabled)
        {
            return null;
        }
        else if ("Stronghold".equals(structureName) && strongholdGenerator != null)
        {
            return strongholdGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        else if ("Monument".equals(structureName) && oceanMonumentGenerator != null)
        {
            return oceanMonumentGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        /*else if ("Village".equals(structureName) && villageGenerator != null)
        {
            return villageGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }*/
        else if ("Mineshaft".equals(structureName) && mineshaftGenerator != null)
        {
            return mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        else
        {
            return "Temple".equals(structureName) && scatteredFeatureGenerator != null
                    ? scatteredFeatureGenerator.getNearestStructurePos(worldIn, position, findUnexplored)
                    : null;
        }
    }

    /**
     * Recreates data about structures intersecting given chunk (used for example by getPossibleCreatures), without placing any blocks. When called for the first time before any
     * chunk is generated - also initializes the internal state needed by getPossibleCreatures.
     *
     * @param chunkIn the chunk in
     * @param x the x
     * @param z the z
     */
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        if (mapFeaturesEnabled)
        {
            if (useMineShafts)
            {
                mineshaftGenerator.generate(world, x, z, (ChunkPrimer) null);
            }

            /*if (useVillages)
            {
                villageGenerator.generate(world, x, z, (ChunkPrimer) null);
            }*/

            if (useStrongholds)
            {
                strongholdGenerator.generate(world, x, z, (ChunkPrimer) null);
            }

            if (useTemples)
            {
                scatteredFeatureGenerator.generate(world, x, z, (ChunkPrimer) null);
            }

            if (useMonuments)
            {
                oceanMonumentGenerator.generate(world, x, z, (ChunkPrimer) null);
            }
        }
    }


    public boolean tick(){return false;}
    public String makeString(){return "Planetoid";}
    public boolean isChunkGeneratedAt(int x,int z){return true;}
    public static int round(double d) {return (int)Math.round(d);}
}