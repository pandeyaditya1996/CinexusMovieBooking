import unittest
from django.urls import reverse, resolve
from . import views

class MovieURLTest(unittest.TestCase):
    def test_movie_list_url(self):
        url = reverse('movie-list')
        self.assertEqual(resolve(url).func.view_class, views.MovieList)
        
    def test_movie_detail_url(self):
        url = reverse('movie-detail', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.MovieDetail)

class ShowtimeURLTest(unittest.TestCase):
    def test_showtime_list_url(self):
        url = reverse('showtime-list')
        self.assertEqual(resolve(url).func.view_class, views.ShowtimeList)
        
    def test_showtime_detail_url(self):
        url = reverse('showtime-detail', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.ShowtimeDetail)

class BookingURLTest(unittest.TestCase):
    def test_booking_list_url(self):
        url = reverse('booking-list')
        self.assertEqual(resolve(url).func.view_class, views.BookingList)
        
    def test_booking_detail_url(self):
        url = reverse('booking-detail', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.BookingDetail)

class AdminURLTest(unittest.TestCase):
    def test_movie_crud_url(self):
        url = reverse('admin-movie-crud', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.MovieCRUD)
        
    def test_showtime_crud_url(self):
        url = reverse('admin-showtime-crud', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.ShowtimeCRUD)
        
    def test_theater_crud_url(self):
        url = reverse('admin-theater-crud', args=[1])
        self.assertEqual(resolve(url).func.view_class, views.TheaterCRUD)

class ExtraURLTest(unittest.TestCase):
    def test_accumulate_points_url(self):
        url = reverse('accumulate-points')
        self.assertEqual(resolve(url).func.view_class, views.AccumulatePoints)
        
    def test_book_multiple_seats_url(self):
        url = reverse('book-multiple-seats')
        self.assertEqual(resolve(url).func.view_class, views.MultiSeatBooking)

class UserURLTest(unittest.TestCase):
    def test_register_url(self):
        url = reverse('register')
        self.assertEqual(resolve(url).func.view_class, views.RegisterView)

    def test_login_url(self):
        url = reverse('login')
        self.assertEqual(resolve(url).func, views.login)

class MembershipURLTest(unittest.TestCase):
    def test_membership_options_url(self):
        url = reverse('membership-options')
        self.assertEqual(resolve(url).func.view_class, views.MembershipOptionsView)

