import unittest
from django.test import TestCase
from API.models import Theater, Movie, Showtime, Booking, UserProfile

class TheaterModelTest(TestCase):
    def setUp(self):
        self.theater = Theater.objects.create(name="Theater1", location="Location1")

    def test_theater_creation(self):
        self.assertEqual(self.theater.name, "Theater1")
        self.assertEqual(self.theater.location, "Location1")

class MovieModelTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="Movie1", shows={}, desc="Description1")

    def test_movie_creation(self):
        self.assertEqual(self.movie.title, "Movie1")
        self.assertEqual(self.movie.desc, "Description1")

class ShowtimeModelTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="Movie2", shows={}, desc="Description2")
        self.theater = Theater.objects.create(name="Theater2", location="Location2")
        self.showtime = Showtime.objects.create(movie=self.movie, theater=self.theater)

    def test_showtime_creation(self):
        self.assertEqual(self.showtime.movie, self.movie)
        self.assertEqual(self.showtime.theater, self.theater)

class BookingModelTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="Movie3", shows={}, desc="Description3")
        self.booking = Booking.objects.create(movie=self.movie, user_id=1, seats=5, payment_method="Card", num_tickets=5, total_price=50.00)

    def test_booking_creation(self):
        self.assertEqual(self.booking.movie, self.movie)
        self.assertEqual(self.booking.user_id, 1)
        self.assertEqual(self.booking.seats, 5)
        self.assertEqual(self.booking.payment_method, "Card")

class UserProfileModelTest(TestCase):
    def setUp(self):
        self.user_profile = UserProfile.objects.create(rewards_points=10, membership_type='Regular')

    def test_user_profile_creation(self):
        self.assertEqual(self.user_profile.rewards_points, 10)
        self.assertEqual(self.user_profile.membership_type, 'Regular')

class MovieJSONFieldTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="MovieJSON", shows={"date": "2023-10-09", "time": "19:00"}, desc="JSON Description")

    def test_movie_json_field(self):
        self.assertEqual(self.movie.shows, {"date": "2023-10-09", "time": "19:00"})

class ShowtimeDateTimeTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="DateTimeMovie", shows={}, desc="DateTime Description")
        self.theater = Theater.objects.create(name="DateTimeTheater", location="LocationDateTime")
        self.showtime = Showtime.objects.create(movie=self.movie, theater=self.theater, start_time="2023-10-09T19:00:00Z")

    def test_showtime_date_time(self):
        self.assertEqual(str(self.showtime.start_time), "2023-10-09 19:00:00+00:00")

class BookingFieldsTest(TestCase):
    def setUp(self):
        self.movie = Movie.objects.create(title="BookingFieldsMovie", shows={}, desc="BookingFields Description")
        self.showtime = Showtime.objects.create(movie=self.movie, theater=Theater.objects.create(name="BookingFieldsTheater", location="LocationBookingFields"))
        self.booking = Booking.objects.create(movie=self.movie, user_id=1, seats=5, payment_method="Card", showtime=self.showtime, num_tickets=5, total_price=50.00)

    def test_booking_fields(self):
        self.assertEqual(self.booking.num_tickets, 5)
        self.assertEqual(self.booking.total_price, 50.00)

if __name__ == "__main__":
    unittest.main()










