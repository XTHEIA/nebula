package kr.sbxt.xtheia.theia.nebula;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Optional;


public class ServerEventListener implements Listener
{
	@EventHandler
	private void onTryEnchant(PrepareItemEnchantEvent event)
	{
		Optional<MetaItem> _itemOptional_ = ItemManager.isMetaItem(event.getItem());
		if (_itemOptional_.isPresent())
		{
			MetaItem item = _itemOptional_.get();
			if (! item.isCanEnchantTable())
			{
				event.setCancelled(true);
				Location location = event.getEnchantBlock().getLocation();
				Nebula.Log("Blocked enchant event of MetaItem " + ChatColor.AQUA + item.codeName + ChatColor.WHITE + "!",
						ChatColor.WHITE + "Player " + ChatColor.YELLOW + event.getEnchanter().getName() + ChatColor.WHITE +
						", World " + ChatColor.GREEN + location.getWorld().getName() + ChatColor.WHITE +
						", Pos " + ChatColor.GREEN + location.toVector().toBlockVector());
			}
		}
	}
	
	@EventHandler
	private void onDealt(EntityDamageByEntityEvent event)
	{
		Nebula.Log(ChatColor.AQUA + "" + event.getDamage());
	}
	
}
