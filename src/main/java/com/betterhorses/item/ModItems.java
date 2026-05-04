package com.betterhorses.item;

import com.betterhorses.BetterHorses;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


/**
 * Standard class for registering mod items
 *
 * @see HorseboxItem
 * @see BreedingChartItem
 */
public class ModItems {

    public static final Item HORSEBOX = registerItem("horsebox", new HorseboxItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item BREEDING_CHART = registerItem("breeding_chart", new BreedingChartItem(new Item.Settings().maxCount(1)));
    public static final Item NETHERITE_HORSE_ARMOR = registerItem("netherite_horse_armor",
            new AnimalArmorItem(ArmorMaterials.NETHERITE, AnimalArmorItem.Type.EQUESTRIAN, false, new Item.Settings().maxCount(1).fireproof()) {
                @Override
                public Identifier getEntityTexture() {
                    return Identifier.of(BetterHorses.MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png");
                }
            }
    );
    public static final Item HORSESHOE = registerItem("horseshoe", new HorseshoeItem(
                    new Item.Settings()
                            .maxCount(1)
                            .attributeModifiers(AttributeModifiersComponent.builder().add(
                                    EntityAttributes.GENERIC_JUMP_STRENGTH,
                                    new EntityAttributeModifier(BetterHorses.identifier("horseshoe"), 0.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                                    AttributeModifierSlot.BODY
                            ).build())
                            .maxDamage(3000)
                            .equipmentSlot((entity, stack) -> EquipmentSlot.BODY)
            )
    );

    public static void registerItems() {
        BetterHorses.LOGGER.info("Registering items for " + BetterHorses.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.SADDLE, HORSESHOE);
            entries.addAfter(HORSESHOE, HORSEBOX);
            entries.addAfter(HORSEBOX, BREEDING_CHART);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.DIAMOND_HORSE_ARMOR, NETHERITE_HORSE_ARMOR);
        });
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(BetterHorses.MOD_ID, name), item);
    }
}
