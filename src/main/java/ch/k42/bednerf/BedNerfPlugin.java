package ch.k42.bednerf;

import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import ch.k42.bednerf.config.BedNerfConfig;

/**
 * Created on 03.05.14.
 * 
 * @author trichner
 */
public class BedNerfPlugin extends JavaPlugin {

	@Override
	public List<Class<?>> getDatabaseClasses() {
		return Arrays.<Class<?>> asList(PlayerBed.class);
	}

	// expose database setup
	@Override
	public void installDDL() {
		super.installDDL();
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable() {

		// ---- load config
		this.saveDefaultConfig();
		BedNerfConfig config = new BedNerfConfig(getConfig());
		config.load();

		// ---- init dao/DB
		PlayerBedDAO dao = new PlayerBedDAO(this);
		dao.connect();

		// ---- register listeners
		BedListener listener = new BedListener(dao, this, config.getMessages(), config.getCooldowns());
		getServer().getPluginManager().registerEvents(listener, this);
	}
}
