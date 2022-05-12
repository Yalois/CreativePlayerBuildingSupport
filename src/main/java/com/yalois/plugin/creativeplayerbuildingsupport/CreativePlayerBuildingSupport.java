package com.yalois.plugin.creativeplayerbuildingsupport;

import com.yalois.plugin.creativeplayerbuildingsupport.Listener.InvListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public final class CreativePlayerBuildingSupport extends JavaPlugin {
    //TODO 这是我写过的最美观 最清楚 而且还有注释 的代码了 --Yalois
    public static ArrayList<ItemStack> item_data_s;
    File f=new File(getDataFolder()+"/config.yml");


    /*
    设置
     */
    public static boolean allowFlight;
    public static ArrayList<String>  allowWorlds;
    @Override
    public void onEnable() {
        //TODO 插件介绍 状态信息
        getLogger().info("[建筑玩家建筑支持]加载完毕!");
        getLogger().info("作者:Yalois");
        getLogger().info("作者QQ:2051703348");
        getLogger().info("爱发电:§ehttps://afdian.net/@qx233");

        if(f.exists()==false)
        {
            saveDefaultConfig();
        }
        allowFlight=getConfig().getBoolean("allowFlight",true);
        item_data_s= (ArrayList<ItemStack>) getConfig().getList("ItemData",new ArrayList<>());
        allowWorlds=(ArrayList<String>) getConfig().getList("allowWorlds",new ArrayList<>());

        ToLogPluginSomeSetting();
        // Plugin startup logic
        registerEventListener();
    }
    //保存数据到本地
    public void saveDataToLocalSystem()
    {
        getConfig().set("ItemData",item_data_s);
        getConfig().set("allowWorlds",allowWorlds);
        getConfig().set("allowFlight",allowFlight);
        saveConfig();
        reloadConfig();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDataToLocalSystem();
    }
    public void registerEventListener()
    {
        Bukkit.getPluginManager().registerEvents(new InvListener(),this);
    }
    public void openMainMenuForPlayer(String name)
    {
        Inventory v=Bukkit.createInventory(null,9, ChatColor.RED+"建筑玩家中心菜单");

        ItemStack CK=new ItemStack(Material.CHEST);
        ItemMeta im_ck=CK.getItemMeta();
        im_ck.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"建材仓库");
        CK.setItemMeta(im_ck);
        v.setItem(3,CK);
        ItemStack head=new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm= (SkullMeta) head.getItemMeta();
        sm.setOwningPlayer(Bukkit.getPlayer(name));
        sm.setDisplayName(ChatColor.GREEN+"你好!"+name);
        head.setItemMeta(sm);
        v.setItem(4,head);
        if(allowFlight==true) {
        ItemStack flyState=new ItemStack(Material.ELYTRA);
            ItemMeta imf = flyState.getItemMeta();
            imf.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            imf.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "飞行状态 | 发光代表正在飞行状态");
            imf.setLore(Arrays.asList(ChatColor.BLUE + "点击切换飞行状态"));
            if (Bukkit.getPlayer(name).isFlying()) {
                imf.addEnchant(Enchantment.PROTECTION_FALL, 1, true);
            }
            flyState.setItemMeta(imf);
            v.setItem(5, flyState);
        }else{
            ItemStack flyState=new ItemStack(Material.ELYTRA);
            ItemMeta imf = flyState.getItemMeta();
            imf.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            imf.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "管理员未开启飞行");
            flyState.setItemMeta(imf);
            v.setItem(5, flyState);
        }
        Player p=Bukkit.getPlayer(name);
        p.closeInventory();
        p.openInventory(v);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("cpm"))
        {
            if(sender instanceof Player){
                Player p= (Player) sender;
                if(checkPlayerIsCreativePlayer(sender.getName())){
                    if(checkWorldisAllow(p.getWorld().getName())){
                    openMainMenuForPlayer(p.getName());
                    }else{
                        p.setAllowFlight(false);
                        p.sendMessage("当前世界未开放建筑玩家权限!");
                    }

                }
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("cpma"))
        {
            if(sender instanceof Player)
            {
                Player p= (Player) sender;
                if(p.hasPermission("cpma.use"))
                {
                    switch (args.length){
                        case 0:
                            p.sendMessage("管理员你好！这里是本插件的管理指令介绍",
                                    "/cpma help - 查看本帮助",
                                    "/cpma add - 将手持的材料作为建材提供给玩家",
                                    "/cpma addworld [worldname] - 添加建筑玩家启用世界",
                                    "/cpma remove [建材id] - 移除某个建材",
                                    "/cpma switchFly - 允许建筑玩家飞行权限切换",
                                    "/cpma setPlayer [name] [true/false] - 设置某玩家是否为建筑玩家",
                                    "/cpma reload - 重载本地配置文件"
                                    );
                            break;
                        case 1:
                            String com=args[0];
                            if(com.equalsIgnoreCase("help"))
                            {
                                p.sendMessage("管理员你好！这里是本插件的管理指令介绍",
                                        "/cpma help - 查看本帮助",
                                        "/cpma add - 将手持的材料作为建材提供给玩家",
                                        "/cpma addworld [worldname] - 添加建筑玩家启用世界",
                                        "/cpma remove [建材id] - 移除某个建材",
                                        "/cpma switchFly - 允许建筑玩家飞行权限切换",
                                        "/cpma setPlayer [name] [true/false] - 设置某玩家是否为建筑玩家",
                                        "/cpma reload - 重载本地配置文件");
                            }else if(com.equalsIgnoreCase("add"))
                            {
                                // 添加到建材库
                                ItemStack is=p.getEquipment().getItem(EquipmentSlot.HAND);
                                if(is!=null&&is.getType()!= Material.AIR)
                                {
                                    item_data_s.add(Tools.changeInListSave(is));
                                    p.sendMessage("添加["+is.getType()+"]成功!");
                                    saveDataToLocalSystem();
                                }else{
                                    p.sendMessage("您手上为空!");
                                }
                            }else if(com.equalsIgnoreCase("reload"))
                            {
                                // 重载 1.打印重载后的状态信息 2.加载重载信息
                                reloadConfig();
                                item_data_s= (ArrayList<ItemStack>) getConfig().getList("ItemData",new ArrayList<>());
                                allowWorlds=(ArrayList<String>) getConfig().getList("allowWorlds",new ArrayList<>());
                                allowFlight=getConfig().getBoolean("allowFlight",true);
                                ToLogPluginSomeSetting();
                            }else if(com.equalsIgnoreCase("switchFly"))
                            {
                                p.sendMessage((allowFlight?"使建筑玩家没有飞行权限":"使建筑玩家拥有飞行权限"));
                                if(allowFlight==true) {
                                    allowFlight = false;
                                }else {
                                    allowFlight=true;
                                }
                                saveDataToLocalSystem();
                            }
                            break;
                        case 2:
                            if(args[0].equalsIgnoreCase("remove")&&isNumeric1(args[1])){
                                // 检测是否有这个条目 然后进行移除
                                int idx_l=Integer.valueOf(args[1]);
                                if(idx_l>=0&&item_data_s.size()>idx_l)
                                {
                                    item_data_s.remove(idx_l);
                                    p.sendMessage("删除完毕!");
                                }else{
                                    sender.sendMessage("这个id不存在!");
                                }
                            }
                            if(args[0].equalsIgnoreCase("addworld")){
                                CreativePlayerBuildingSupport.allowWorlds.add(args[1]);
                                p.sendMessage("已将世界["+args[1]+"]添加");
                                saveDataToLocalSystem();
                            }
                            break;
                        case 3:
                            if(args[0].equalsIgnoreCase("setPlayer")){
                                String pname=args[1];
                                boolean set_result=false;
                                if(args[2].equalsIgnoreCase("true"))
                                {
                                    set_result=true;
                                }else if(args[2].equalsIgnoreCase("false"))
                                {
                                    set_result=false;
                                }
                                getConfig().set("Players."+pname,set_result);
                                saveConfig();
                                reloadConfig();
                                p.sendMessage("成功将玩家["+pname+"]"+(set_result?"设置为建筑玩家":"设置为非建筑玩家"));
                            }
                            break;
                        default:
                            p.sendMessage("管理员你好！这里是本插件的管理指令介绍",
                                    "/cpma help - 查看本帮助",
                                    "/cpma add - 将手持的材料作为建材提供给玩家",
                                    "/cpma addworld [worldname] - 添加建筑玩家启用世界",
                                    "/cpma remove [建材id] - 移除某个建材",
                                    "/cpma switchFly - 允许建筑玩家飞行权限切换",
                                    "/cpma setPlayer [name] [true/false] - 设置某玩家是否为建筑玩家",
                                    "/cpma reload - 重载本地配置文件");
                            break;
                    }
                }
            }
            return true;
        }
        return false;
    }
    //检测字符串是否可以变成数字
    public static boolean isNumeric1(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    private boolean checkPlayerIsCreativePlayer(String name)
    {
        boolean result=false;
        result=getConfig().getBoolean("Players."+name,false);
        return result;
    }
    private boolean checkWorldisAllow(String worldname)
    {
        boolean result=false;
        for(String w:allowWorlds)
        {
            if(w.equalsIgnoreCase(worldname))
            {
                result=true;
                break;
            }
        }
        return result;
    }
    //输出插件状态信息
    public void ToLogPluginSomeSetting()
    {
        getLogger().info("[建筑玩家建筑支持]插件已经加载成功!");
        getLogger().info("是否允许建筑玩家飞行:"+(allowFlight?"是":"否"));
        getLogger().info("允许玩家进行创造性建筑的的世界:");
        for(String n:allowWorlds)
        {
            getLogger().info(n);
        }
    }
}
