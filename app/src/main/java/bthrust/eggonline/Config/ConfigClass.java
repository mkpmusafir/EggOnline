package bthrust.eggonline.Config;

/**
 * Created by win-3 on 1/2/2018.
 */

public class ConfigClass {

    public static final String BASE_URL = "http://btservicenow.com/Online-egg/api/public/";
    public static final String LOGIN_URL = "http://btservicenow.com/Online-egg/wp-json/jwt-auth/v1/token";
  //  public static final String PRODUCT_URL = "http://btservicenow.com/Online-egg/wc-api/v3/products";
    public static final String PRODUCT_CATEGORY_URL = BASE_URL+"products";
    public static final String CATEGORY_URL = BASE_URL+"categories";
    public static final String SIGNUP_URL = BASE_URL+"user/signup";
    public static final String PAYMOD_CHECK = BASE_URL+"checkout";
    public static final String MYORDER_URL = BASE_URL+"user/orders";
    public static final String MYORDERVIEW_URL = BASE_URL+"user/order";


    // SharedPreferences ========================================

    public static final String SHARE_PREF = "MySharePref";
}
