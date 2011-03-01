package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
// CraftBukkit end

public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel(int i) {
        super(i);
        this.maxStackSize = 1;
        this.durability = 64;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        // CraftBukkit start - store the clicked block
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer craftServer = ((WorldServer) world).getServer();
        org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
        // CraftBukkit end

        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        int i1 = world.getTypeId(i, j, k);

        if (i1 == 0) {
            // CraftBukkit start - Flint and steel
            Player thePlayer = (Player) entityhuman.getBukkitEntity();

            IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
            BlockIgniteEvent eventIgnite = new BlockIgniteEvent(blockClicked, igniteCause, thePlayer);
            craftServer.getPluginManager().callEvent(eventIgnite);
            boolean preventFire = eventIgnite.isCancelled();

            if (preventFire) {
                itemstack.b(1);
                return false;
            }
            // CraftBukkit end

            world.a((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "fire.ignite", 1.0F, b.nextFloat() * 0.4F + 0.8F);
            world.e(i, j, k, Block.FIRE.id);
        }

        itemstack.b(1);
        return true;
    }
}
