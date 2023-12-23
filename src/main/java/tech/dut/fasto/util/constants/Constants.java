package tech.dut.fasto.util.constants;

public class Constants {

    private Constants() {}

    public static final Integer DIGIT_CODE_LENGTH = 6;
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    public static final String TEMPLATE_ACTIVE_CODE = "active_code";

    public static final String TEMPLATE_ACTIVE_SHOP_CODE = "active_shop";

    public static final String TEMPLATE_ACTIVE_BUSINESS_CODE = "active_shop";

    public static final String ACTIVE_SHOP = "Active shop";
    public static final String ACTIVE_BUSINESS = "Active business";

    public static final String ACTIVE_ACCOUNT_BY_CODE = "Active account by code";

    public static final String FORGOT_PASSWORD_BY_CODE = "Forgot password by code";

    public static final String TEMPLATE_FORGOT_PASSWORD_CODE = "active_code";

    public static final String GOOGLE_INFO_URI = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    public static final String GOOGLE_INFO_URI_BIRTHDAY_GENDER = "https://people.googleapis.com/v1/people/me?personFields=genders,birthdays&access_token=";
    public static final String FACEBOOK_INFO_URI = "https://graph.facebook.com/v14.0/me?fields=id,first_name,middle_name,last_name,name,email,picture,birthday,gender";

    /**
     * Aws
     */
    public static final String AWS_PRODUCT_IMAGE_FOLDER = "photo/product/";
    public static final String AWS_SHOP_IMAGE_FOLDER = "photo/banner/";
    public static final String AWS_VOUCHER_IMAGE_FOLDER = "photo/voucher/";
    public static final String AWS_USER_IMAGE_FOLDER = "photo/user/";
    public static final String AWS_CATEGORY_IMAGE_FOLDER = "photo/category/";
    public static final String AWS_BILL_IMAGE_FOLDER = "photo/bill/";
    public static final String AWS_NEWS_IMAGE_FOLDER = "photo/news/";
    public static final String AWS_REVIEW_IMAGE_FOLDER = "photo/review/";
    public static final String AWS_PHOTO_FOLDER = "photo/";
    public static final String SLASH = "/";
    public static final char CHAR_HYPHEN = '-';
    public static final String SPACE = " ";
    public static final String UNDER_LINE = "_";

    public static final String HYPHEN = "-";

    public static final CharSequence PLUS = "+";


    public static final String BREAK_POINT = ".";

    public static final String S3 = "s3";

    public static final String URL_AMAZON = "amazonaws.com";

    public static final String VNP_DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String WEB_VIEW_NEWS = "<!DOCTYPE html><html lang=en><head><meta charset=UTF-8><meta http-equiv=X-UA-Compatible content=IE=edge> <meta name=viewport content=width=device-width, initial-scale=1.0> <title>Dapass Detail News</title> <style> * {font-family: 'NanumBarunGothic';} body {margin: 0;padding: 0;} img {width: 100%%; height: auto;} .header {position: relative;} .header_content {position: absolute;width: 100%%;top: 0px;padding-top: 80px;color: #FFFFFF;background: linear-gradient(180deg, rgba(0, 0, 0, 0.45) 31.77%%, rgba(0, 0, 0, 0) 100%%);} .no_wrap {line-height: 1.6rem;overflow: hidden;display: -webkit-box;-webkit-line-clamp: 2;-webkit-box-orient: vertical;max-height: 3.2rem;}  .title {font-size: 20px;font-weight: 600;margin: 10px 15px;} .sub_title {margin: 10px 15px;font-size: 16px;font-weight: 400;} .content {padding: 20px 16px;} .content img {padding: 10px 0px;} </style> </head> <body> <div class=header><img src=%s alt=> <div class=header_content> <h4 class=title no_wrap> %s </h4> <p class=sub_title no_wrap> %s </p> </div></div> <div class=content> %s </div> </body></html>";

    public static final String SORT_OLDEST_TO_NEWEST = "SORT_OLDEST_TO_NEWEST";
    public static final String SORT_NEWEST_TO_OLDEST = "SORT_NEWEST_TO_OLDEST";

    /**
     * VNPay
     */
    public static final String VNP_TOKEN_KEY = "vnp_token";
    public static final String VNP_VERSION_KEY = "vnp_version";
    public static final String VNP_TIMEZONE = "Asia/Ho_Chi_Minh";
    public static final String VNP_ORDER_INFO_VALUE = "Thanh toan cho don hang %s";
    public static final String VNP_TXT_REF_ORDER = "%s-%s-ORDER";

    public static final String TITLE = "FASTO";

    public static final String TOKEN_CREATE = "token_create";
    public static final String PAY_AND_CREATE = "pay_and_create";
    public static final String TOKEN_PAY = "token_pay";

}
