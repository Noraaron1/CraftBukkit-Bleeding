package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class ItemInWorldManager {

    private World b;
    public EntityHuman a;
    public float c = 0.0F; // CraftBukkit private -> public
    private int d;
    private int e = 0;
    private float f = 0.0F;
    private int g;
    private int h;
    private int i;
    private int j;
    private boolean k;
    private int l;
    private int m;
    private int n;
    private int o;

    public ItemInWorldManager(World world) {
        this.b = world;
    }

    public void a() {
        ++this.j;
        if (this.k) {
            int i = this.j - this.o;
            int j = this.b.getTypeId(this.l, this.m, this.n);

            if (j != 0) {
                Block block = Block.byId[j];
                float f = block.a(this.a) * (float) (i + 1);

                if (f >= 1.0F) {
                    this.k = false;
                    this.d(this.l, this.m, this.n);
                }
            } else {
                this.k = false;
            }
        }
    }

    public void a(int i, int j, int k) {
        this.d = this.j;
        int l = this.b.getTypeId(i, j, k);

        PlayerInteractEvent event = canInterect(Action.LEFT_CLICK_BLOCK, a, b, a.inventory.b(), i, j, k, l, -1);
        if(event.useItemInHand() != Event.Result.DENY) {
            Block.byId[l].b(this.b, i, j, k, this.a);
        }

        if (l > 0 && Block.byId[l].a(this.a) >= 1.0F) {
            this.d(i, j, k);
        } else {
            this.g = i;
            this.h = j;
            this.i = k;
        }
    }

    public void b(int i, int j, int k) {
        if (i == this.g && j == this.h && k == this.i) {
            int l = this.j - this.d;
            int i1 = this.b.getTypeId(i, j, k);

            if (i1 != 0) {
                Block block = Block.byId[i1];
                float f = block.a(this.a) * (float) (l + 1);

                if (f >= 1.0F) {
                    this.d(i, j, k);
                } else if (!this.k) {
                    this.k = true;
                    this.l = i;
                    this.m = j;
                    this.n = k;
                    this.o = this.d;
                }
            }
        }

        this.c = 0.0F;
        this.e = 0;
    }

    public boolean c(int i, int j, int k) {
        Block block = Block.byId[this.b.getTypeId(i, j, k)];
        int l = this.b.getData(i, j, k);
        boolean flag = this.b.e(i, j, k, 0);

        if (block != null && flag) {
            block.b(this.b, i, j, k, l);
        }

        return flag;
    }

    public boolean d(int i, int j, int k) {
        // CraftBukkit start
        if (this.a instanceof EntityPlayer) {
            CraftServer server = ((WorldServer) this.b).getServer();
            org.bukkit.block.Block block = ((WorldServer) this.b).getWorld().getBlockAt(i, j, k);
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) this.a.getBukkitEntity();

            BlockBreakEvent event = new BlockBreakEvent(block,player);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
        }
        // CraftBukkit end

        int l = this.b.getTypeId(i, j, k);
        int i1 = this.b.getData(i, j, k);
        boolean flag = this.c(i, j, k);
        ItemStack itemstack = this.a.z();

        if (itemstack != null) {
            itemstack.a(l, i, j, k);
            if (itemstack.count == 0) {
                itemstack.a(this.a);
                this.a.A();
            }
        }

        if (flag && this.a.b(Block.byId[l])) {
            Block.byId[l].a_(this.b, i, j, k, i1);
            ((EntityPlayer) this.a).a.b((Packet) (new Packet53BlockChange(i, j, k, this.b)));
        }

        return flag;
    }

    public boolean a(EntityHuman entityhuman, World world, ItemStack itemstack) {
        int i = itemstack.count;
        ItemStack itemstack1 = itemstack.a(world, entityhuman);

        if (itemstack1 == itemstack && (itemstack1 == null || itemstack1.count == i)) {
            return false;
        } else {
            entityhuman.inventory.a[entityhuman.inventory.c] = itemstack1;
            if (itemstack1.count == 0) {
                entityhuman.inventory.a[entityhuman.inventory.c] = null;
            }

            return true;
        }
    }

    // CraftBukkit start - Interact
    public boolean a(EntityHuman entityhuman, World world, ItemStack itemstack, int i, int j, int k, int l) {
        int i1 = world.getTypeId(i, j, k);
        boolean result = false;
        if(i > 0) {
            PlayerInteractEvent event = canInterect(Action.RIGHT_CLICK_BLOCK, entityhuman, world, itemstack, i, j, k, i1, l);
            if(event.useInteractedBlock() != Event.Result.DENY) result = Block.byId[i1].a(world, i, j, k, entityhuman);
            if(event.useItemInHand() != Event.Result.DENY && (!result || event.useItemInHand() == Event.Result.ALLOW))
                result = itemstack.a(entityhuman, world, i, j, k, l);
        }
        return result;
    }

    public PlayerInteractEvent canInterect(Action action, EntityHuman entityhuman, World world, ItemStack itemstack, int i, int j, int k, int id, int face) {
        if (id <= 0) return null;
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer server = ((WorldServer) world).getServer();
        Event.Type eventType = Event.Type.PLAYER_INTERACT;
        CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
        LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

        PlayerInteractEvent event = new PlayerInteractEvent(eventType, action, new CraftItemStack(itemstack), block, CraftBlock.notchToBlockFace(face), who instanceof Player ? (Player)who : null);
        server.getPluginManager().callEvent(event);

        if (event.useInteractedBlock() == Event.Result.DENY) {
            if (entityhuman instanceof EntityPlayer && id == org.bukkit.Material.WOODEN_DOOR.getId()) {
                ((EntityPlayer)entityhuman).a.b((Packet) (new Packet53BlockChange(i, j + ((l & 8) != 0 ? -1 : 1), k, world)));
            }
        }
        return event;
    }
    
    // CraftBukkit end
}
