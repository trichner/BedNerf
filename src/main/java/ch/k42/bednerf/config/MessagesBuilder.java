package ch.k42.bednerf.config;

public class MessagesBuilder {
	private String spawnSetMessage;
	private String bedClickMessage;
	private String bedClickMessageRep;
	private String respawnMessageBed;
	private String respawnMessageNoBed;
	private String deathMessageBedObstructed;

	public MessagesBuilder setSpawnSetMessage(String spawnSetMessage) {
		this.spawnSetMessage = spawnSetMessage;
		return this;
	}

	public MessagesBuilder setBedClickMessage(String bedClickMessage) {
		this.bedClickMessage = bedClickMessage;
		return this;
	}

	public MessagesBuilder setBedClickMessageRep(String bedClickMessageRep) {
		this.bedClickMessageRep = bedClickMessageRep;
		return this;
	}

	public MessagesBuilder setRespawnMessageBed(String respawnMessageBed) {
		this.respawnMessageBed = respawnMessageBed;
		return this;
	}

	public MessagesBuilder setRespawnMessageNoBed(String respawnMessageNoBed) {
		this.respawnMessageNoBed = respawnMessageNoBed;
		return this;
	}

	public MessagesBuilder setDeathMessageBedObstructed(String deathMessageBedObstructed) {
		this.deathMessageBedObstructed = deathMessageBedObstructed;
		return this;
	}

	public Messages createMessages() {
		return new Messages(spawnSetMessage, bedClickMessage, bedClickMessageRep, respawnMessageBed,
				respawnMessageNoBed, deathMessageBedObstructed);
	}
}