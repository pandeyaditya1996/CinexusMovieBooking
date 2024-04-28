package com.cmpe.cosmos.data.constant

object ApiEndpoints {
    const val BASE_URL = "http:/alb-5Dec01-407379133.us-west-1.elb.amazonaws.com:8000/"

    const val ENDPOINT_LOGIN = "login/"
    const val ENDPOINT_REGISTER = "register/"

    const val ENDPOINT_MOVIES = "movies-by-location/"

    const val ENDPOINT_THEATERS = "theaters-by-location/"

    const val ENDPOINT_MOVIES_BY_THEATERS = "movies-by-theater/"

    const val ENDPOINT_THEATERS_SHOWTIME_BY_MOVIES = "theaters-showtime-by-movies/"

    const val ENDPOINT_SEATMAP_BY_ID = "seatmap-by-ids/"

    const val ENDPOINT_CREATE_BOOKING = "user/create-booking/"

    const val ENDPOINT_USER_BOOKINGS = "bookings/"

    const val ENDPOINT_USER = "user/"

    const val ENDPOINT_CANCEL_BOOKING = "user/cancel-booking/"

    const val ENDPOINT_MOVIES_WATCHED = "movies-watched/"

    const val ENDPOINT_REWARD_POINTS = "rewards-by-user/"

    const val ENDPOINT_UPDATE_MEMBERSHIP = "update-membership/"

    const val BASE_TMDB_URL = "https://image.tmdb.org/t/p/"

    const val BANNER = "w1280/"

    const val POSTER = "w185/"
}