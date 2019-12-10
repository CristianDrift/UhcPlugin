/*
 * UhcPlugin
 * Copyright (c) 2019  Pau Machetti Vallverd�
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.PauMAVA.UhcPlugin.world;

import java.util.List;

import me.PauMAVA.UhcPlugin.UhcPluginCore;
import me.PauMAVA.UhcPlugin.commands.UhcConfigCmd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UhcWorldBorder {
	
	private static final UhcPluginCore plugin = UhcPluginCore.getInstance();
	private static FileConfiguration config = UhcConfigCmd.getConfig();
	
	public static void refreshBorder(Integer episode) {
		if(!configIsSet()) {
			setConfig();
		}
		if(!isClosable()) {
			/* Nothing has to be done then */
			return;
		}
		if(episode.intValue() == getBorderClosingEpisode().intValue()) {
			List<World> dimensions = plugin.getServer().getWorlds();
			for(World dimension: dimensions) {
				dimension.getWorldBorder().setSize(getFinalRadius(), (10 - getBorderClosingEpisode()) * config.getInt("chapter_length") * 60);
			}
			float velocity = (getOriginalBorderRadius() - getFinalRadius()) / ((10 - getBorderClosingEpisode())* config.getInt("chapter_length") * 60);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GOLD + "[Game] " + ChatColor.YELLOW + "The border is closing as you have reached episode " + getBorderClosingEpisode() + "!");
				p.sendMessage(ChatColor.GOLD + "[Game] " + ChatColor.YELLOW + "It is time for you to reach 0,0. Get ready for the fight. Good luck!");
				p.sendMessage(ChatColor.BLUE + "[Info] " + ChatColor.AQUA + "The border is closing at a velocity of " + velocity + " blocks/second.");
			}
		}
		return;
	}
	
	private static Boolean isClosable() {
		return config.getBoolean("closable_border");
	}
	
	private static Integer getFinalRadius() {
		return config.getInt("final_radius");
	}
	
	private static Integer getBorderClosingEpisode() {
		return config.getInt("border_closing_episode");
	}
	
	private static Integer getOriginalBorderRadius() {
		return config.getInt("border_radius");
	}
	
	private static Boolean configIsSet() {
		if(config.isSet("border_radius") && config.isSet("closable_border") && config.isSet("final_radius") && config.isSet("border_closing_episode")) {
			return true;
		}
		return false;
	}
	
	private static void setConfig() {
		config.set("border_radius", 1500);
		config.set("closable_border", true);
		config.set("final_radius", 150);
		config.set("border_closing_episode", 8);
		return;
	}
}
