package icynote.ui;


import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;

public class CurrentPreferences {

    private static OrderBy orderBy = OrderBy.TITLE;

    public static void setOrderBy(OrderBy newOrder){
        orderBy = newOrder;
    }

    public static OrderBy getOrderBy(){
        return orderBy;
    }

    private static OrderType orderType = OrderType.ASC;

    public static void setOrderType(OrderType newOrder){
        orderType = newOrder;
    }

    public static OrderType getOrderType(){
        return orderType;
    }
}
