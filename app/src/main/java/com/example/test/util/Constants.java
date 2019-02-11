package com.example.test.util;

public class Constants {
    public static final String API_KEY = "51668be3a6241efa5b10e7f7607d1a20";

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String CHANGES_URL = "changes?api_key=";
    public static final String CHANGES_START_DATE_URL = "&start_date=";
    public static final String CHANGES_END_DATE_URL = "&end_date=";

    public static final String DETAILS_URL = "?api_key=";

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_FILE_SIZE = "w500";
    public static final String IMAGE_FILE_SIZE_FOR_DETAILS = "w780";

    public static final int MAX_NR_OF_DAYS = 14;

    public static final int MAX_NR_OF_API_REQUESTS = 30;

    public static final int TOO_MANY_API_CALLS_RESPONSE_CODE = 429;
    public static final int BAD_API_KEY_RESPONSE_CODE = 401;
}
