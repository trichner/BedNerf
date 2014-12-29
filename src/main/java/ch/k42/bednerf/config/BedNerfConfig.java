package ch.k42.bednerf.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created on 04.05.14.
 * 
 * @author trichner
 */
public class BedNerfConfig {

	private static final String KEY_FIRSTCOOLDOWN = "cooldowns.initial";
	private static final String KEY_CONSECUTIVECOOLDOWN = "cooldowns.consecutive";

	private static final String MSGS = "messages.";
	private static final String KEY_MSGSPAWNSET = MSGS.concat("spawnSetMessage");
	private static final String KEY_MSGSPAWNNOTSET = MSGS.concat("spawnNotSetMessage");

	private static final String KEY_MSGDEATHBED = MSGS.concat("deathMessageBed");
	private static final String KEY_MSGDEATHNOBED = MSGS.concat("deathMessageNoBed");
	private static final String KEY_MSGDEATHBEDOB = MSGS.concat("deathMessageBedObstructed");

	private static final String KEY_MSGSPAWNBED = MSGS.concat("respawnMessageBed");
	private static final String KEY_MSGSPAWNNOBED = MSGS.concat("respawnMessageNoBed");

	private static final String KEY_MSGCLICKBED = MSGS.concat("bedClickMessage");
	private static final String KEY_MSGCLICKBEDREP = MSGS.concat("bedClickMessageRep");

	private FileConfiguration config;

	private Cooldowns cooldowns;
	private Messages messages;

	public BedNerfConfig(FileConfiguration config) {
		this.config = config;
	}

	public void load() {
		int initial = config.getInt(KEY_FIRSTCOOLDOWN) * 1000;
		int consecutive = config.getInt(KEY_CONSECUTIVECOOLDOWN) * 1000;
		cooldowns = new Cooldowns(initial, consecutive);

		MessagesBuilder builder = new MessagesBuilder()
				.setBedClickMessage(readMessage(KEY_MSGCLICKBED))
				.setBedClickMessageRep(readMessage(KEY_MSGCLICKBEDREP))
				.setDeathMessageBed(readMessage(KEY_MSGDEATHBED))
				.setDeathMessageNoBed(readMessage(KEY_MSGDEATHNOBED))
				.setRespawnMessageBed(readMessage(KEY_MSGSPAWNBED))
				.setRespawnMessageNoBed(readMessage(KEY_MSGSPAWNNOBED))
				.setSpawnSetMessage(readMessage(KEY_MSGSPAWNSET))
				.setDeathMessageBedObstructed(readMessage(KEY_MSGDEATHBEDOB));

		messages = builder.createMessages();
	}

	private String readMessage(String key) {
		String msg = config.getString(key);
		if (msg != null) {
			return ChatColor.translateAlternateColorCodes('&', msg);
		} else {
			return null;
		}
	}

	public Cooldowns getCooldowns() {
		return cooldowns;
	}

	public Messages getMessages() {
		return messages;
	}
}
