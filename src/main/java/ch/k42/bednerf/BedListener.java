package ch.k42.bednerf;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.google.common.collect.ImmutableSet;

import ch.k42.bednerf.config.Cooldowns;
import ch.k42.bednerf.config.Messages;

/**
 * Created on 03.05.14.
 * 
 * @author trichner
 */
public class BedListener implements Listener{

	private static final int TICKRATE = 20;
	private static final Set<Material> BED_BLOCKS = ImmutableSet.of(Material.BED, Material.BED_BLOCK);

	private PlayerBedDAO dao;
	private BedNerfPlugin plugin;

	private Messages messages;

	private Cooldowns cooldowns;

	public BedListener(PlayerBedDAO dao, BedNerfPlugin plugin, Messages messages,
			Cooldowns cooldowns) {
		this.dao = dao;
		this.plugin = plugin;
		this.messages = messages;
		this.cooldowns = cooldowns;
	}

	// Death Event, set minecraft spawn if available, otherwise set null
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerBed bed = dao.findByUUID(player.getUniqueId().toString());

		// has a bed an it's ready
		if (isReady(bed)) {
			// Bed is ready, set spawn
			sendMessage(player, messages.getDeathMessageBed());
			player.setBedSpawnLocation(bed.getLocation(), true); // maybe we have to force the spawn location
		} else {
			sendMessage(player, messages.getDeathMessageNoBed());
			player.setBedSpawnLocation(null);
			dao.remove(bed);
		}
	}

	private static boolean isReady(PlayerBed bed) {
		// Has a bed?
		if (bed == null) {
			return false;
		}
		// Bed ready?
		if (bed.getReadyTimestamp() > getNow()) {
			return false;
		}
		// Bed still here?
		return BED_BLOCKS.contains(bed.getLocation().getBlock().getType());
	}

	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		PlayerBed bed = dao.findByUUID(player.getUniqueId().toString());
		if (bed != null) {
			// update cooldown
			bed.setReadyTimestamp(getNow() + cooldowns.getConsecutiveCooldown());
			dao.persist(bed);
			sendMessage(player, messages.getRespawnMessageBed(cooldowns.getConsecutiveCooldown()));
			notifyPlayer(player,cooldowns.getConsecutiveCooldown());
		} else {
			// Randomspawn
			sendMessage(player, messages.getRespawnMessageNoBed());
		}
	}

	/**
	 * Player clicks a bed
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (isBedRightClick(event)) {
			// Set Bedspawn
			Player player = event.getPlayer();
			PlayerBed bed = dao.findByUUID(player.getUniqueId().toString());
			// location of bed
			Location location = event.getClickedBlock().getLocation();
			// already same bed set?
			if (isAlreadySet(bed, location)) {
				sendMessage(player, messages.getBedClickMessageRep());
			} else {
				if (bed == null) { // player has no bed yet
					bed = new PlayerBed(); //dao.create();
					bed.setUUID(player.getUniqueId().toString());
				}
				bed.setLocation(location);
				bed.setReadyTimestamp(getNow() + cooldowns.getFirstCooldown());
				dao.persist(bed);
				sendMessage(player,messages.getBedClickMessage(cooldowns.getFirstCooldown()));
				notifyPlayer(player, cooldowns.getFirstCooldown());
			}
			event.setUseInteractedBlock(Event.Result.DENY);
		}
	}

	private static final boolean isAlreadySet(PlayerBed bed, Location location){
		if(bed==null){
			return false;
		}
		return location.equals(bed.getLocation());
	}

	private static final boolean isBedRightClick(PlayerInteractEvent event) {
		if (!event.hasBlock()) {
			return false;
		}
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return false;
		}
		return BED_BLOCKS.contains(event.getClickedBlock().getType());
	}

	private static final long OFFSET = 1000; // 1second

	private void notifyPlayer(final OfflinePlayer player, long millis) {
		// adding 1s to make sure everything is ready
		millis += OFFSET;

		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				Player onlineplayer = plugin.getServer().getPlayer(player.getUniqueId());
				if (onlineplayer != null) { // player even online?
					PlayerBed bed = dao.findByUUID(player.getUniqueId().toString());
					if (isReady(bed)) {
						sendMessage(onlineplayer, messages.getSpawnSetMessage());
					} else {
						//sendMessage(onlineplayer,messages.getSpawnNotSetMessage());
					}
				}
			}
		}, millis * TICKRATE / (1000));
	}

	private static final void sendMessage(Player player,String message){
		if(message!=null){
			player.sendMessage(message);
		}
	}

	private static final long getNow() {
		return System.currentTimeMillis();
	}
}
