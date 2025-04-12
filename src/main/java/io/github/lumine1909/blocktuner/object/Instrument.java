package io.github.lumine1909.blocktuner.object;

import com.google.common.collect.ImmutableMap;
import io.github.lumine1909.blocktuner.util.InstrumentUtil;
import org.bukkit.Material;

import java.util.Map;

public enum Instrument {
    HARP("harp", org.bukkit.Instrument.PIANO, Material.GRASS_BLOCK),
    BASS("bass", org.bukkit.Instrument.BASS_GUITAR, Material.OAK_PLANKS),
    SNARE("snare", org.bukkit.Instrument.SNARE_DRUM, Material.SAND),
    HAT("hat", org.bukkit.Instrument.STICKS, Material.GLASS),
    BASEDRUM("basedrum", org.bukkit.Instrument.BASS_DRUM, Material.STONE),
    BELL("bell", org.bukkit.Instrument.BELL, Material.GOLD_BLOCK),
    FLUTE("flute", org.bukkit.Instrument.FLUTE, Material.CLAY),
    CHIME("chime", org.bukkit.Instrument.CHIME, Material.PACKED_ICE),
    GUITAR("guitar", org.bukkit.Instrument.GUITAR, Material.WHITE_WOOL),
    XYLOPHONE("xylophone", org.bukkit.Instrument.XYLOPHONE, Material.BONE_BLOCK),
    IRON_XYLOPHONE("iron_xylophone", org.bukkit.Instrument.IRON_XYLOPHONE, Material.IRON_BLOCK),
    COW_BELL("cow_bell", org.bukkit.Instrument.COW_BELL, Material.SOUL_SAND),
    DIDGERIDOO("didgeridoo", org.bukkit.Instrument.DIDGERIDOO, Material.PUMPKIN),
    BIT("bit", org.bukkit.Instrument.BIT, Material.EMERALD_BLOCK),
    BANJO("banjo", org.bukkit.Instrument.BANJO, Material.HAY_BLOCK),
    PLING("pling", org.bukkit.Instrument.PLING, Material.GLOWSTONE),
    ZOMBIE("zombie", org.bukkit.Instrument.ZOMBIE, Material.ZOMBIE_HEAD),
    SKELETON("skeleton", org.bukkit.Instrument.SKELETON, Material.SKELETON_SKULL),
    CREEPER("creeper", org.bukkit.Instrument.CREEPER, Material.CREEPER_HEAD),
    DRAGON("dragon", org.bukkit.Instrument.DRAGON, Material.DRAGON_HEAD),
    WITHER_SKELETON("wither_skeleton", org.bukkit.Instrument.WITHER_SKELETON, Material.WITHER_SKELETON_SKULL),
    PIGLIN("piglin", org.bukkit.Instrument.PIGLIN, Material.PIGLIN_HEAD),
    DEFAULT("default", org.bukkit.Instrument.CUSTOM_HEAD, Material.CRAFTING_TABLE),
    EMPTY("empty", org.bukkit.Instrument.CUSTOM_HEAD, Material.AIR);

    public static final Map<org.bukkit.Instrument, Instrument> INSTRUMENT_MAP;
    public static final Map<Material, Instrument> MATERIAL_MAP;
    public static final Map<String, Instrument> NAME_MAP;

    static {
        ImmutableMap.Builder<org.bukkit.Instrument, Instrument> instrumentBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Material, Instrument> materialBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, Instrument> nameBuilder = ImmutableMap.builder();
        for (Instrument instrument : Instrument.values()) {
            nameBuilder.put(instrument.getName(), instrument);
            if (instrument.getBukkitInstrument() != org.bukkit.Instrument.CUSTOM_HEAD) {
                instrumentBuilder.put(instrument.getBukkitInstrument(), instrument);
                materialBuilder.put(instrument.getMaterial(), instrument);
            }
        }
        INSTRUMENT_MAP = instrumentBuilder.build();
        MATERIAL_MAP = materialBuilder.build();
        NAME_MAP = nameBuilder.build();
    }

    private final String name;
    private final org.bukkit.Instrument bukkitInstrument;
    private final Material material;

    Instrument(String name, org.bukkit.Instrument bukkitInstrument, Material material) {
        this.name = name;
        this.bukkitInstrument = bukkitInstrument;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public org.bukkit.Instrument getBukkitInstrument() {
        return bukkitInstrument;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return InstrumentUtil.getName(this);
    }
}