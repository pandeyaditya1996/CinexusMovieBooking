from django.urls import path
from . import views


movie_urlpatterns = [
    # path('movies/', views.MovieList.as_view(), name='movie-list'),
    path('movies/', views.MovieList.as_view(), name='movies_view'),
    path('movies/<int:pk>/', views.MovieDetail.as_view(), name='movie-detail'),
]

showtime_urlpatterns = [
    path('showtimes/', views.ShowtimeList.as_view(), name='showtime-list'),
    path('showtimes/<int:pk>/', views.ShowtimeDetail.as_view(), name='showtime-detail'),    
]

booking_urlpatterns = [
    path('bookings/', views.BookingList.as_view(), name='booking-list'),
    path('bookings/<int:pk>/', views.BookingDetail.as_view(), name='booking-detail'),
]

# admin_urlpatterns = [
#     path('admin/movies/<int:pk>/', views.MovieCRUD.as_view(), name='admin-movie-crud'),
#     path('admin/showtimes/<int:pk>/', views.ShowtimeCRUD.as_view(), name='admin-showtime-crud'),
#     path('admin/theaters/<int:pk>/', views.TheaterCRUD.as_view(), name='admin-theater-crud'),    
# ]

theater_urlpatterns = [
      path('theaters/', views.GetTheaters.as_view(), name='get-theaters'),
    path('theaters/<int:pk>/', views.TheaterCRUD.as_view(), name='theater-detail'),
]

admin_urlpatterns = [
    path('admin/<int:user_id>/change-seating/<int:theater_id>/', views.TheaterCRUD.as_view(), name='change-seating'),
    path('admin/<int:user_id>/update-discount/<int:theater_id>/', views.ShowtimeCRUD.as_view(), name='update-discount'),
    # path('admin/<int:pk>/view-analytics/', views.TheaterCRUD.as_view(), name='change-seating'), 
    # also add paths for add/remove/update
]

extra_urlpatterns = [
    path('accumulate-points/', views.AccumulatePoints.as_view(), name='accumulate-points'),
    path('book-multiple-seats/', views.MultiSeatBooking.as_view(), name='book-multiple-seats'),
]

# URL patterns for user registration and login
user_urlpatterns = [
    path('register/', views.RegisterView.register, name='register'),
    path('login/', views.LoginView.as_view(), name='login'),
]

# URL pattern for fetching membership options
membership_urlpatterns = [
    path('membership-options/', views.MembershipOptionsView.as_view(), name='membership-options'),
]

# URL patterns for user booking summary
user_summary_urlpatterns = [
    path('user/<int:user_id>/summary/', views.user_booking_summary, name='user-booking-summary'),
]

# URL patterns for movies watched by the user in the past 30 days
movies_watched_urlpatterns = [
    path('user/<int:user_id>/movies-watched/', views.movies_watched, name='movies-watched'),
]

# New URL pattern for movies by location with user ID
movies_by_location_urlpatterns = [
    path('movies-by-location/', views.MoviesByLocation.as_view(), name='movies-by-location'),
]


theaters_by_location_urlpatterns = [
    path('theaters-by-location/', views.TheatersByLocation.as_view(), name='theaters-by-location'),
]


# New URL pattern for movies by theater with 'admin' prefix and user ID
movies_by_theater_urlpatterns = [
    path('movies-by-theater/<int:pk>/', views.MoviesByTheaterId.as_view(), name='movies-by-theater'),
]


# New URL pattern for deleting movies and related entries
delete_movie_urlpatterns = [
    path('admin/<int:user_id>/delete-movie/<int:movie_id>/', views.DeleteMovieAndRelatedEntries.as_view(), name='delete-movie'),
]

# New URL pattern for deleting theater and related entries
delete_theater_urlpatterns = [
    path('admin/<int:user_id>/delete-theater/<int:theater_id>/', views.DeleteTheaterAndRelatedEntries.as_view(), name='delete-theater'),
]

