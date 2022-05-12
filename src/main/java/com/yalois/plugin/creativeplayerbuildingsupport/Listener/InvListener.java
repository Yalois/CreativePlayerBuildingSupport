package com.yalois.plugin.creativeplayerbuildingsupport.Listener;

import com.yalois.plugin.creativeplayerbuildingsupport.CreativePlayerBuildingSupport;
import com.yalois.plugin.creativeplayerbuildingsupport.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

public class InvListener implements Listener {

    @EventHandler
    public void invopen(InventoryClickEvent ICE)
    {
        if(ICE.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW+"建材仓库"))
        {
            ICE.setCancelled(true);
            if(ICE.getRawSlot()==53)
            {
                if(ICE.getInventory().getItem(53)!=null&&ICE.getInventory().getItem(53).hasItemMeta()&&ICE.getInventory().getItem(53).getItemMeta().hasCustomModelData())
                {
                    int nextPage=ICE.getInventory().getItem(53).getItemMeta().getCustomModelData();
                    ICE.getWhoClicked().closeInventory();
                    openCK(Bukkit.getPlayer(ICE.getWhoClicked().getName()),nextPage);
                }
            }
            if(0<=ICE.getRawSlot()&&ICE.getRawSlot()<45)
            {
                ItemStack iss=ICE.getView().getItem(ICE.getRawSlot());
                if(iss!=null&&iss.getType()!=Material.AIR)
                {
                    if(ICE.getClick()== ClickType.LEFT) {
                        ICE.getWhoClicked().getInventory().addItem(Tools.OutGet(iss, 1));
                    }else if(ICE.getClick()== ClickType.RIGHT){
                        ICE.getWhoClicked().getInventory().addItem(Tools.OutGet(iss, 64));
                    }
                }
            }
        }
        if(ICE.getView().getTitle().equalsIgnoreCase(ChatColor.RED+"建筑玩家中心菜单"))
        {
            ICE.setCancelled(true);
            Player p= (Player) ICE.getWhoClicked();
            if(ICE.getRawSlot()==5)
            {
                //飞行状态切换
                if(CreativePlayerBuildingSupport.allowFlight==true) {
                    if (p.isFlying()) {
                        p.closeInventory();
                        p.setAllowFlight(false);
                    } else {
                        p.closeInventory();
                        p.setAllowFlight(true);
                    }
                }
            }
            if(ICE.getRawSlot()==3)
            {
                openCK(p,1);
            }
        }
    }
    public void openCK(Player p,int page){
        p.closeInventory();
        Inventory ck= Bukkit.createInventory(null,54,ChatColor.YELLOW+"建材仓库");
        int total_page= 0;
        if(CreativePlayerBuildingSupport.item_data_s.size()%45==0)
        {
            total_page=CreativePlayerBuildingSupport.item_data_s.size()/45;
        }else {
            total_page=CreativePlayerBuildingSupport.item_data_s.size()/45+1;
        }
        if(page<total_page) {
            ItemStack NEXTPage = new ItemStack(Material.DIAMOND_HORSE_ARMOR);
            ItemMeta nextpage_im = NEXTPage.getItemMeta();
            nextpage_im.setDisplayName(ChatColor.AQUA + "下一页");
            nextpage_im.setCustomModelData(page+1);
            NEXTPage.setItemMeta(nextpage_im);
            ck.setItem(53, NEXTPage);
            for(int i=45;i<53;i++)
            {
                ck.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }else {
            for (int i = 45; i < 54; i++) {
                ck.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
            }
        }
        int startIndex=45*(page-1);
        //int endIndex = 0;
        int endIndex_in_inv = 0;
        if(startIndex+45<=CreativePlayerBuildingSupport.item_data_s.size())
        {
            //endIndex=startIndex+45;
            endIndex_in_inv=45;
        }else{
            //endIndex= CreativePlayerBuildingSupport.item_data_s.size()-1;
            endIndex_in_inv=CreativePlayerBuildingSupport.item_data_s.size()-startIndex;
            //startIndex=45
            //endIndex_in_inv=90-45

        }
        for(int i=0;i<endIndex_in_inv;i++)
        {
            ck.setItem(i, Tools.ShowInInv(CreativePlayerBuildingSupport.item_data_s.get(i+startIndex),i+startIndex));
        }
        p.openInventory(ck);
    }
}
