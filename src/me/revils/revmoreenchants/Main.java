package me.revils.revmoreenchants;


import me.revils.revmoreenchants.enchants.CurrencyFinder;
import me.revils.revmoreenchants.enchants.lol;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {

        try {
            CurrencyFinder.LoadFile();
            lol.LoadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(new CurrencyFinder(), this);
        Bukkit.getPluginManager().registerEvents(new lol(), this);
        // to see the enchants on the pickaxe lore need to add this Placeholder
        // to Lore.yml in RevEnchants file
        // %Rev_Enchant_CurrencyFinder_Colour%%Rev_Enchant_CurrencyFinder_Name% &f%Rev_Enchant_CurrencyFinder_Level%
        // %Rev_Enchant_SayLol_Colour%%Rev_Enchant_SayLol_Name% &f%Rev_Enchant_SayLol_Level%









    }

    @Override
    public void onDisable() {

    }
}
