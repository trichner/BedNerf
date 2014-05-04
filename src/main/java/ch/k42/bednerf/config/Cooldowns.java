package ch.k42.bednerf.config;

/**
 * Created on 04.05.14.
 * 
 * @author trichner
 */
public class Cooldowns {
	private long firstCooldown;
	private long consecutiveCooldown;

	public Cooldowns(long firstCooldown, long consecutiveCooldown) {
		this.firstCooldown = firstCooldown;
		this.consecutiveCooldown = consecutiveCooldown;
	}

	public long getFirstCooldown() {
		return firstCooldown;
	}

	public long getConsecutiveCooldown() {
		return consecutiveCooldown;
	}
}
