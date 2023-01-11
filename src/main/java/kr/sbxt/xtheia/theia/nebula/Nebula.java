package kr.sbxt.xtheia.theia.nebula;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class Nebula extends JavaPlugin
{
	
	public static Nebula plugin;
	public static ConsoleCommandSender logger;
	private static final ServerEventListener SERVER_EVENT_LISTENER = new ServerEventListener();
	
	@Override
	public void onEnable()
	{
		final var plugin = Nebula.plugin = this;
		logger = Bukkit.getServer().getConsoleSender();
		getServer().getPluginManager().registerEvents(SERVER_EVENT_LISTENER, plugin);
		
		final var pluginPath = getDataFolder();
		if (! pluginPath.exists()) pluginPath.mkdir();
		final var presetPath = new File(pluginPath, "presets");
		if (! presetPath.exists())
		{
			presetPath.mkdir();
			final var example = new File(presetPath, "preset-example.yml");
			Log("Trying to Create example preset file...");
			try
			{
				example.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				final var stream = getResource("preset-example.yml");
				OutputStream outputStream = new FileOutputStream(example);
				while (true)
				{
					final var i = stream.read();
					if (i == - 1)
					{
						break;
					}
					else
					{
						outputStream.write(i);
					}
				}
				outputStream.close();
				stream.close();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		
		// Loading Preset files
		final var files = presetPath.listFiles();
		if (files == null || files.length == 0)
		{
			Log("Cannot find any preset file!");
			return;
		}
		for (final var file : files)
		{
			final var fileName = file.getName();
			try
			{
				if (! file.getName().endsWith(".yml"))
				{
					throw new ItemDeserializeException("File is not a yaml file");
				}
				final var configuration = YamlConfiguration.loadConfiguration(file);
				
				final var codeName = configuration.getString(ItemLoader.CODENAME);
				if (codeName == null || codeName.isEmpty())
				{
					throw new ItemDeserializeException("codename empty");
				}
				
				if (! ItemManager.codenameAvailable(codeName))
				{
					throw new ItemDeserializeException("codename already in use or unavailable");
				}
				
				final var item = new MetaItem(codeName, ItemManager.newUid());
				
				item.setDisplayName(configuration.getString(ItemLoader.DISPLAYNAME, "Empty Item Display Name"));
				item.setUnbreakable(configuration.getBoolean(ItemLoader.Unbreakable, false));
				item.setCanEnchantTable(configuration.getBoolean(ItemLoader.AllowEnchantTable, false));
				
				// Material
				final var _matStr_ = configuration.getString(ItemLoader.MATERIAL, "GOLDEN_SWORD");
				final var _material_ = Material.getMaterial(_matStr_);
				if (_material_ == null)
				{
					throw new ItemDeserializeException("material invalid");
				}
				else
				{
					item.setMaterial(_material_);
				}
				
				// Durability Damage
				final var damage = configuration.getInt(ItemLoader.ITEMDAMAGE, 0);
				if (damage < 0)
				{
					throw new ItemDeserializeException("item damage invalid");
				}
				else
				{
					item.setItemDamage(damage);
				}
				
				// Wield Damage
				final var wieldDmgBase = configuration.getDouble(ItemLoader.WieldDamageBase, 1.0);
				if (wieldDmgBase < - 1)
				{
					throw new ItemDeserializeException("item wield damage invalid");
				}
				else
				{
					item.setWieldDamageBase(wieldDmgBase);
				}
				
				// Lores
				final var loresStr = configuration.getStringList(ItemLoader.Lores);
				for (final var lore : loresStr)
				{
					item.addLores(lore);
				}
				
				// Attrs
				final var attrsStr = configuration.getStringList(ItemLoader.AttributeModifiers);
				for (final var attrStr : attrsStr)
				{
					final var attrToken = attrStr.split("/");
					if (attrToken.length != 4)
					{
						throw new ItemDeserializeException("attribute modifier invalid");
					}
					final var _slotStr_ = attrToken[0];
					final var slot = _slotStr_.equals("ALL") ? null : EquipmentSlot.valueOf(_slotStr_);
					final var attribute = Attribute.valueOf(attrToken[1]);
					final var operation = AttributeModifier.Operation.valueOf(attrToken[2]);
					final var value = Double.parseDouble(attrToken[3]);
					item.addAttrModificationList(new AttrModification(attribute, operation, value, slot));
				}
				
				// Flags
				final var flagsStr = configuration.getStringList(ItemLoader.Flags);
				for (final var flagStr : flagsStr)
				{
					final var flag = ItemFlag.valueOf(flagStr);
					item.addFlag(flag);
				}
				
				// Enchants
				final var enchantsStr = configuration.getStringList(ItemLoader.Enchants);
				for (final var enchantStr : enchantsStr)
				{
					
					final var errMsg = ChatColor.RED + "Cannot Parse Enchant [ " + ChatColor.WHITE + enchantStr + ChatColor.RED + " ] :: ";
					
					final var enchantToken = enchantStr.split("/");
					
					if (enchantToken.length != 3)
					{
						throw new ItemDeserializeException("enchant modifier invalid");
					}
					
					final var _enchantmentToken_ = enchantToken[0].toLowerCase(Locale.ROOT);
					final var _enchantment_ = Enchantment.getByKey(NamespacedKey.minecraft(_enchantmentToken_));
					if (_enchantment_ == null)
					{
						throw new ItemDeserializeException("enchant modifier invalid");
					}
					
					try
					{
						final var _levelStr_ = enchantToken[1];
						
						final var _level_ = Integer.parseInt(_levelStr_);
						final var ignore = enchantToken[2].toLowerCase(Locale.ROOT).equals("true");
						item.addEnchant(new Enchant(_enchantment_, _level_, ignore));
					} catch (NumberFormatException e)
					{
						throw new ItemDeserializeException("enchant level invalid");
					} catch (Exception e)
					{
						throw new ItemDeserializeException(e.getClass().getName());
					}
				}
				
				ItemManager.register(item);
				Log("Successfully Loaded " + ChatColor.AQUA + item.codeName + ChatColor.WHITE + "! " +
				    ChatColor.GRAY + "(" + file.getPath() + ")");
				
			} catch (Exception e)
			{
				Log(ChatColor.RED + "Failed to Load " + ChatColor.YELLOW + fileName + ChatColor.GRAY + " (" + e.getMessage() + ")");
			}
		}
		Log("", ChatColor.YELLOW + "Finished indexing every single preset files!", "");
	}
	
	@Override
	public void onDisable()
	{
		// Plugin shutdown logic
	}
	
	@Override
	public void onLoad()
	{
	
	}
	
	public static void Log(String message)
	{
		logger.sendMessage(message);
	}
	
	public static void Log(String messsage1, String... message2s)
	{
		Log(messsage1);
		for (final var message2 : message2s)
		{
			Log(message2);
		}
	}
}
