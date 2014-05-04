package ch.k42.bednerf;

import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Location;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;

import ch.k42.bednerf.minions.LogMinions;

/**
 * Created on 03.05.14.
 * 
 * @author trichner
 */
public class PlayerBedDAO {

	private BedNerfPlugin plugin;
	private EbeanServer db;

	public PlayerBedDAO(BedNerfPlugin plugin) {
		this.plugin = plugin;
	}

	public void connect() {
		db = plugin.getDatabase();
		try {
			db.find(PlayerBed.class).findRowCount();
		} catch (PersistenceException e) {
			plugin.installDDL();
			LogMinions.w("No database found, creating new one.");
		}
	}

	public PlayerBed findByUUID(String UUID) {
		Query<PlayerBed> query = find().where().eq("UUID", UUID).query();
		try {
			return query.findUnique();
		} catch (PersistenceException e) {
			return null;
		}
	}

	public void persist(PlayerBed bed) {
		if (bed != null) {
			db.save(bed);
		}
	}

	public List<PlayerBed> findByLocation(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String world = location.getWorld().getName();
		Query<PlayerBed> query = find().where().eq("X", x).eq("Y", y).eq("Z", z).eq("WORLD", world).query();
		return query.findList();
	}

	public void remove(PlayerBed bed) {
		if (bed != null) {
			db.delete(bed);
		}
	}

	public void remove(Collection<PlayerBed> beds) {
		db.delete(beds);
	}

	private Query<PlayerBed> find() {
		return db.find(PlayerBed.class);
	}

}
