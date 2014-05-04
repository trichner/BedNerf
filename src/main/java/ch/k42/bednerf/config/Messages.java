package ch.k42.bednerf.config;

/**
 * Created on 04.05.14.
 * 
 * @author trichner
 */
public class Messages {
	private String spawnSetMessage;
	private String spawnNotSetMessage;
	private String bedClickMessage;
	private String bedClickMessageRep;
	private String respawnMessageBed;
	private String respawnMessageNoBed;
	private String deathMessageBed;
	private String deathMessageNoBed;
	private String deathMessageBedObstructed;

	public Messages(String spawnSetMessage, String spawnNotSetMessage, String bedClickMessage,
			String bedClickMessageRep, String respawnMessageBed, String respawnMessageNoBed,
			String deathMessageBed, String deathMessageNoBed, String deathMessageBedObstructed) {
		this.spawnSetMessage = spawnSetMessage;
		this.spawnNotSetMessage = spawnNotSetMessage;
		this.bedClickMessage = bedClickMessage;
		this.bedClickMessageRep = bedClickMessageRep;
		this.respawnMessageBed = respawnMessageBed;
		this.respawnMessageNoBed = respawnMessageNoBed;
		this.deathMessageBed = deathMessageBed;
		this.deathMessageNoBed = deathMessageNoBed;
		this.deathMessageBedObstructed = deathMessageBedObstructed;
	}

	public String getDeathMessageBedObstructed() {
		return deathMessageBedObstructed;
	}

	public String getSpawnNotSetMessage() {
		return spawnNotSetMessage;
	}

	public String getRespawnMessageBed(long millis) {
		return String.format(respawnMessageBed, toMinutes(millis));
	}

	public String getRespawnMessageNoBed() {
		return respawnMessageNoBed;
	}

	public String getDeathMessageBed() {
		return deathMessageBed;
	}

	public String getDeathMessageNoBed() {
		return deathMessageNoBed;
	}

	public String getSpawnSetMessage() {
		return spawnSetMessage;
	}

	public String getBedClickMessage(long millis) {
		return String.format(bedClickMessage, toMinutes(millis));
	}

	public String getBedClickMessageRep() {
		return bedClickMessageRep;
	}

	private static int toMinutes(long millis) {
		return (int) Math.ceil((millis / (1000 * 60.0)));
	}
}
