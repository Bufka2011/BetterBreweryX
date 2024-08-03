package com.dre.brewery.storage;

import com.dre.brewery.BCauldron;
import com.dre.brewery.BIngredients;
import com.dre.brewery.Barrel;
import com.dre.brewery.utility.BoundingBox;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.UUID;

public class FlatFileStorage implements DataManager {

    File rawFile = new File(plugin.getDataFolder(), "worlddata.yml");
    YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(rawFile);


    private void save() {
        try {
            dataFile.save(rawFile);
        } catch (Exception e) {
            plugin.errorLog("Failed to save worlddata.yml");
            e.printStackTrace();
        }
    }

    @Override
    public Barrel getBarrel(UUID id, boolean async) {
        String path = "barrels." + id;

        Block spigot = deserializeLocation(dataFile.getString(path + ".spigot")).getBlock();
        BoundingBox bounds = BoundingBox.fromPoints(dataFile.getIntegerList(path + ".bounds"));
        float time = (float) dataFile.getDouble(path + ".time", 0.0);
        byte sign = (byte) dataFile.getInt(path + ".sign", 0);
        ItemStack[] items = BukkitSerialization.itemStackArrayFromBase64(dataFile.getString(path + ".items", null));


        return new Barrel(spigot, sign, bounds, items, time, async, id);
    }

    @Override
    public void saveBarrel(Barrel barrel) {
        String path = "barrels." + barrel.getId();

        dataFile.set(path + ".spigot", serializeLocation(barrel.getSpigot().getLocation()));
        dataFile.set(path + ".bounds", barrel.getBody().getBounds().serialize());
        dataFile.set(path + ".time", barrel.getTime());
        dataFile.set(path + ".sign", barrel.getBody().getSignoffset());
        dataFile.set(path + ".items", BukkitSerialization.itemStackArrayToBase64(barrel.getInventory().getContents()));
        save();
    }

    @Override
    public void deleteBarrel(Barrel barrel) {
        dataFile.set("barrels." + barrel.getId(), null);
        save();
    }

    @Override
    public BCauldron getCauldron(UUID id) {
        String path = "cauldrons." + id;

        Block block = deserializeLocation(dataFile.getString(path + ".block")).getBlock();
        BIngredients ingredients = BIngredients.deserializeIngredients(dataFile.getString(path + ".ingredients"));
        int state = dataFile.getInt(path + ".state", 0);

        return new BCauldron(block, ingredients, state, id);
    }

    @Override
    public void saveCauldron(BCauldron cauldron) {
        String path = "cauldrons." + cauldron.getId();

        dataFile.set(path + ".block", serializeLocation(cauldron.getBlock().getLocation()));
        dataFile.set(path + ".ingredients", cauldron.getIngredients().serializeIngredients());
        dataFile.set(path + ".state", cauldron.getState());
        save();
    }

    @Override
    public void deleteCauldron(BCauldron cauldron) {
        dataFile.set("cauldrons." + cauldron.getId(), null);
        save();
    }
}