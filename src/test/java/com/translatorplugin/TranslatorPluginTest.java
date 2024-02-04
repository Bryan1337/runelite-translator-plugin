package com.translatorplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import util.JSONHelper;

public class TranslatorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TranslatorPlugin.class);
		RuneLite.main(args);
	}
}