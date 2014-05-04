package ch.k42.bednerf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created on 03.05.14.
 * 
 * @author trichner
 */
@Entity
@Table(name = "PLAYER_BEDS")
public class PlayerBed {

	@Id
	Integer id;

	@Column(name = "UUID")
	String UUID;

	@Column(name = "X")
	int x;

	@Column(name = "Y")
	int y;

	@Column(name = "Z")
	int z;

	@Column(name = "WORLD")
	String world;

	@Column(name = "READY_TIME")
	long readyTimestamp;

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
	}

	public Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z);
	}

	public void setLocation(Location location) {
		setX(location.getBlockX());
		setY(location.getBlockY());
		setZ(location.getBlockZ());
		setWorld(location.getWorld().getName());
	}

	public long getReadyTimestamp() {
		return readyTimestamp;
	}

	public void setReadyTimestamp(long readyTimestamp) {
		this.readyTimestamp = readyTimestamp;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
