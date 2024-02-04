package com.translatorplugin;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import util.JSONHelper;
import util.TextHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Translator Plugin"
)
public class TranslatorPlugin extends Plugin
{
	@Inject
	private Client client;

	private boolean enabled = false;

	private static final ImmutableMap<String, String> MenuOptionRemap = ImmutableMap.<String, String>builder()
			.put("Play", "Speel")
			.put("Open Store", "Open Winkel")
			.put("View Inbox", "Bekijk Inbox")
			.put("Learn More", "Lees meer")
			.put("Use", "Gebruik")
			.put("Wear", "Draag")
			.put("Climb", "Beklim")
			.put("Trade", "Ruil")
			.put("Makeover", "Maak over")
			.put("Eat", "Eet")
			.put("Steal-from", "Steel van")
			.put("Search", "Doorzoek")
			.put("Revert", "Reverteer")
			.put("Drop", "Neerleggen")
			.put("Examine", "Examineer")
			.put("Cancel", "Canceleer")
			.put("Check", "Bekijk")
			.put("Unload", "Los")
			.put("Uncharge", "Ontlaad")
			.put("Rub", "Wrijf over")
			.put("Feel", "Voel")
			.put("Destroy", "Vernietig")
			.put("Remove", "Verwijder")
			.put("Trophy Emote", "Trofee emotie")
			.put("Talk-to", "Praat met")
			.put("Collect", "Haal op")
			.put("Follow", "Volg")
			.put("Walk here", "Loop hier")
			.put("Trade with", "Ruil met")
			.put("Report", "Raporteer")
			.put("Lookup", "Zoek op")
			.put("Exchange", "Ruil")
			.put("Prices", "Prijzen")
			.put("View", "Bekijk")
			.put("Decant", "Decanteer")
			.put("Chop down", "Hak om")
			.put("Travel", "Reis met")
			.put("Activate", "Activeer")
			.put("Cast", "Versturen")
			.put("Outside", "Buiten")
			.put("History", "Geschiedenis")
			.put("Sets", "Setten")
			.put("Metamorphosis", "Metamorfose")
			.put("Claim-staves", "Claim staven")
			.put("Pick-up", "Pak op")
			.put("Attack", "Val aan")
			.put("Pickpocket", "Beroof")
			.put("Climb-up", "Klim omhoog")
			.put("Climb-down", "Klim omlaag")
			.put("Pet", "Aai")
			.put("Deposit", "Depositeer")
			.put("Smith", "Smeed")
			.put("Shoo-away", "Jaag weg")
			.put("Close", "Doe dicht")
			.put("Check-health", "Bekijk gezondheid")
			.put("Inspect", "Inspecteer")
			.put("Guide", "Gids")
			.put("Loot", "Gids")
			.put("Climb-into", "Klim in")
			.put("Cross", "Spring over")
			.put("Configure", "Configureer")
			.put("Friends List", "Vriendenlijst")
			.put("Add Friend", "Voeg vriend toe")
			.put("Delete", "Verwijder")
			.put("Message", "Bericht")
			.put("Quest List", "Quest lijst")
			.put("Inventory", "Inventaris")
			.put("Skills", "Vaardigheden")
			.put("Runecraft", "Runemaken")
			.put("guide", "Gids")
			.put("Construction", "Constructie")
			.put("Enter-guest", "Naar binnen-gast")
			.put("Enter-member", "Naar binnen-lid")
			.put("Enter", "Naar binnen")
			.put("Account Management", "Accountbeheer")
			.put("Logout", "Uitloggen")
			.put("Settings", "Instellingen")
			.put("Toggle Run", "Schakel ren")
			.put("Stats", "Eigenschappen")
			.put("Rummage", "Snuffelen")
			.put("Knock-at", "Klop-op")
			.put("Unlock", "Ontgrendel")
			.put("Buy", "Koop")
			.put("Walk-down", "Loop naar beneden")
			.put("Study", "Bestudeer")
			.put("Clean", "Maak schoon")
			.put("Take", "Neem")
			.put("Look-at", "Kijk naar")
			.put("Shave", "Scheer")
			.put("Haircut", "Kapper")
			.put("Pray-at", "Bid bij")
			.put("Music Player", "Muziekspeler")
			.put("Withdraw-All", "Pak-All")
			.put("Withdraw-X", "Pak-X")
			.put("Withdraw-10", "Pak-10")
			.put("Withdraw-5", "Pak-5")
			.put("Withdraw-1", "Pak-1")
			.put("Placeholder", "Plaatshouder")
			.put("Withdraw-All-but-1", "Pak-All-behalve-1")
			.put("Remove placeholders", "Verwijder plaatshouders")
			.put("Collapse tab", "Klap tab in")
			.put("View tab", "Bekijk tab")
			.put("Bank", "")
			.put("Channel:", "Kanaal:")
			.put("Clear history", "Verwijder geschiedenis")
			.put("Show none", "Bekijk niks")
			.put("Show friends", "Bekijk vrienden")
			.put("Show all", "Bekijk alles")
			.put("Switch tab", "Verander tab")
			.put("Worn Equipment", "Gedragen uitrusting")
			.put("Redecorate", "Herdecoreer")
			.put("Relocate", "Verhuis")
			.put("Combat Options", "Aanvalsopties")
			.put("Cook", "Kook")
			.put("Build", "Bouw")
			.put("Chat-channel", "Chat-kanaal")
			.put("Trade:", "Ruil:")
			.put("Private:", "Prive:")
			.put("Setup your autochat", "Zet autochat op")
			.put("Filter public chat", "Filter publieke chat")
			.put("Public:", "Publiek")
			.put("Hide", "Verberg")
			.put("Show standard", "Laat standaard zien")
			.put("Show autochat", "Laat autochat zien")
			.put("Configure filter", "Configureer filter")
			.put("Game:", "Spel")
			.put("Filter", "Filtreer")
			.put("Set chat mode:", "Zet chat modus")
			.put("Guest Clan (/@gc)", "Gasten CLan (/@gc)")
			.put("Channel (/@f)", "Kanaal (/@f)")
			.put("Public (/@p)", "Publiek (/@p)")
			.put("Deposit-All", "Depositeer-All")
			.put("Deposit-X", "Depositeer-X")
			.put("Deposit-10", "Depositeer-10")
			.put("Deposit-5", "Depositeer-5")
			.put("Deposit-1", "Depositeer-1")
			.put("Report game bug", "Reporteer bug")
			.put("Toggle player option", "Schakel speler optie")
			.put("Report abuse", "Reporteer misbruik")
			.put("Set custom quantity", "Zet aangepast aantal")
			.put("Enable spell filtering", "Zet spreuk filtering aan")
			.put("Magic", "Magie")
			.put("Disable prayer filtering", "Zet gebedsfiltering uit")
			.put("Prayer", "Bidden")
			.put("Emotes", "Emoties")
			.put("Pummel", "Pummeleer")
			.put("Block", "Blokkeer")
			.put("Auto retaliate", "Auto retaliatie")
			.put("Special Attack", "Speciale aanval")
			.put("All Settings", "Alle instellingen")
			.put("Select", "Selecteer")
			.put("Adjust Brightness", "Pas helderheid aan")
			.put("Audio", "Geluid")
			.put("Toggle skull prevention", "Schakel schedel preventie")
			.put("View House Options", "Bekijk huis opties")
			.put("Open Bond Pouch", "Open bond zakje")
			.put("Call Servant", "Roep butler")
			.put("Expel Guests", "Zet gasten uit")
			.put("Leave House", "Verlaat huis")
			.put("Viewer", "Bekijker")
			.put("On", "Aan")
			.put("Off", "Uit")
			.put("Display", "Weergeef")
			.put("Unlock hint", "Laat hint zien")
			.put("Manual", "Manueel")
			.put("Random", "Willekeurig")
			.put("Toggle", "Schakel")
			.put("Area", "Gebied")
			.put("Strength", "Sterkheid")
			.put("Defence", "Verdediging")
			.put("Slayer", "Slachten")
			.put("Hunter", "Jagen")
			.put("Farming", "Boeren")
			.put("Perform", "Voer uit")
			.put("Relic unlock", "Reliek unlock")
			.put("Toggle Accept Aid", "Schakel accept aid")
			.put("Warnings", "Waarschuwingen")
			.put("Activities", "Activiteiten")
			.put("Popout", "Pop uit")
			.put("Join", "Betreed")
			.put("Setup", "Opzetten")
			.put("View another clan", "")
			.put("General", "Generaal")
			.put("Captain", "Kapitein")
			.put("Lieutenant", "Luitenant")
			.put("Corporal", "Corporaal")
			.put("Not ranked", "Niet gerankeerd")
			.put("Only me", "Alleen ik")
			.put("General+", "Generaal+")
			.put("Captain+", "Kapitein+")
			.put("Lieutenant+", "Luitenant+")
			.put("Corporal+", "Corporaal+")
			.put("Any friends", "Vrienden")
			.put("Anyone", "Iedereen")
			.put("Disable", "Uitzetten")
			.put("Set prefix", "Zet voorvoegsel")
			.put("Your Clan", "Jouw clan")
			.put("Grouping", "Groepering")
			.put("Teleport", "Teleporteer")
			.build();

