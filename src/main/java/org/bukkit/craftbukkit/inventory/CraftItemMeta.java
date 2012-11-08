package org.bukkit.craftbukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class CraftItemMeta implements ItemMeta {
    private String displayName;
    private List<String> lore; // TODO: lore
    private Map<Enchantment, Integer> enchantments; // TODO: enchantments
    private int maxStackSize; // TODO: custom max size

    CraftItemMeta() {}

    CraftItemMeta(CraftItemStack itemstack) {
        // TODO: Set item data
    }

    public CraftItemMeta clone() {
        try {
            return (CraftItemMeta) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    void applyToItem(CraftItemStack item) {
        item.getHandle().c(displayName); // Set display name, creates stuff for us
        // TODO: Apply enchantments
    }

    boolean applicableTo(ItemStack itemstack) {
        return true;
    }

    boolean isEmpty() {
        return !(hasDisplayName() || hasEnchants() || hasLore());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    public boolean hasEnchant(Enchantment ench) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEnchantLevel(Enchantment ench) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<Enchantment, Integer> getEnchants() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeEnchant(Enchantment ench) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasEnchants() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCustomStackSize() {
        return maxStackSize;
    }

    public void setCustomStackSize(int size) {
        maxStackSize = size;
    }

    public boolean hasCustomStackSize() {
        return maxStackSize > 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CraftItemMeta)) {
            return false;
        } else if (this.isEmpty()) {
            return ((CraftItemMeta) object).isEmpty();
        }

        CraftItemMeta objectMeta = (CraftItemMeta) object;

        // Do the display names equal?
        if ((this.hasDisplayName() && !objectMeta.hasDisplayName()) || (this.hasDisplayName() && !this.getDisplayName().equals(objectMeta.getDisplayName()))) {
            return false;
        }

        // Do the enchants equal?
        if ((this.hasEnchants() && !objectMeta.hasEnchants()) || (this.hasEnchants() && !this.getEnchants().equals(objectMeta.getEnchants()))) {
            return false;
        }

        // Does lore match?
        if ((this.hasLore() && !objectMeta.hasLore())) {
            return false;
        }

        // CustomStackSize check
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (this.lore != null ? this.lore.hashCode() : 0);
        hash = 61 * hash + (this.enchantments != null ? this.enchantments.hashCode() : 0);
        // hash = 61 * hash + this.maxStackSize;
        return hash;
    }
}
