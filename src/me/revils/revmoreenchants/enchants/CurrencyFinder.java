package me.revils.revmoreenchants.enchants;

import me.revils.revenchants.api.CurrencyReceiveReason;
import me.revils.revenchants.api.RevEnchantsApi;
import me.revils.revenchants.events.ConfigLoadEvent;
import me.revils.revenchants.events.MineBlockEvent;
import me.revils.revenchants.rev.RevTool;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

public class CurrencyFinder implements Listener {

    private static String ID = "CurrencyFinder";

    private static YamlConfiguration EFile;

    private static YamlConfiguration SFile;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMineBlockEvent(MineBlockEvent event){
        RevTool item = event.getTool(); // player PickAxe
        long level = 0;

        /// check if enchantment is enabled in config
        if (EFile.getBoolean("Settings.Enabled",true)){
            /// get player PickAxe enchantment level
            level = item.getEnchant(ID,event.getPlayer());
        }
        /// check if shard is enabled in config for this enchantment
        if (SFile.getBoolean("Settings.Enabled",true)){
            /// get player PickAxe shard level of that  enchantment
            // its up to you if you want to create shard of the enchants
            level = level + item.getShardsLevels(ID,SFile.getLong("Settings.MaxStack",100),event.getPlayer());

        }
        /// check player has 0 levels of the enchantment/shard
        if (level== 0){
            // stop as its 0 levels
            return;
        }

        long blocks =1;

        // this will check if MultiBreak is enabled
        // remove this option if you don't want to support
        //blocks broken by other enchantment like Explosive and JackHammer
        if (EFile.getBoolean("Settings.MultiBreak",true)){
            //get all blocks broken by other enchantment like Explosive and JackHammer
            blocks = event.getMinedBlockAmount();
        }



        // this will get the current level chance
        double Chancce = RevEnchantsApi.getChance(EFile.getDouble("Settings.Chance",1),EFile.getDouble("Settings.Increase-Chance-by",0.1),level);


        // this will get how many successes has this enchants of all blocks that's been broken

        //                             chance  Enchant-id(must be Enchant- and id of the enchants )   Player   Amount of broken blocks
        /// getMultiBreakSuccess(double chance ,String Reason, Player p,long Amount)
        long  success = RevEnchantsApi.getMultiBreakSuccess(Chancce,"Enchant-"+ID,event.getPlayer(),blocks);

        // check if successes > 0
        if (success > 0){
            // this enchantment has success
            // here do whatever you want
            // success will return how many blocks has the enchantment  called on
            // example :
            //      if Explosive broke 20 blocks and one by player pickaxe
            //      total is 21
            //       but the chance of the enchants is success on just 4 using  method getMultiBreakSuccess
            //        success will  = 4
            ///      for example now you can give player tokens for 4 blocks
            ///       Like here
            long Tokens = 10 * success;
            /// CurrencyID  can be a option in config if you want
            RevEnchantsApi.depositCurrency(event.getPlayer(),Tokens,"RevTokens", CurrencyReceiveReason.ENCHANTMENT);

        }

    }

    // this will reload the enchants when you do /re reload
    @EventHandler(priority = EventPriority.LOWEST)
    public void onConfigLoadEvent(ConfigLoadEvent event){
        try {
            LoadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void LoadFile() throws IOException {
        // create  enchants config
        RevEnchantsApi.loadEnchantsFile(ID);
        EFile = RevEnchantsApi.getEnchantsYml(ID);
        if (EFile.get("Settings.Name") == null){
            // configuration must have all this options to work
            // you can add other options if you want and use them
            EFile.set("Settings.Enabled",true);
            EFile.set("Settings.InGui",true);
            EFile.set("Settings.Name","CurrencyFinder");
            EFile.set("Settings.Colour","&7");
            EFile.set("Settings.Slot",3);
            EFile.set("Settings.MaxLevel",1000);
            EFile.set("Settings.MultiBreak",true);
            EFile.set("Settings.MultiBreakMaxSuccess",1000);
            EFile.set("Settings.Currency","RevToken");
            EFile.set("Settings.Cost",10000);
            EFile.set("Settings.Increase-Cost-by",1000);
            EFile.set("Settings.Chance",1);
            EFile.set("Settings.Increase-Chance-by",0.01);
            EFile.set("Settings.Requirement.Enabled",false);
            EFile.set("Settings.Requirement.Number",5);
            EFile.set("Settings.Requirement.Placeholder","%RevEnchants_Tool_Flag_Level%");
            EFile.set("Settings.Description","&7Chance to get tokens key%nl%&7as you mine the chance depend on the level");
            EFile.set("Settings.Book", new ItemStack(Material.BOOK, 1));

            EFile.set("Settings.Message.Enabled",false);// need to do this by yourself
            EFile.set("Settings.Message.Bar",true);
            EFile.set("Settings.Message.Format","&7CurrencyFinder %Amount_FormattedA%");
            RevEnchantsApi.saveEnchantsFile(ID);

        }
        // create  shard config as it will same as the enchantment
        // its up to you if you want to create shard of the enchants
        RevEnchantsApi.loadShardFile(ID);
        SFile = RevEnchantsApi.getShardYml(ID);
        if (SFile.get("Settings.Name") == null){
            // configuration must have all this options to work
            // you can add other options if you want and use them
            SFile.set("Settings.Enabled",true);
            SFile.set("Settings.Name","&cCurrencyFinder");
            SFile.set("Settings.MaxLevel",2);
            SFile.set("Settings.MaxStack",100);
            SFile.set("Settings.Requirement.Enabled",false);
            SFile.set("Settings.Requirement.Number",10);
            SFile.set("Settings.Requirement.Placeholder","%RevEnchants_Tool_Flag_Level%");
            SFile.set("Settings.Description","&7Increase your CurrencyFinder level");
            SFile.set("Settings.Shard.Item", new ItemStack(Material.PRISMARINE_SHARD, 1));
            SFile.set("Settings.Shard.Glow",true);
            SFile.set("Settings.Shard.Name","&cCurrencyFinder + %Level%");
            ArrayList<String> MDede = new ArrayList<String>();
            MDede.add("&1Unique Shard  ");
            MDede.add("&7Increase your pickaxe CurrencyFinder level by %Level%");
            // %Level_Formatted% %Level_Roman%
            SFile.set("Settings.Shard.Lore",MDede);
            RevEnchantsApi.saveShardFile(ID);

        }


    }
}
