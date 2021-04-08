package uk.co.tmdavies.skillarmorsets.utils;

import net.minecraft.server.v1_14_R1.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    public static final Random random = new Random();
    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public static String Chat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public enum DefaultFontInfo {

        A('A', 5), a('a', 5), B('B', 5), b('b', 5), C('C', 5), c('c', 5), D('D', 5), d('d', 5), E('E', 5), e('e', 5),
        F('F', 5), f('f', 4), G('G', 5), g('g', 5), H('H', 5), h('h', 5), I('I', 3), i('i', 1), J('J', 5), j('j', 5),
        K('K', 5), k('k', 4), L('L', 5), l('l', 1), M('M', 5), m('m', 5), N('N', 5), n('n', 5), O('O', 5), o('o', 5),
        P('P', 5), p('p', 5), Q('Q', 5), q('q', 5), R('R', 5), r('r', 5), S('S', 5), s('s', 5), T('T', 5), t('t', 4),
        U('U', 5), u('u', 5), V('V', 5), v('v', 5), W('W', 5), w('w', 5), X('X', 5), x('x', 5), Y('Y', 5), y('y', 5),
        Z('Z', 5), z('z', 5), NUM_1('1', 5), NUM_2('2', 5), NUM_3('3', 5), NUM_4('4', 5), NUM_5('5', 5), NUM_6('6', 5),
        NUM_7('7', 5), NUM_8('8', 5), NUM_9('9', 5), NUM_0('0', 5), EXCLAMATION_POINT('!', 1), AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5), DOLLAR_SIGN('$', 5), PERCENT('%', 5), UP_ARROW('^', 5), AMPERSAND('&', 5), ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4), RIGHT_PERENTHESIS(')', 4), MINUS('-', 5), UNDERSCORE('_', 5), PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5), LEFT_CURL_BRACE('{', 4), RIGHT_CURL_BRACE('}', 4), LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3), COLON(':', 1), SEMI_COLON(';', 1), DOUBLE_QUOTE('"', 3), SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4), RIGHT_ARROW('>', 4), QUESTION_MARK('?', 5), SLASH('/', 5), BACK_SLASH('\\', 5),
        LINE('|', 1), TILDE('~', 5), TICK('`', 2), PERIOD('.', 1), COMMA(',', 1), SPACE(' ', 3), DEFAULT('a', 4);

        private char character;
        private int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public char getCharacter() {
            return this.character;
        }

        public int getLength() {
            return this.length;
        }

        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE)
                return this.getLength();
            return this.length + 1;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c)
                    return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }
    }

    private final static int CENTER_PX = 154;

    public static void sendCenteredMessage(String message) {
        if (message == null || message.equals(""))
            Bukkit.broadcastMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        Bukkit.broadcastMessage(sb.toString() + message);
    }

    public static void sendPlayerCenteredMessage(Player p, String message) {
        if (message == null || message.equals(""))
            p.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode == true) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        p.sendMessage(sb.toString() + message);
    }

    public static List<ItemStack> armorCheck(Player p) {
        List<ItemStack> armors = new ArrayList<>();
        PlayerInventory inv = p.getInventory();

        if (inv.getHelmet() != null)
            armors.add(inv.getHelmet());

        if (inv.getChestplate() != null)
            armors.add(inv.getChestplate());

        if (inv.getLeggings() != null)
            armors.add(inv.getLeggings());

        if (inv.getBoots() != null)
            armors.add(inv.getBoots());

        return armors;

    }

    public static void spawnDamageIndicator(Player p, Entity deadMob, double damageAmount, boolean isCrit) {
        Location loc = deadMob.getLocation();
        double rangeMin = -1.0;
        double rangeMax = 1.0;
        double r1 = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        double r2 = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        double r3 = rangeMin + (rangeMax - rangeMin) * random.nextDouble();

        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc.add(r1, r2, r3), EntityType.ARMOR_STAND);

        stand.setVisible(false);
        stand.setGravity(false);
        stand.setBasePlate(false);
        if (!isCrit) {
            stand.setCustomName(Utils.Chat("&c" + damageAmount));
        } else {
            stand.setCustomName(Utils.Chat("&a" + damageAmount));
        }
        stand.setCustomNameVisible(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> stand.remove(), 40L);
    }

    public static List<Entity> getEntitiesAroundPoint(Location location, double radius) {
        List<Entity> entities = new ArrayList<Entity>();
        World world = location.getWorld();

        // To find chunks we use chunk coordinates (not block coordinates!)
        int smallX = MathHelper.floor((location.getX() - radius) / 16.0D);
        int bigX = MathHelper.floor((location.getX() + radius) / 16.0D);
        int smallZ = MathHelper.floor((location.getZ() - radius) / 16.0D);
        int bigZ = MathHelper.floor((location.getZ() + radius) / 16.0D);

        for (int x = smallX; x <= bigX; x++) {
            for (int z = smallZ; z <= bigZ; z++) {
                if (world.isChunkLoaded(x, z)) {
                    entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities())); // Add all entities from this chunk to the list
                }
            }
        }

        // Remove the entities that are within the box above but not actually in the sphere we defined with the radius and location
        // This code below could probably be replaced in Java 8 with a stream -> filter
        Iterator<Entity> entityIterator = entities.iterator(); // Create an iterator so we can loop through the list while removing entries
        while (entityIterator.hasNext()) {
            if (entityIterator.next().getLocation().distanceSquared(location) > radius * radius) { // If the entity is outside of the sphere...
                entityIterator.remove(); // Remove it
            }
        }
        return entities;
    }

    public static String colorCodesHex(String message) {

        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})#");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                    + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                    + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5)
            );
        }

        return matcher.appendTail(buffer).toString();

    }

    public static String colorRainbow(String message) {

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            double hue = map(0, message.length(), 0, 1, i);
            if (hue > 1) hue = 1;
            if (hue < 0) hue = 0;
            int rgb = Color.HSBtoRGB((float) hue, 1, 1);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            output.append("&#")
                    .append(extendStringPrefix(Integer.toHexString(red), 2, "0"))
                    .append(extendStringPrefix(Integer.toHexString(green), 2, "0"))
                    .append(extendStringPrefix(Integer.toHexString(blue), 2, "0"))
                    .append("#").append(message.charAt(i));
        }

        return colorCodesHex(output.toString());

    }

    public static String extendStringPrefix(String string, int minLength, String filler) {

        StringBuilder output = new StringBuilder(string);
        while (output.length() < minLength) {
            output.insert(0, filler);
        }
        return output.toString();

    }

    public static double map(double inStart, double inEnd, double outStart, double outEnd, double value) {

        if (value < inStart) return outStart;
        if (value > inEnd) return outEnd;
        return (value - inStart) / (inEnd - inStart) * (outEnd - outStart) + outStart;

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

}
