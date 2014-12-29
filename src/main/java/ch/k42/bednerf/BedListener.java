package ch.k42.bednerf;

import ch.k42.bednerf.config.Cooldowns;
import ch.k42.bednerf.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.material.Bed;
import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * Created on 03.05.14.
 * 
 * @author trichner
 */
public class BedListener implements Listener {

	private static final int TICKRATE = 20;

	private PlayerBedDAO dao;
	private BedNerfPlugin plugin;

	private Messages messages;

	private Cooldowns cooldowns;

	public BedListener(PlayerBedDAO dao, BedNerfPlugin plugin, Messages messages, Cooldowns cooldowns) {
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
			player.setBedSpawnLocation(bed.getLocation());
			if (player.getBedSpawnLocation() == null) {
				// wont delete bed, we'll stay fair
				sendMessage(player, messages.getDeathMessageBedObstructed());
			} else {
				// spawn at bed, we display a message on spawn
			}
		} else {
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
		return Material.BED_BLOCK.equals(bed.getLocation().getBlock().getType());
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
			notifyPlayer(player, cooldowns.getConsecutiveCooldown());
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
			location = getBedHead(location);
			// already same bed set?
			if (isAlreadySet(bed, location)) {
				sendMessage(player, messages.getBedClickMessageRep());
			} else {
				if (bed == null) { // player has no bed yet
					bed = new PlayerBed(); // dao.create();
					bed.setUUID(player.getUniqueId().toString());
				}
				bed.setLocation(location);
				bed.setReadyTimestamp(getNow() + cooldowns.getFirstCooldown());
				dao.persist(bed);
				if(cooldowns.getFirstCooldown()>0) {
					// only send if there is a cooldown
					sendMessage(player, messages.getBedClickMessage(cooldowns.getFirstCooldown()));
				}
				notifyPlayer(player, cooldowns.getFirstCooldown());
			}
			event.setUseInteractedBlock(Event.Result.DENY);
		}
	}

	@EventHandler
	public void onBlockEvent(BlockBreakEvent event) {
		checkBlock(event);
	}

	@EventHandler
	public void onBlockEvent(BlockDamageEvent event) {
		checkBlock(event);
	}

	@EventHandler
	public void onBlockEvent(BlockPistonRetractEvent event) {
		checkBlock(event);
	}

	@EventHandler
	public void onBlockEvent(BlockPistonExtendEvent event) {
		checkBlock(event);
	}

	@EventHandler
	public void onBlockEvent(BlockFadeEvent event) {
		checkBlock(event);
	}
	
	private void checkBlock(BlockEvent event){
		if (Material.BED_BLOCK.equals(event.getBlock().getType())) {
			// Remove all beds that used this location
			final Location location = event.getBlock().getLocation();
			// DB access might take some time and we don't need Bukkit API for this -> Async
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				@Override
				public void run() {
					List<PlayerBed> affectedBeds = dao.findByLocation(location);
					dao.remove(affectedBeds);
				}
			});
		}
	}

	private static boolean isAlreadySet(PlayerBed bed, Location location) {
		if (bed == null) {
			return false;
		}
		return location.equals(bed.getLocation());
	}

	private static Location getBedHead(Location location) {
		MaterialData data = location.getBlock().getState().getData();
		Location head = null;
		// is it even a bed?
		if(data instanceof Bed){
			Bed bed = ((Bed) data);
			// is it already the head?
			if(bed.isHeadOfBed()){
				head = location;
			}else {
				BlockFace face = bed.getFacing();
				head = location.add(face.getModX(),face.getModY(),face.getModZ());
			}
		}

		return  head;
	}

	private static boolean isBedRightClick(PlayerInteractEvent event) {
		if (!event.hasBlock()) {
			return false;
		}
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return false;
		}
		return Material.BED_BLOCK.equals(event.getClickedBlock().getType());
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
						// sendMessage(onlineplayer,messages.getSpawnNotSetMessage());
					}
				}
			}
		}, millis * TICKRATE / (1000));
	}

	private static void sendMessage(Player player, String message) {
		if (message != null && !("".equals(message))) {
			player.sendMessage(message);
		}
	}

	private static long getNow() {
		return System.currentTimeMillis();
	}
}
