package com.lemberskay.reactpizza.repository.mapper;

public final class ColumnName {

    //MENU ITEM
    public static final String MENU_ITEM_ID = "menu_item_id";
    public static final String MENU_ITEM_NAME = "menu_item_name";
    public static final String MENU_ITEM_PRICE = "price";
    public static final String MENU_ITEM_DESCRIPTION = "description";
    public static final String MENU_ITEM_IMG_URL = "menu_item_img";
    public static final String MENU_ITEM_RATING = "rating";

    //CATEGORY
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_IMG_URL = "category_img";

    //USER
    public static final String USER_ID = "user_id";
    public static final String USER_LOGIN = "user_name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "user_role";

    //COUNTRY
    public static final String COUNTRY_ID = "country_id";
    public static final String COUNTRY_NAME = "country_name";

    //ADDRESS
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_STREET_NAME = "street_name";
    public static final String ADDRESS_STREET_NUMBER = "street_number";
    public static final String ADDRESS_CITY = "city";

    //ORDER
    public  static final String ORDER_ID = "order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_MENU_ITEM_QUANTITY = "quantity";

    private ColumnName(){}
}
