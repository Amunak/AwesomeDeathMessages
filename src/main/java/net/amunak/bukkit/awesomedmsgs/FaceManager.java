package net.amunak.bukkit.awesomedmsgs;

/**
 * Copyright 2014 Jiří Barouš
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manager used to cache the faces of players
 * 
 * @author sword
 * @author TeaJizzle
 * @author Amunak
 */
public class FaceManager {

    protected JavaPlugin plugin;
    /**
     * Service URL used to fetch images
     * <p><tt>{0}</tt> is replaced by player's identifier</p>
     */
    public static final String SKIN_API_URL = "https://minotar.net/avatar/{0}/8.png";
    protected HashMap<UUID, BufferedImage> imageCache;

    public FaceManager(JavaPlugin p) {
        plugin = p;

        // Determine the initial hashmap capacity
        // Possibly going unnecessarily complicated here
        int mapCapacity = (p.getServer().getMaxPlayers() < 8) ? 8 : p.getServer().getMaxPlayers();
        mapCapacity /= 2;
        if (mapCapacity > 64) {
            mapCapacity = 64;
        }
        if (mapCapacity < p.getServer().getOnlinePlayers().size()) {
            mapCapacity = (int) (p.getServer().getOnlinePlayers().size() * 1.4);
        }
        imageCache = new HashMap<>(mapCapacity);
    }

    /**
     * Caches a player's face using custom image
     *
     * @param p the player
     * @param i the face image
     * @return <tt>true</tt> on success (image was cached)
     */
    public boolean cacheFace(Player p, BufferedImage i) {
        assert p != null;

        if (i != null) {
            imageCache.put(p.getUniqueId(), i);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Caches a player's face using {@link #fetchFace(org.bukkit.entity.Player)}
     *
     * @param p the player
     * @return <tt>true</tt> on success (image was cached)
     */
    public boolean cacheFace(Player p) {
        assert p != null;
        return cacheFace(p, fetchFace(p));
    }

    /**
     * Tries to fetch a player's face from online resource (skin API) defined by {@link #SKIN_API_URL}
     *
     * @param p
     * @return
     */
    public BufferedImage fetchFace(Player p) {
        assert p != null;
        BufferedImage faceImage = null;
        String finalURL = MessageFormat.format(SKIN_API_URL, p.getName());

        try {
            URL imageResourcePath = new URL(finalURL);
            faceImage = ImageIO.read(imageResourcePath);
        } catch (IOException e) {
            p.getServer().getLogger().log(Level.INFO, "Resource Error: {0} could not be loaded:", finalURL);
            p.getServer().getLogger().log(Level.INFO, "{0}; trying fallback face", e.toString());
            try {
                faceImage = ImageIO.read(plugin.getResource("face_fallback.png"));
            } catch (IOException f) {
                p.getServer().getLogger().log(Level.WARNING, "Failed to load the fallback face: {0}", f.toString());
            }
        }

        return faceImage;
    }

    /**
     * Returns player's cached face Attempts to get it if it's not cached
     *
     * @param p the player
     * @return player's face
     */
    public BufferedImage getFace(Player p) {
        assert p != null;

        // This may be a bad idea. Remove if people complain.
        if (!hasFaceCached(p)) {
            cacheFace(p);
        }

        return imageCache.get(p.getUniqueId());
    }

    /**
     * Checks whether the player has a cached face
     *
     * @param p the player
     * @return <tt>true</tt> when the player's face is cached
     */
    public boolean hasFaceCached(Player p) {
        return imageCache.containsKey(p.getUniqueId());
    }

    /**
     * Removes player's image from the cache
     * <p>
     * Fails silently when entry for the player doesn't exist</p>
     *
     * @param p the player
     */
    public void flushCache(Player p) {
        imageCache.remove(p.getUniqueId());
    }

    /**
     * Removes all players' images from the cache
     */
    public void flushCache() {
        imageCache.clear();
    }
}
