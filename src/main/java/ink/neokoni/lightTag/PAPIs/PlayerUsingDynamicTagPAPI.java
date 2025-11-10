package ink.neokoni.lightTag.PAPIs;

import ink.neokoni.lightTag.DataStorage.PlayerDatas;
import ink.neokoni.lightTag.DataStorage.Tags;
import ink.neokoni.lightTag.Utils.TagUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class PlayerUsingDynamicTagPAPI {
    private Player player;
    private int using; // tag id
    private int animateDely=0;
    private int delayTickTimer=0;
    private int animateTimer = 0; // timer for animte frame cont
    private String tagType;
    private Component[] frames = new Component[]{Component.text("")};

    public PlayerUsingDynamicTagPAPI(Player p) {
        player = p;

        String usevalue = PlayerDatas.getString(player.getUniqueId()+".using");

        if(usevalue==null) {
            return;
        }
        using = Integer.valueOf(usevalue);

        if (using<0) {
            return;
        }

        frames = TagUtils.getTagContentById(using);
        if (frames==null) {
            return;
        }

        tagType = Tags.getString(using+".type");

        if (!tagType.equals("ANIMATION")) {
            return;
        }

        animateDely = Tags.getInt(using+".delay");
    }

    public String get() {
        if (using<0) {
            return "";
        }

        if (tagType.equals("ANIMATION") && animateTimer== frames.length) {
            animateTimer = 0;
        }

        switch (tagType) {
            case "STATIC": {
                return LegacyComponentSerializer.legacySection().serialize(frames[0]);
            }
            case "ANIMATION": {
                if (animateDely>0 && delayTickTimer<animateDely-1) {
                    delayTickTimer++;
                    String frame = LegacyComponentSerializer.legacySection().serialize(frames[animateTimer]);
                    return frame;
                }
                String frame = LegacyComponentSerializer.legacySection().serialize(frames[animateTimer]);
                delayTickTimer=0;
                animateTimer++;
                return frame;
            }
            default: {
                return "";
            }
        }
    }
}
