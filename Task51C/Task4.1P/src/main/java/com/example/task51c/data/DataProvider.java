package com.example.task51c.data;

import com.example.task51c.models.NewsItem;
import java.util.ArrayList;
import java.util.List;
public class DataProvider {
    public static List<NewsItem> getNewsList() {
        List<NewsItem> list = new ArrayList<>();
        //Featured item (isFeatured = true)
        list.add(new NewsItem(1, "Messi scores hat-trick", "Lionel Messi scored three goals in a thrilling match against Real Betis.", "messi", "Football", true));
        list.add(new NewsItem(2, "LeBron James injury update", "LeBron out for 2 weeks with ankle soreness.", "lebron", "Basketball", true));
        list.add(new NewsItem(3, "Australia wins Cricket World Cup", "Aussies beat India in a nail-biting final.", "cricket_worldcup", "Cricket", true));
        list.add(new NewsItem(4, "Champions League quarterfinals set", "Real Madrid to face Bayern Munich.", "ucl", "Football", true));

        //Normal new (isFeatured = false)
        list.add(new NewsItem(5, "NBA playoffs race heats up", "Celtics and Bucks secure top seeds.", "nba_playoffs", "Basketball", false));
        list.add(new NewsItem(6, "IPL 2025: New season starts", "Exciting young talents emerge.", "ipl", "Cricket", false));
        list.add(new NewsItem(7, "Manchester City extends lead", "Haaland scores again.", "mancity", "Football", false));
        list.add(new NewsItem(8, "Steph Curry breaks 3-point record", "Warriors star makes history.", "curry", "Basketball", false));
        list.add(new NewsItem(9, "Ashes series drawn 2-2", "Last test ends in dynamic tie.", "ashes", "Cricket", false));
        list.add(new NewsItem(10, "Transfer window rumours", "Mbappe linked to Liverpool.", "transfer", "Football", false));
        return list;
    }
}
