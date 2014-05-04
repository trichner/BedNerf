BedNerf
=======

**Author:** Thomas Richner

**License:** [GNU General Public License, version 3 (GPL-3.0)](http://opensource.org/licenses/gpl-3.0)

BedNerf is a plugin for Minecraft Bukkit Servers, it tries to nerf exploits that are possible with Vanilla Minecraft beds. A Bed can only be used as spawn after some time has passed. After a respawn at a bed you will again have to wait for a cooldown.

This helps prevent massive suicide operations and other exploits.


For an example configuration see [Default Config](https://github.com/trichner/BedNerf/blob/master/src/main/resources/config.yml). This file will be generated if no config.yml is present in your plugin folder.

## Features ##

- keeps track of players bedspawns and nerfs them
- full persistence, works with an sql database
- informs the player of his bed status
- quite robust

## Troubleshooting ##

- spigot requires a 'ebean.properties' file, create an empty file named 'ebean.properties' in your spigot folder (where spigot.yml is) and the error should disappear.
- you can view the contents of the database by opening BedNerf.db with sqlite3
