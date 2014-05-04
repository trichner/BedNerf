package ch.k42.bednerf.minions;

import org.bukkit.Bukkit;

/**
 * Created on 03.05.14.
 *
 * @author trichner
 */
public class LogMinions {

	public static final String TAG = "[BED NERF] ";

	public static final void w(String msg){
		Bukkit.getLogger().warning(TAG+msg);
	}

	public static final void w(String msg,Object... args){
		w(String.format(msg,args));
	}


	public static void e(Exception e) {
		Bukkit.getLogger().warning(TAG+"Exception: " + e.getMessage());
		e.printStackTrace();
	}

	public static final void i(String msg){
		Bukkit.getLogger().info(TAG+msg);
	}

	public static final void i(String msg,Object... args){
		i(String.format(msg,args));
	}

	private static final String DTAG = "[Debug] ";
	public static final void d(String msg){
		Bukkit.getLogger().info(TAG + DTAG +msg);
	}

	public static final void d(String msg,Object... args){
		d(String.format(msg,args));
	}
}
