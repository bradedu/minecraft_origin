package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkConverter {

    private static final Logger b = LogManager.getLogger();
    public static final ChunkConverter a = new ChunkConverter();
    private static final EnumDirection8[] c = EnumDirection8.values();
    private final EnumSet<EnumDirection8> d;
    private final int[][] e;
    private static final Map<Block, ChunkConverter.a> f = new IdentityHashMap();
    private static final Set<ChunkConverter.a> g = Sets.newHashSet();

    private ChunkConverter() {
        this.d = EnumSet.noneOf(EnumDirection8.class);
        this.e = new int[16][];
    }

    public ChunkConverter(NBTTagCompound nbttagcompound) {
        this();
        if (nbttagcompound.hasKeyOfType("Indices", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Indices");

            for (int i = 0; i < this.e.length; ++i) {
                String s = String.valueOf(i);

                if (nbttagcompound1.hasKeyOfType(s, 11)) {
                    this.e[i] = nbttagcompound1.getIntArray(s);
                }
            }
        }

        int j = nbttagcompound.getInt("Sides");
        EnumDirection8[] aenumdirection8 = EnumDirection8.values();
        int k = aenumdirection8.length;

        for (int l = 0; l < k; ++l) {
            EnumDirection8 enumdirection8 = aenumdirection8[l];

            if ((j & 1 << enumdirection8.ordinal()) != 0) {
                this.d.add(enumdirection8);
            }
        }

    }

    public void a(Chunk chunk) {
        this.b(chunk);
        EnumDirection8[] aenumdirection8 = ChunkConverter.c;
        int i = aenumdirection8.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection8 enumdirection8 = aenumdirection8[j];

            a(chunk, enumdirection8);
        }

        World world = chunk.getWorld();

        ChunkConverter.g.forEach((chunkconverter_a) -> {
            chunkconverter_a.a(world);
        });
    }

    private static void a(Chunk chunk, EnumDirection8 enumdirection8) {
        World world = chunk.getWorld();

        if (chunk.F().d.remove(enumdirection8)) {
            Set<EnumDirection> set = enumdirection8.a();
            boolean flag = false;
            boolean flag1 = true;
            boolean flag2 = set.contains(EnumDirection.EAST);
            boolean flag3 = set.contains(EnumDirection.WEST);
            boolean flag4 = set.contains(EnumDirection.SOUTH);
            boolean flag5 = set.contains(EnumDirection.NORTH);
            boolean flag6 = set.size() == 1;
            int i = (chunk.locX << 4) + (flag6 && (flag5 || flag4) ? 1 : (flag3 ? 0 : 15));
            int j = (chunk.locX << 4) + (flag6 && (flag5 || flag4) ? 14 : (flag3 ? 0 : 15));
            int k = (chunk.locZ << 4) + (flag6 && (flag2 || flag3) ? 1 : (flag5 ? 0 : 15));
            int l = (chunk.locZ << 4) + (flag6 && (flag2 || flag3) ? 14 : (flag5 ? 0 : 15));
            EnumDirection[] aenumdirection = EnumDirection.values();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            Iterator iterator = BlockPosition.b(i, 0, k, j, world.getHeight() - 1, l).iterator();

            while (iterator.hasNext()) {
                BlockPosition.MutableBlockPosition blockposition_mutableblockposition1 = (BlockPosition.MutableBlockPosition) iterator.next();
                IBlockData iblockdata = world.getType(blockposition_mutableblockposition1);
                IBlockData iblockdata1 = iblockdata;
                EnumDirection[] aenumdirection1 = aenumdirection;
                int i1 = aenumdirection.length;

                for (int j1 = 0; j1 < i1; ++j1) {
                    EnumDirection enumdirection = aenumdirection1[j1];

                    blockposition_mutableblockposition.g(blockposition_mutableblockposition1).c(enumdirection);
                    iblockdata1 = a(iblockdata1, enumdirection, world, blockposition_mutableblockposition1, blockposition_mutableblockposition);
                }

                Block.a(iblockdata, iblockdata1, world, blockposition_mutableblockposition1, 18);
            }

        }
    }

    private static IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, GeneratorAccess generatoraccess, BlockPosition.MutableBlockPosition blockposition_mutableblockposition, BlockPosition.MutableBlockPosition blockposition_mutableblockposition1) {
        return ((ChunkConverter.a) ChunkConverter.f.getOrDefault(iblockdata.getBlock(), ChunkConverter.Type.DEFAULT)).a(iblockdata, enumdirection, generatoraccess.getType(blockposition_mutableblockposition1), generatoraccess, blockposition_mutableblockposition, blockposition_mutableblockposition1);
    }

    private void b(Chunk chunk) {
        BlockPosition.b blockposition_b = BlockPosition.b.r();
        Throwable throwable = null;

        try {
            BlockPosition.b blockposition_b1 = BlockPosition.b.r();
            Throwable throwable1 = null;

            try {
                World world = chunk.getWorld();

                int i;

                for (i = 0; i < 16; ++i) {
                    ChunkSection chunksection = chunk.getSections()[i];
                    int[] aint = this.e[i];

                    this.e[i] = null;
                    if (chunksection != null && aint != null && aint.length > 0) {
                        EnumDirection[] aenumdirection = EnumDirection.values();
                        DataPaletteBlock<IBlockData> datapaletteblock = chunksection.getBlocks();
                        int[] aint1 = aint;
                        int j = aint.length;

                        for (int k = 0; k < j; ++k) {
                            int l = aint1[k];
                            int i1 = l & 15;
                            int j1 = l >> 8 & 15;
                            int k1 = l >> 4 & 15;

                            blockposition_b.c(i1 + (chunk.locX << 4), j1 + (i << 4), k1 + (chunk.locZ << 4));
                            IBlockData iblockdata = (IBlockData) datapaletteblock.a(l);
                            IBlockData iblockdata1 = iblockdata;
                            EnumDirection[] aenumdirection1 = aenumdirection;
                            int l1 = aenumdirection.length;

                            for (int i2 = 0; i2 < l1; ++i2) {
                                EnumDirection enumdirection = aenumdirection1[i2];

                                blockposition_b1.g(blockposition_b).c(enumdirection);
                                if (blockposition_b.getX() >> 4 == chunk.locX && blockposition_b.getZ() >> 4 == chunk.locZ) {
                                    iblockdata1 = a(iblockdata1, enumdirection, world, blockposition_b, blockposition_b1);
                                }
                            }

                            Block.a(iblockdata, iblockdata1, world, blockposition_b, 18);
                        }
                    }
                }

                for (i = 0; i < this.e.length; ++i) {
                    if (this.e[i] != null) {
                        ChunkConverter.b.warn("Discarding update data for section {} for chunk ({} {})", i, chunk.locX, chunk.locZ);
                    }

                    this.e[i] = null;
                }
            } catch (Throwable throwable2) {
                throwable1 = throwable2;
                throw throwable2;
            } finally {
                if (blockposition_b1 != null) {
                    if (throwable1 != null) {
                        try {
                            blockposition_b1.close();
                        } catch (Throwable throwable3) {
                            throwable1.addSuppressed(throwable3);
                        }
                    } else {
                        blockposition_b1.close();
                    }
                }

            }
        } catch (Throwable throwable4) {
            throwable = throwable4;
            throw throwable4;
        } finally {
            if (blockposition_b != null) {
                if (throwable != null) {
                    try {
                        blockposition_b.close();
                    } catch (Throwable throwable5) {
                        throwable.addSuppressed(throwable5);
                    }
                } else {
                    blockposition_b.close();
                }
            }

        }

    }

    public boolean a() {
        int[][] aint = this.e;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int[] aint1 = aint[j];

            if (aint1 != null) {
                return false;
            }
        }

        return this.d.isEmpty();
    }

    public NBTTagCompound b() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        int i;

        for (i = 0; i < this.e.length; ++i) {
            String s = String.valueOf(i);

            if (this.e[i] != null && this.e[i].length != 0) {
                nbttagcompound1.setIntArray(s, this.e[i]);
            }
        }

        if (!nbttagcompound1.isEmpty()) {
            nbttagcompound.set("Indices", nbttagcompound1);
        }

        i = 0;

        EnumDirection8 enumdirection8;

        for (Iterator iterator = this.d.iterator(); iterator.hasNext(); i |= 1 << enumdirection8.ordinal()) {
            enumdirection8 = (EnumDirection8) iterator.next();
        }

        nbttagcompound.setByte("Sides", (byte) i);
        return nbttagcompound;
    }

    static enum Type implements ChunkConverter.a {

        BLACKLIST(new Block[] { Blocks.OBSERVER, Blocks.NETHER_PORTAL, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL, Blocks.DRAGON_EGG, Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND, Blocks.SIGN, Blocks.WALL_SIGN}) {
            public IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
                return iblockdata;
            }
        },
        DEFAULT(new Block[0]) {
            public IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
                return iblockdata.updateState(enumdirection, generatoraccess.getType(blockposition1), generatoraccess, blockposition, blockposition1);
            }
        },
        CHEST(new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST}) {
            public IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
                if (iblockdata1.getBlock() == iblockdata.getBlock() && enumdirection.k().c() && iblockdata.get(BlockChest.b) == BlockPropertyChestType.SINGLE && iblockdata1.get(BlockChest.b) == BlockPropertyChestType.SINGLE) {
                    EnumDirection enumdirection1 = (EnumDirection) iblockdata.get(BlockChest.FACING);

                    if (enumdirection.k() != enumdirection1.k() && enumdirection1 == iblockdata1.get(BlockChest.FACING)) {
                        BlockPropertyChestType blockpropertychesttype = enumdirection == enumdirection1.e() ? BlockPropertyChestType.LEFT : BlockPropertyChestType.RIGHT;

                        generatoraccess.setTypeAndData(blockposition1, (IBlockData) iblockdata1.set(BlockChest.b, blockpropertychesttype.a()), 18);
                        if (enumdirection1 == EnumDirection.NORTH || enumdirection1 == EnumDirection.EAST) {
                            TileEntity tileentity = generatoraccess.getTileEntity(blockposition);
                            TileEntity tileentity1 = generatoraccess.getTileEntity(blockposition1);

                            if (tileentity instanceof TileEntityChest && tileentity1 instanceof TileEntityChest) {
                                TileEntityChest.a((TileEntityChest) tileentity, (TileEntityChest) tileentity1);
                            }
                        }

                        return (IBlockData) iblockdata.set(BlockChest.b, blockpropertychesttype);
                    }
                }

                return iblockdata;
            }
        },
        LEAVES(true, new Block[] { Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES}) {
            private final ThreadLocal<List<ObjectSet<BlockPosition>>> g = ThreadLocal.withInitial(() -> {
                return Lists.newArrayListWithCapacity(7);
            });

            public IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
                IBlockData iblockdata2 = iblockdata.updateState(enumdirection, generatoraccess.getType(blockposition1), generatoraccess, blockposition, blockposition1);

                if (iblockdata != iblockdata2) {
                    int i = (Integer) iblockdata2.get(BlockProperties.ab);
                    List<ObjectSet<BlockPosition>> list = (List) this.g.get();

                    if (list.isEmpty()) {
                        for (int j = 0; j < 7; ++j) {
                            list.add(new ObjectOpenHashSet());
                        }
                    }

                    ((ObjectSet) list.get(i)).add(blockposition.h());
                }

                return iblockdata;
            }

            public void a(GeneratorAccess generatoraccess) {
                BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
                List<ObjectSet<BlockPosition>> list = (List) this.g.get();

                for (int i = 2; i < list.size(); ++i) {
                    int j = i - 1;
                    ObjectSet<BlockPosition> objectset = (ObjectSet) list.get(j);
                    ObjectSet<BlockPosition> objectset1 = (ObjectSet) list.get(i);
                    ObjectIterator objectiterator = objectset.iterator();

                    while (objectiterator.hasNext()) {
                        BlockPosition blockposition = (BlockPosition) objectiterator.next();
                        IBlockData iblockdata = generatoraccess.getType(blockposition);

                        if ((Integer) iblockdata.get(BlockProperties.ab) >= j) {
                            generatoraccess.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockProperties.ab, j), 18);
                            if (i != 7) {
                                EnumDirection[] aenumdirection = null.f;
                                int k = aenumdirection.length;

                                for (int l = 0; l < k; ++l) {
                                    EnumDirection enumdirection = aenumdirection[l];

                                    blockposition_mutableblockposition.g(blockposition).c(enumdirection);
                                    IBlockData iblockdata1 = generatoraccess.getType(blockposition_mutableblockposition);

                                    if (iblockdata1.b(BlockProperties.ab) && (Integer) iblockdata.get(BlockProperties.ab) > i) {
                                        objectset1.add(blockposition_mutableblockposition.h());
                                    }
                                }
                            }
                        }
                    }
                }

                list.clear();
            }
        },
        STEM_BLOCK(new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM}) {
            public IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
                if ((Integer) iblockdata.get(BlockStem.AGE) == 7) {
                    BlockStemmed blockstemmed = ((BlockStem) iblockdata.getBlock()).e();

                    if (iblockdata1.getBlock() == blockstemmed) {
                        return (IBlockData) blockstemmed.e().getBlockData().set(BlockFacingHorizontal.FACING, enumdirection);
                    }
                }

                return iblockdata;
            }
        };

        public static final EnumDirection[] f = EnumDirection.values();

        private Type(Block... ablock) {
            this(false, ablock);
        }

        private Type(boolean flag, Block... ablock) {
            Block[] ablock1 = ablock;
            int i = ablock.length;

            for (int j = 0; j < i; ++j) {
                Block block = ablock1[j];

                ChunkConverter.f.put(block, this);
            }

            if (flag) {
                ChunkConverter.g.add(this);
            }

        }
    }

    public interface a {

        IBlockData a(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1);

        default void a(GeneratorAccess generatoraccess) {}
    }
}