	private ArrayList<String> rightClickOverrides = new ArrayList<>();

	private Map<String, String> itemTranslationMap;

	private Map<String, String> itemTranslationOverrideMap = Map.of(
		"Shark", "Schaark"
	);

	@Inject
	private TranslatorPluginConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Translator plugin enabled!");

		this.enabled = true;

		this.initTranslations();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Translator plugin disabled");

		for(String s : this.rightClickOverrides) {

			log.info("Option: '" + s + "'");
		}

		this.enabled = false;
	}

	private void initTranslations() {

		Map<String, String> itemTranslationMap = JSONHelper.getJsonMapFromPath("src/assets/itemTranslationMap_nl.json");

		this.itemTranslationMap = itemTranslationMap;
	}

	@Subscribe
	private void onBeforeRender(BeforeRender event) {

		if (client.getGameState() != GameState.LOGGED_IN) {

			return;
		}

		for (Widget widgetRoot : client.getWidgetRoots()) {

			remapWidget(widgetRoot);
		}
	}

	private void remapWidget(Widget widget) {

		if(!this.enabled) {

			return;
		}

		Widget[] children = widget.getDynamicChildren();

		if (children == null) {

			return;
		}

		Widget[] childComponents = widget.getDynamicChildren();

		if (childComponents != null) {

			mapWidgetText(childComponents);
		}

		childComponents = widget.getStaticChildren();

		if (childComponents != null) {

			mapWidgetText(childComponents);
		}

		childComponents = widget.getNestedChildren();

		if (childComponents != null) {

			mapWidgetText(childComponents);
		}
	}

	private void mapWidgetText(Widget[] childComponents) {
		for (Widget component : childComponents) {

			remapWidget(component);

			String text = component.getText();

			if (text.isEmpty()) {

				continue;
			}

			RemapWidgetText(component, text, itemTranslationMap);
		}
	}

	@Subscribe
	protected void onMenuEntryAdded(MenuEntryAdded event) {
		MenuEntry entry = event.getMenuEntry();
		RemapMenuEntryText(entry);
	}

	private void RemapMenuEntryText(MenuEntry menuEntry) {

		ArrayList<String> strippedTargets = TextHelper.extractWordBetweenTags(menuEntry.getTarget());

		String newTarget = menuEntry.getTarget();

		for(String target : strippedTargets) {

			String itemNameEntry = itemTranslationMap.get(target);

			if(itemNameEntry == null) {

				continue;
			}

			newTarget = newTarget.replace(target, itemNameEntry);
		}

		menuEntry.setTarget(newTarget);

		ArrayList<String> strippedOptions = TextHelper.extractWordBetweenTags(menuEntry.getOption());

		String newOption = menuEntry.getOption();

		for(String option : strippedOptions) {

			String optionEntry = MenuOptionRemap.get(option);

			if(optionEntry == null) {

				this.rightClickOverrides.add(option);

				continue;
			}

			newOption = newOption.replace(option, optionEntry);
		}

		menuEntry.setOption(newOption);
	}
	private void RemapWidgetText(Widget component, String text, Map<String, String> map) {

		String strippedText = TextHelper.extractTextBetweenTags(component.getText());

		String itemNameEntry = itemTranslationMap.get(strippedText);

		if(itemNameEntry != null) {

			String translatedItemNameEntry = TextHelper.reinsertTextBetweenTags(component.getText(), itemNameEntry);

			component.setText(translatedItemNameEntry);
		}
	}
	@Provides
	TranslatorPluginConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TranslatorPluginConfig.class);
	}
}