# New URL pattern for deleting a showtime id
delete_showtime_urlpatterns = [
    path('admin/<int:user_id>/delete-showtime/<int:showtime_id>/', views.DeleteShowtime.as_view(), name='delete-showtime'),
]

# New URL pattern for updating a showtime with 'admin' prefix
update_showtime_urlpatterns = [
    path('admin/<int:user_id>/update-showtime/<int:schedule_id>/', views.UpdateShowtime.as_view(), name='update-showtime'),
]

# New URL pattern for updating a movie with 'admin' prefix
update_movie_urlpatterns = [
    path('admin/<int:user_id>/update-movie/<int:movie_id>/', views.UpdateMovie.as_view(), name='update-movie'),
]

# New URL pattern for updating a theater with 'admin' prefix
update_theater_urlpatterns = [
    path('admin/<int:user_id>/update-theater/<int:theater_id>/', views.UpdateTheater.as_view(), name='update-theater'),
]

# New URL pattern for adding a movie with 'admin' prefix
add_movie_urlpatterns = [
    path('admin/<int:user_id>/add-movie/', views.AddMovie.as_view(), name='add-movie'),
]

# URL pattern for adding a theater with 'admin' prefix
add_theater_urlpatterns = [
    path('admin/<int:user_id>/add-theater/', views.AddTheater.as_view(), name='add-theater'),
]

# New URL pattern for adding a showtime with 'admin' prefix
add_showtime_urlpatterns = [
    path('admin/<int:user_id>/add-showtime/', views.AddShowtime.as_view(), name='add-showtime'),
]

# New URL pattern for getting theaters and showtimes by movie with 'admin' prefix
theaters_showtime_by_movies_urlpatterns = [
    path('theaters-showtime-by-movies/', views.TheatersShowtimeByMovies.as_view(), name='theaters-showtime-by-movies'),
]

create_booking_urlpatterns = [path('user/create-booking/', views.CreateBookingView.as_view(), name='create-booking'),
]

cancel_booking_urlpatterns = [path('user/cancel-booking/<int:booking_id>/', views.CancelBookingView.as_view(), name='cancel-booking'),
]

seatMapbyIds_urlpatterns = [path('seatmap-by-ids/', views.SeatmapByIds.as_view(), name='seatmap-by-ids'),
]

# URL pattern for fetching user rewards
rewards_by_user_urlpatterns = [
    path('rewards-by-user/<int:user_id>/', views.RewardsByUserId.as_view(), name='rewards-by-user'),
]

update_membership_urlpatterns = [
    path('update-membership/', views.UpdateMembership.as_view(), name='update-membership'),
]

analytics_by_movies_urlpatterns = [
    path('admin/<int:user_id>/analytics-by-movies/', views.AnalyticsByMovies.as_view(), name='analytics-by-movies'),
]

# URL pattern for AnalyticsByLocation
analytics_by_location_urlpatterns = [
    path('admin/<int:user_id>/analytics-by-location/', views.AnalyticsByLocation.as_view(), name='analytics-by-location'),
]

# Combining all URL patterns
urlpatterns = analytics_by_location_urlpatterns + analytics_by_movies_urlpatterns + update_membership_urlpatterns + rewards_by_user_urlpatterns + seatMapbyIds_urlpatterns + cancel_booking_urlpatterns + theaters_by_location_urlpatterns + create_booking_urlpatterns + theaters_showtime_by_movies_urlpatterns + add_showtime_urlpatterns + add_theater_urlpatterns + add_movie_urlpatterns + update_theater_urlpatterns + update_movie_urlpatterns + update_showtime_urlpatterns + delete_showtime_urlpatterns + delete_theater_urlpatterns + delete_movie_urlpatterns + movies_by_location_urlpatterns + movies_by_theater_urlpatterns + movie_urlpatterns + theater_urlpatterns + showtime_urlpatterns + booking_urlpatterns + admin_urlpatterns + extra_urlpatterns + user_urlpatterns + membership_urlpatterns + user_summary_urlpatterns + movies_watched_urlpatterns

