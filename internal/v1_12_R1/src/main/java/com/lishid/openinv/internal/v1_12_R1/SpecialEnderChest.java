/*
 * Copyright (C) 2011-2018 lishid. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.lishid.openinv.internal.v1_12_R1;

import com.lishid.openinv.internal.ISpecialEnderChest;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class SpecialEnderChest implements IInventory, ISpecialEnderChest {

    private EntityPlayer owner;
    private String displayName;
    private NonNullList<ItemStack> items;
    private final InventoryEnderChest enderChest;
    private final CraftInventory inventory = new CraftInventory(this);
    private boolean playerOnline;

    public SpecialEnderChest(final Player player, final Boolean online) {
        this.owner = PlayerDataManager.getHandle(player);
        this.displayName = this.owner.getEnderChest().getName();
        this.playerOnline = online;
        EntityPlayer nmsPlayer = PlayerDataManager.getHandle(player);
        this.enderChest = nmsPlayer.getEnderChest();
    }

    @Override
    public Inventory getBukkitInventory() {
        return this.inventory;
    }

    @Override
    public boolean isInUse() {
        return !this.getViewers().isEmpty();
    }

    @Override
    public void setPlayerOffline() {
        this.playerOnline = false;
    }

    @Override
    public void setPlayerOnline(final Player player) {
        if (!this.playerOnline) {
            try {
                this.owner = PlayerDataManager.getHandle(player);
                InventoryEnderChest enderChest = owner.getEnderChest();
                for (int i = 0; i < enderChest.getSize(); ++i) {
                    enderChest.setItem(i, this.items.get(i));
                }
                this.items = enderChest.items;
            } catch (Exception ignored) {
            }
            this.playerOnline = true;
        }
    }

    @Override
    public void update() {
        this.enderChest.update();
        this.owner.getEnderChest().update();
    }

    public List<ItemStack> getContents() {
        return this.items;
    }

    @Override
    public int getSize() {
        return this.owner.getEnderChest().getSize();
    }

    /**
     * @return isEmpty()
     */
    @Override
    public boolean x_() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.a;
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = ContainerUtil.a(this.items, i, j);
        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        ItemStack itemstack = this.items.get(i);
        if (itemstack.isEmpty()) {
            return ItemStack.a;
        } else {
            this.items.set(i, ItemStack.a);
            return itemstack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.items.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        this.update();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean a(EntityHuman entityHuman) {
        return true;
    }

    @Override
    public void startOpen(EntityHuman entityHuman) {

    }

    @Override
    public void closeContainer(EntityHuman entityHuman) {

    }

    @Override
    public boolean b(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getProperty(int i) {
        return 0;
    }

    @Override
    public void setProperty(int i, int i1) {
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        this.owner.getEnderChest().onOpen(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        this.owner.getEnderChest().onClose(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.owner.getEnderChest().getViewers();
    }

    @Override
    public InventoryHolder getOwner() {
        return this.owner.getEnderChest().getOwner();
    }

    @Override
    public void setMaxStackSize(int i) {
        this.owner.getEnderChest().setMaxStackSize(i);
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public String getName() {
        return this.displayName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return null;
    }
}
