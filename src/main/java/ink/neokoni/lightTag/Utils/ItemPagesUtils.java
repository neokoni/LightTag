package ink.neokoni.lightTag.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemPagesUtils {
    public static List<Integer> getThisPageIds(List<Integer> all, int pageSize, int page) {
        page -=1;
        List<Integer> result = new ArrayList<>();
        int start = page*pageSize;
        int end = start+pageSize;

        if (end>all.size())end=all.size();

        for (int i = start; i <end; i++) {
            result.add(all.get(i));
        }

        return result;
    }

    public static int getMaxPage(List<Integer> all, int pageSize) {
        int result = 0;
        result += all.size()/pageSize;
        if (all.size()%pageSize!=0)result++;
        return result;
    }
}
