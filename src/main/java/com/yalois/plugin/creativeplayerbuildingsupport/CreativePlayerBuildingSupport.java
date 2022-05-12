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
    //TODO ������д���������� ����� ���һ���ע�� �Ĵ����� --Yalois
    public static ArrayList<ItemStack> item_data_s;
    File f=new File(getDataFolder()+"/config.yml");


    /*
    ����
     */
    public static boolean allowFlight;
    public static ArrayList<String>  allowWorlds;
    @Override
    public void onEnable() {
        //TODO ������� ״̬��Ϣ
        getLogger().info("[������ҽ���֧��]�������!");
        getLogger().info("����:Yalois");
        getLogger().info("����QQ:2051703348");
        getLogger().info("������:��ehttps://afdian.net/@qx233");

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
    //�������ݵ�����
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
        Inventory v=Bukkit.createInventory(null,9, ChatColor.RED+"����������Ĳ˵�");

        ItemStack CK=new ItemStack(Material.CHEST);
        ItemMeta im_ck=CK.getItemMeta();
        im_ck.setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"���Ĳֿ�");
        CK.setItemMeta(im_ck);
        v.setItem(3,CK);
        ItemStack head=new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm= (SkullMeta) head.getItemMeta();
        sm.setOwningPlayer(Bukkit.getPlayer(name));
        sm.setDisplayName(ChatColor.GREEN+"���!"+name);
        head.setItemMeta(sm);
        v.setItem(4,head);
        if(allowFlight==true) {
        ItemStack flyState=new ItemStack(Material.ELYTRA);
            ItemMeta imf = flyState.getItemMeta();
            imf.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            imf.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "����״̬ | ����������ڷ���״̬");
            imf.setLore(Arrays.asList(ChatColor.BLUE + "����л�����״̬"));
            if (Bukkit.getPlayer(name).isFlying()) {
                imf.addEnchant(Enchantment.PROTECTION_FALL, 1, true);
            }
            flyState.setItemMeta(imf);
            v.setItem(5, flyState);
        }else{
            ItemStack flyState=new ItemStack(Material.ELYTRA);
            ItemMeta imf = flyState.getItemMeta();
            imf.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            imf.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "����Աδ��������");
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
                        p.sendMessage("��ǰ����δ���Ž������Ȩ��!");
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
                            p.sendMessage("����Ա��ã������Ǳ�����Ĺ���ָ�����",
                                    "/cpma help - �鿴������",
                                    "/cpma add - ���ֳֵĲ�����Ϊ�����ṩ�����",
                                    "/cpma addworld [worldname] - ��ӽ��������������",
                                    "/cpma remove [����id] - �Ƴ�ĳ������",
                                    "/cpma switchFly - ��������ҷ���Ȩ���л�",
                                    "/cpma setPlayer [name] [true/false] - ����ĳ����Ƿ�Ϊ�������",
                                    "/cpma reload - ���ر��������ļ�"
                                    );
                            break;
                        case 1:
                            String com=args[0];
                            if(com.equalsIgnoreCase("help"))
                            {
                                p.sendMessage("����Ա��ã������Ǳ�����Ĺ���ָ�����",
                                        "/cpma help - �鿴������",
                                        "/cpma add - ���ֳֵĲ�����Ϊ�����ṩ�����",
                                        "/cpma addworld [worldname] - ��ӽ��������������",
                                        "/cpma remove [����id] - �Ƴ�ĳ������",
                                        "/cpma switchFly - ��������ҷ���Ȩ���л�",
                                        "/cpma setPlayer [name] [true/false] - ����ĳ����Ƿ�Ϊ�������",
                                        "/cpma reload - ���ر��������ļ�");
                            }else if(com.equalsIgnoreCase("add"))
                            {
                                // ��ӵ����Ŀ�
                                ItemStack is=p.getEquipment().getItem(EquipmentSlot.HAND);
                                if(is!=null&&is.getType()!= Material.AIR)
                                {
                                    item_data_s.add(Tools.changeInListSave(is));
                                    p.sendMessage("���["+is.getType()+"]�ɹ�!");
                                    saveDataToLocalSystem();
                                }else{
                                    p.sendMessage("������Ϊ��!");
                                }
                            }else if(com.equalsIgnoreCase("reload"))
                            {
                                // ���� 1.��ӡ���غ��״̬��Ϣ 2.����������Ϣ
                                reloadConfig();
                                item_data_s= (ArrayList<ItemStack>) getConfig().getList("ItemData",new ArrayList<>());
                                allowWorlds=(ArrayList<String>) getConfig().getList("allowWorlds",new ArrayList<>());
                                allowFlight=getConfig().getBoolean("allowFlight",true);
                                ToLogPluginSomeSetting();
                            }else if(com.equalsIgnoreCase("switchFly"))
                            {
                                p.sendMessage((allowFlight?"ʹ�������û�з���Ȩ��":"ʹ�������ӵ�з���Ȩ��"));
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
                                // ����Ƿ��������Ŀ Ȼ������Ƴ�
                                int idx_l=Integer.valueOf(args[1]);
                                if(idx_l>=0&&item_data_s.size()>idx_l)
                                {
                                    item_data_s.remove(idx_l);
                                    p.sendMessage("ɾ�����!");
                                }else{
                                    sender.sendMessage("���id������!");
                                }
                            }
                            if(args[0].equalsIgnoreCase("addworld")){
                                CreativePlayerBuildingSupport.allowWorlds.add(args[1]);
                                p.sendMessage("�ѽ�����["+args[1]+"]���");
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
                                p.sendMessage("�ɹ������["+pname+"]"+(set_result?"����Ϊ�������":"����Ϊ�ǽ������"));
                            }
                            break;
                        default:
                            p.sendMessage("����Ա��ã������Ǳ�����Ĺ���ָ�����",
                                    "/cpma help - �鿴������",
                                    "/cpma add - ���ֳֵĲ�����Ϊ�����ṩ�����",
                                    "/cpma addworld [worldname] - ��ӽ��������������",
                                    "/cpma remove [����id] - �Ƴ�ĳ������",
                                    "/cpma switchFly - ��������ҷ���Ȩ���л�",
                                    "/cpma setPlayer [name] [true/false] - ����ĳ����Ƿ�Ϊ�������",
                                    "/cpma reload - ���ر��������ļ�");
                            break;
                    }
                }
            }
            return true;
        }
        return false;
    }
    //����ַ����Ƿ���Ա������
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
    //������״̬��Ϣ
    public void ToLogPluginSomeSetting()
    {
        getLogger().info("[������ҽ���֧��]����Ѿ����سɹ�!");
        getLogger().info("�Ƿ���������ҷ���:"+(allowFlight?"��":"��"));
        getLogger().info("������ҽ��д����Խ����ĵ�����:");
        for(String n:allowWorlds)
        {
            getLogger().info(n);
        }
    }
}
