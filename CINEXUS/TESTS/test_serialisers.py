import unittest
from rest_framework.test import APITestCase
from API.serialisers import MovieSerializer, ShowtimeSerializer, BookingSerializer, UserProfileSerializer, TheaterSerializer, UserSerializer, MembershipOptionsSerializer
from API.models import Movie, Theater, Showtime, Booking, UserProfile
from django.contrib.auth.models import User

class MovieSerializerTest(APITestCase):
    def test_movie_serializer(self):
        movie = Movie.objects.create(title="Movie1", shows={}, desc="Description1")
        serializer = MovieSerializer(movie)
        self.assertEqual(serializer.data["title"], "Movie1")
        self.assertEqual(serializer.data["desc"], "Description1")

class ShowtimeSerializerTest(APITestCase):
    def test_showtime_serializer(self):
        movie = Movie.objects.create(title="Movie2", shows={}, desc="Description2")
        theater = Theater.objects.create(name="Theater2", location="Location2")
        showtime = Showtime.objects.create(movie=movie, theater=theater)
        serializer = ShowtimeSerializer(showtime)
        self.assertEqual(serializer.data["movie"], movie.id)
        self.assertEqual(serializer.data["theater"], theater.id)

class BookingSerializerTest(APITestCase):
    def test_booking_serializer(self):
        movie = Movie.objects.create(title="Movie3", shows={}, desc="Description3")
        booking = Booking.objects.create(movie=movie, user_id=1, seats=5, payment_method="Card", num_tickets=5, total_price=50.00)
        serializer = BookingSerializer(booking)
        self.assertEqual(serializer.data["movie"], movie.id)
        self.assertEqual(serializer.data["user_id"], 1)

class UserProfileSerializerTest(APITestCase):
    def test_user_profile_serializer(self):
        user_profile = UserProfile.objects.create(rewards_points=10, membership_type='Regular')
        serializer = UserProfileSerializer(user_profile)
        self.assertEqual(serializer.data["rewards_points"], 10)

class TheaterSerializerTest(APITestCase):
    def test_theater_serializer(self):
        theater = Theater.objects.create(name="Theater3", location="Location3")
        serializer = TheaterSerializer(theater)
        self.assertEqual(serializer.data["name"], "Theater3")
        self.assertEqual(serializer.data["location"], "Location3")

class UserSerializerTest(APITestCase):
    def test_user_serializer(self):
        user_data = {"username": "testuser", "password": "testpassword"}
        serializer = UserSerializer(data=user_data)
        self.assertTrue(serializer.is_valid())
        user_obj = serializer.save()
        self.assertEqual(user_obj.username, "testuser")
        self.assertTrue(user_obj.check_password("testpassword"))

class MembershipOptionsSerializerTest(APITestCase):
    def test_membership_options_serializer(self):
        valid_data = {"membership_type": "Regular"}
        invalid_data = {"membership_type": "InvalidType"}
        
        valid_serializer = MembershipOptionsSerializer(data=valid_data)
        invalid_serializer = MembershipOptionsSerializer(data=invalid_data)
        
        self.assertTrue(valid_serializer.is_valid())
        self.assertFalse(invalid_serializer.is_valid())

if __name__ == "__main__":
    unittest.main()