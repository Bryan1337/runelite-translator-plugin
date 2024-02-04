package com.translatorplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("translatorplugin")
public interface TranslatorPluginConfig extends Config
{
	@ConfigItem(
		keyName = "translatorplugin",
		name = "Translator plugin",
		description = "Plugin that translates ingame text"
	)
	default String greeting()
	{
		return "Yo lol";
	}
}
