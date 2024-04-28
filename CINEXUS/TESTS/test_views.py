from django.test import TestCase, APITestCase
from django.urls import reverse, resolve
from API import views
from API.models import Movie, Showtime, Booking, Theater
from django.contrib.auth.models import User

class MovieViewsTest(APITestCase):
    def test_movie_list(self):
        response = self.client.get(reverse('movie-list'))
        self.assertEqual(response.status_code, 200)
        
    def test_movie_detail(self):
        movie = Movie.objects.create(title="Test Movie", desc="Test Description")
        response = self.client.get(reverse('movie-detail', args=[movie.id]))
        self.assertEqual(response.status_code, 200)

class ShowtimeViewsTest(APITestCase):
    def test_showtime_list(self):
        response = self.client.get(reverse('showtime-list'))
        self.assertEqual(response.status_code, 200)
        
    def test_showtime_detail(self):
        movie = Movie.objects.create(title="Test Movie for Showtime", desc="Test Description for Showtime")
        showtime = Showtime.objects.create(movie=movie, theater=Theater.objects.create(name="Test Theater for Showtime", location="LocationTest for Showtime"))
        response = self.client.get(reverse('showtime-detail', args=[showtime.id]))
        self.assertEqual(response.status_code, 200)

class BookingViewsTest(APITestCase):
    def test_booking_list(self):
        response = self.client.get(reverse('booking-list'))
        self.assertEqual(response.status_code, 200)
        
    def test_booking_detail(self):
        movie = Movie.objects.create(title="Test Movie for Booking", desc="Test Description for Booking")
        booking = Booking.objects.create(movie=movie, user_id=1)
        response = self.client.get(reverse('booking-detail', args=[booking.id]))
        self.assertEqual(response.status_code, 200)

class AccumulatePointsViewTest(APITestCase):
    def test_accumulate_points(self):
        response = self.client.post(reverse('accumulate-points'))
        self.assertEqual(response.status_code, 200)

class MultiSeatBookingViewTest(APITestCase):
    def test_multi_seat_booking(self):
        response = self.client.post(reverse('book-multiple-seats'))
        self.assertEqual(response.status_code, 200)
        
class TheaterViewsTest(APITestCase):
    def test_theater_list(self):
        response = self.client.get(reverse('theater-list'))
        self.assertEqual(response.status_code, 200)
        
    def test_theater_detail(self):
        theater = Theater.objects.create(name="TheaterTest", location="LocationTest")
        response = self.client.get(reverse('theater-detail', args=[theater.id]))
        self.assertEqual(response.status_code, 200)

class UserViewsTest(APITestCase):
    def test_register(self):
        user_data = {"username": "testuser", "password": "testpassword"}
        response = self.client.post(reverse('register'), data=user_data)
        self.assertEqual(response.status_code, 201)
        
    def test_login(self):
        User.objects.create_user(username="testuser", password="testpassword")
        login_data = {"username": "testuser", "password": "testpassword"}
        response = self.client.post(reverse('login'), data=login_data)
        self.assertEqual(response.status_code, 200)

class MembershipViewsTest(APITestCase):
    def test_membership_options(self):
        response = self.client.get(reverse('membership-options'))
        self.assertEqual(response.status_code, 200)
