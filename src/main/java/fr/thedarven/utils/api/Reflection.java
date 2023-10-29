package fr.thedarven.utils.api;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Utility methods for the reflection and the NMS
 */
public class Reflection {

    /**
     * Retrieves the EntityPlayer of a player
     *
     * @param player the player
     * @return
     */
    public static EntityPlayer getEntityPlayer(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        return craftPlayer.getHandle();
    }

    /**
     * Sends packets to a player
     *
     * @param player the player whom to send the packets
     * @param packets the packets
     */
    public static void sendPackets(Player player, Packet<?>... packets) {
        EntityPlayer entityPlayer = getEntityPlayer(player);
        for (Packet<?> packet : packets) {
            entityPlayer.playerConnection.sendPacket(packet);
        }
    }

    /**
     * Changes private property of an instance object
     *
     * @param instance The instance of object to change
     * @param fieldName The field name
     * @param value The new value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static void setValue(Object instance, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        setValue(instance, instance.getClass(), fieldName, value);
    }

    /**
     * Changes private property of an instance object with specific class
     *
     * @param instance The instance of object to change
     * @param clazz The class of property
     * @param fieldName The field name
     * @param value The new value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static void setValue(Object instance, Class<?> clazz, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(clazz, fieldName).set(instance, value);
    }

    /**
     * Retrieves a readable field of a class
     *
     * @param clazz The class
     * @param fieldName The field name
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

}