package zairus.megaloot.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zairus.megaloot.MegaLoot;
import zairus.megaloot.loot.LootItemHelper;
import zairus.megaloot.loot.LootSet;
import zairus.megaloot.loot.LootWeaponEffect;

public class MLItemArmor extends ItemArmor
{
	protected MLItemArmor(EntityEquipmentSlot equipmentSlot)
	{
		super(ArmorMaterial.DIAMOND, 3, equipmentSlot);
		
		this.setCreativeTab(MegaLoot.creativeTabMain);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
	}
	
	@SideOnly(Side.CLIENT)
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default)
	{
		String lootSetId = LootItemHelper.getLootStringValue(itemStack, MLItem.LOOT_TAG_LOOTSET);
		
		return LootSet.getArmorModel(LootSet.getById(lootSetId), MLItems.getItemType(itemStack.getItem()));
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		String lootSetId = LootItemHelper.getLootStringValue(stack, MLItem.LOOT_TAG_LOOTSET);
		String textureString = "megaloot:textures/models/armor/1.png";
		
		if (lootSetId != null && lootSetId.length() > 0)
			textureString = "megaloot:textures/models/armor/" + lootSetId + ".png";
		
		return textureString;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> modifiers = MLItem.modifiersForStack(slot, stack, super.getAttributeModifiers(slot, stack));
		
		if (this.getEquipmentSlot() == slot)
		{
			List<LootWeaponEffect> effects = LootWeaponEffect.getEffectList(stack);
			
			for (LootWeaponEffect effect : effects)
			{
				if (effect.getAttribute() != null)
				{
					modifiers.removeAll(effect.getAttribute().getName());
					
					modifiers.put(effect.getAttribute().getName(), new AttributeModifier(new UUID(0, 318145), "Armor modifier", (double)LootWeaponEffect.getAmplifierFromStack(stack, effect.getId()), 0));
				}
			}
		}
		
		return modifiers;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("");
		tooltip.add(TextFormatting.GRAY + "Armor set " + TextFormatting.BOLD + "" + LootItemHelper.getLootStringValue(stack, MLItem.LOOT_TAG_LOOTSET));
		tooltip.add("");
		
		LootItemHelper.addInformation(stack, tooltip, false);
	}
}