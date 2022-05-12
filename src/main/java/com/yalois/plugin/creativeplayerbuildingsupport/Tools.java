package com.yalois.plugin.creativeplayerbuildingsupport;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Tools {
    public static ItemStack changeInListSave(ItemStack s)
    {
        ItemStack ss=new ItemStack(s.getType());
        ItemMeta im=ss.getItemMeta();
        //im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        //im.setLore(Arrays.asList(ChatColor.RED+""+ChatColor.BOLD+"建筑方块"));
        ss.setItemMeta(im);

        return ss;
    }
    public static ItemStack ShowInInv(ItemStack s,int id)
    {
        ItemStack ss=new ItemStack(s.getType());
        ItemMeta im=ss.getItemMeta();

        im.setLore(Arrays.asList(ChatColor.RED+""+ChatColor.BOLD+"建筑方块",ChatColor.BLUE+"建材id:"+id));
        ss.setItemMeta(im);

        return ss;
    }
    public static ItemStack OutGet(ItemStack s,int amount)
    {
        ItemStack ss=new ItemStack(s.getType());
        ss.setAmount(amount);
        ItemMeta im=ss.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.setLore(Arrays.asList(ChatColor.RED+""+ChatColor.BOLD+"建筑方块"));
        ss.setItemMeta(im);
        return ss;
    }

}
