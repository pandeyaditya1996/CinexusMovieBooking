from rest_framework import generics, permissions
from rest_framework.exceptions import ValidationError
from .models import Movie, Theater, Showtime, Booking, UserProfile, Location, TheaterOwner, SeatMap
from .serialisers import MovieSerializer, TheaterSerializer, ShowtimeSerializer, BookingSerializer, UserProfileSerializer, UserSerializer, MembershipOptionsSerializer
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from django.http import JsonResponse
from .serialisers import MovieSerializer 
from rest_framework.views import APIView
from rest_framework.renderers import JSONRenderer
from django.shortcuts import get_object_or_404
from rest_framework import status
from django.core.validators import RegexValidator
from rest_framework.permissions import IsAuthenticated
from django.utils import timezone
from decimal import Decimal, InvalidOperation
from django.db.models import Sum
from datetime import timedelta
from django.db import transaction
from django.core.exceptions import ValidationError
from .const import *
from datetime import datetime, time
from itertools import filterfalse
import datetime as dt



class AnalyticsByLocation(APIView):
    def get(self, request, user_id):
        try:
            theater_owner = TheaterOwner.objects.get(user_id=user_id)
        except TheaterOwner.DoesNotExist:
            return Response({"error": "Theater owner not found"}, status=status.HTTP_404_NOT_FOUND)

        user = UserProfile.objects.get(user_id=user_id)
        theater_ids = [int(id) for id in theater_owner.theater_list.split(',')]
        theaters = Theater.objects.filter(theater_id__in=theater_ids)
        today = dt.date.today()

        # Initialize the response structure
        analytics = {
            "owner_name": user.username,
            "theaters_managed": list(theaters.values_list('name', flat=True)),
            "locations": {}
        }

        for theater in theaters:
            location_name = theater.location.name
            if location_name not in analytics["locations"]:
                analytics["locations"][location_name] = {"Theaters": {}}

            theater_analytics = analytics["locations"][location_name]["Theaters"].setdefault(f"Theater: {theater.name}", {})

            showtimes = Showtime.objects.filter(theater_id=theater.theater_id)

            for days in [30, 60, 90]:
                date_threshold = today - dt.timedelta(days=days)
                relevant_bookings = Booking.objects.filter(
                    schedule_id__in=showtimes.values_list('schedule_id', flat=True),
                    booking_timestamp__gte=date_threshold
                )
                total_seats = relevant_bookings.aggregate(Sum('number_of_seats'))['number_of_seats__sum'] or 0
                theater_analytics[f'last_{days}_days'] = total_seats

        return Response(analytics)




class AnalyticsByMovies(APIView):
    def get(self, request, user_id):
        try:
            theater_owner = TheaterOwner.objects.get(user_id=user_id)
        except TheaterOwner.DoesNotExist:
            return Response({"error": "Theater owner not found"}, status=status.HTTP_404_NOT_FOUND)

        user = UserProfile.objects.get(user_id=user_id)
        theater_ids = [int(id) for id in theater_owner.theater_list.split(',')]
        theaters = Theater.objects.filter(theater_id__in=theater_ids)
        today = dt.date.today()

        # Initialize the response structure
        analytics = {
            "owner_name": user.username,
            "theaters_managed": list(theaters.values_list('name', flat=True)),
            "movies": []
        }

        for theater in theaters:
            showtimes = Showtime.objects.filter(theater_id=theater.theater_id)
            movie_ids = showtimes.values_list('movie_id', flat=True).distinct()

            for movie_id in movie_ids:
                movie_title = Movie.objects.get(movie_id=movie_id).title

                # Find or create a dictionary for this movie in the analytics
                movie_dict = next((item for item in analytics['movies'] if item.get("Movie") == movie_title), None)
                if not movie_dict:
                    movie_dict = {"Movie": movie_title, "Theaters": {}}
                    analytics['movies'].append(movie_dict)

                theater_analytics = movie_dict["Theaters"].setdefault(f"Theater: {theater.name}", {})

                for days in [30, 60, 90]:
                    date_threshold = today - dt.timedelta(days=days)
                    relevant_bookings = Booking.objects.filter(
                        schedule_id__in=showtimes.filter(movie_id=movie_id).values_list('schedule_id', flat=True),
                        booking_timestamp__gte=date_threshold
                    )
                    total_seats = relevant_bookings.aggregate(Sum('number_of_seats'))['number_of_seats__sum'] or 0
                    theater_analytics[f'last_{days}_days'] = total_seats

        return Response(analytics)






class UpdateMembership(APIView):
    def post(self, request):
        user_id = request.data.get('user_id')
        new_membership_type = request.data.get('membership_type')

        # Validate input
        if not user_id or not new_membership_type:
            return Response({'error': 'user_id and membership_type are required'}, status=status.HTTP_400_BAD_REQUEST)

        # Retrieve the user and update membership_type
        try:
            user = UserProfile.objects.get(user_id=user_id)
            user.membership_type = new_membership_type
            user.save()
            return Response({'message': 'Membership updated successfully'}, status=status.HTTP_200_OK)
        except UserProfile.DoesNotExist:
            return Response({'error': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class RewardsByUserId(APIView):
    def get(self, request, user_id):
        try:
            user = UserProfile.objects.get(user_id=user_id)
            return Response({'user_id': user.user_id, 'reward_points': user.reward_points}, status=status.HTTP_200_OK)
        except UserProfile.DoesNotExist:
            return Response({'error': 'User not found'}, status=status.HTTP_404_NOT_FOUND)


class SeatmapByIds(APIView):
    def post(self, request):
        # Extracting data from request
        movie_id = request.data.get('movie_id')
        theater_id = request.data.get('theater_id')
        showtime = request.data.get('showtime')
        date = request.data.get('date')

        if not all([movie_id, theater_id, showtime, date]):
            return Response({'error': 'All fields (title, theater_id, showtime, date) are required.'},
                            status=status.HTTP_400_BAD_REQUEST)

        try:
            # Fetch schedule_id from Showtime table
            schedule = Showtime.objects.get(movie_id=movie_id, theater_id=theater_id, showtime=showtime)
            schedule_id = schedule.schedule_id

            request_date = datetime.strptime(date, "%Y-%m-%d").date()

            # Fetch current_seatmap from SeatMap table
            seatmap = SeatMap.objects.get(schedule_id=schedule_id, date = request_date)
            current_seatmap = seatmap.current_seatmap

            return Response({'current_seatmap': current_seatmap, 'schedule_id': schedule_id})

        except Movie.DoesNotExist:
            return Response({'error': 'Movie not found'}, status=status.HTTP_404_NOT_FOUND)
        except Showtime.DoesNotExist:
            return Response({'error': 'Showtime not found'}, status=status.HTTP_404_NOT_FOUND)
        except SeatMap.DoesNotExist:
            return Response({'error': 'SeatMap not found'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class TheatersShowtimeByMovies(APIView):
    def post(self, request, *args, **kwargs):
        movie_id = request.data.get('movie_id')
        location_id = request.data.get('location_id')

        # Query theaters in the specified location
        theaters = Theater.objects.filter(location_id=location_id).distinct()

        theater_showtimes = []
        for theater in theaters:
            # Query showtimes for each theater and the specified movie
            showtimes = Showtime.objects.filter(theater_id=theater.theater_id, movie_id=movie_id).distinct()

            # Preparing showtimes, discounts, from_date, and to_date for the response
            showtime_data = [{
                'showtime': st.showtime.strftime('%H:%M'),
                'discount': st.discount,
                'from_date': st.from_date.strftime('%Y-%m-%d'),  # Format date as a string
                'to_date': st.to_date.strftime('%Y-%m-%d')      # Format date as a string
            } for st in showtimes]

            # Adding theater details and its showtimes to the list
            theater_showtimes.append({
                'theater_id': theater.theater_id,
                'name': theater.name,
                'seating_capacity': theater.seating_capacity,
                'distance': theater.distance,
                'coordinates': theater.coordinates,
                'address': theater.address,
                'showtimes': showtime_data
            })

        return Response(theater_showtimes, status=status.HTTP_200_OK)

class AddShowtime(APIView):
    def post(self, request, *args, **kwargs):
        # Extracting data from the request
        movie_id = request.data.get('movie_id')
        theater_id = request.data.get('theater_id')
        showtime = request.data.get('showtime')
        from_date = request.data.get('from_date')
        to_date = request.data.get('to_date')
        discount = request.data.get('discount')

        # Validate and convert dates and times
        try:
            from_date = datetime.strptime(from_date, '%Y-%m-%d').date()
            to_date = datetime.strptime(to_date, '%Y-%m-%d').date()
            showtime = datetime.strptime(showtime, '%H:%M:%S').time()  # Updated format
        except ValueError as e:
            return Response({"error": f"Date/Time conversion error: {str(e)}"}, status=status.HTTP_400_BAD_REQUEST)

        # Creating a new showtime instance
        try:
            new_showtime = Showtime(
                movie_id=movie_id,
                theater_id=theater_id,
                showtime=showtime,
                from_date=from_date,
                to_date=to_date,
                discount=discount
            )
            new_showtime.full_clean()  # Validate the showtime instance
            new_showtime.save()  # Save the new showtime to the database
            return Response({"message": "New showtime added successfully"}, status=status.HTTP_201_CREATED)
        except ValidationError as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)


class AddTheater(APIView):
    def post(self, request, user_id):
        # Extract theater details from the request
        location_id = request.data.get('location_id')
        name = request.data.get('name')
        seating_capacity = request.data.get('seating_capacity')
        distance = request.data.get('distance')
        coordinates = request.data.get('coordinates')
        address = request.data.get('address')

        with transaction.atomic():
            # Create and save the new Theater
            new_theater = Theater.objects.create(
                location_id=location_id,
                name=name,
                seating_capacity=seating_capacity,
                distance=distance,
                coordinates=coordinates,
                address=address
            )

            # Update TheaterOwner's theater list
            try:
                theater_owner = TheaterOwner.objects.get(user_id=user_id)
                theater_list = theater_owner.theater_list.split(',')
                theater_list.append(str(new_theater.theater_id))
                theater_list.sort(key=int)  # Sort the list of theater IDs
                theater_owner.theater_list = ','.join(theater_list)
                theater_owner.save()
            except TheaterOwner.DoesNotExist:
                # Handle the case where TheaterOwner does not exist for the given user_id
                return Response({"error": "Theater owner not found"}, status=status.HTTP_404_NOT_FOUND)

        return Response({"message": "Theater added successfully"}, status=status.HTTP_201_CREATED)

class AddMovie(APIView):
    def post(self, request, *args, **kwargs):
        # Extracting data from the request
        title = request.data.get('title')
        description = request.data.get('description')
        release_date = request.data.get('release_date')
        duration = request.data.get('duration')
        genre = request.data.get('genre')
        rating = request.data.get('rating')
        poster_url = request.data.get('poster_url')
        banner_url = request.data.get('banner_url')
        censor_rating = request.data.get('censor_rating', False)  # Default to False if not provided
        language = request.data.get('language')
        cast_and_crew = request.data.get('cast_and_crew')
        director = request.data.get('director')
        currently_running = request.data.get('currently_running', 0)  # Default to 0 (not running) if not provided

        # Creating a new movie instance
        try:
            new_movie = Movie(
                title=title,
                description=description,
                release_date=release_date,
                duration=duration,
                genre=genre,
                rating=rating,
                poster_url=poster_url,
                banner_url=banner_url,
                censor_rating=censor_rating,
                language=language,
                cast_and_crew=cast_and_crew,
                director=director,
                currently_running=currently_running
            )
            new_movie.full_clean()  # Validate the movie instance
            new_movie.save()  # Save the new movie to the database
            return Response({"message": "New movie added successfully"}, status=status.HTTP_201_CREATED)
        except ValidationError as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)



class UpdateTheater(APIView):
    def post(self, request, theater_id, *args, **kwargs):
        # Retrieve the theater object based on the given theater_id
        theater = get_object_or_404(Theater, theater_id=theater_id)

        # Update fields from the request
        theater.name = request.data.get('name', theater.name)
        theater.seating_capacity = request.data.get('seating_capacity', theater.seating_capacity)
        theater.distance = request.data.get('distance', theater.distance)
        theater.coordinates = request.data.get('coordinates', theater.coordinates)
        theater.address = request.data.get('address', theater.address)

        # Retrieve and update location if provided
        location_id = request.data.get('location_id')
        if location_id:
            theater.location = get_object_or_404(Location, location_id=location_id)

        # Save the updated theater
        theater.save()

        return Response({"message": "Theater updated successfully"}, status=status.HTTP_200_OK)

class UpdateMovie(APIView):
    def post(self, request, movie_id, *args, **kwargs):
        # Retrieve the movie object based on the given movie_id
        movie = get_object_or_404(Movie, movie_id=movie_id)

        # Update the fields from the request
        movie.title = request.data.get('title', movie.title)
        movie.description = request.data.get('description', movie.description)
        movie.release_date = request.data.get('release_date', movie.release_date)
        movie.duration = request.data.get('duration', movie.duration)
        movie.genre = request.data.get('genre', movie.genre)
        movie.rating = request.data.get('rating', movie.rating)
        movie.poster_url = request.data.get('poster_url', movie.poster_url)
        movie.banner_url = request.data.get('banner_url', movie.banner_url)
        movie.censor_rating = request.data.get('censor_rating', movie.censor_rating)
        movie.language = request.data.get('language', movie.language)
        movie.cast_and_crew = request.data.get('cast_and_crew', movie.cast_and_crew)
        movie.director = request.data.get('director', movie.director)
        movie.currently_running = request.data.get('currently_running', movie.currently_running)

        # Save the updated movie
        movie.save()

        # Return a success response
        return Response({"message": "Movie updated successfully"}, status=status.HTTP_200_OK)


class UpdateShowtime(APIView):
    def post(self, request, schedule_id, *args, **kwargs):
        # Retrieve the showtime object based on the given schedule_id
        showtime = get_object_or_404(Showtime, schedule_id=schedule_id)

        # Update the fields from the request
        showtime.movie_id = request.data.get('movie_id', showtime.movie_id)
        showtime.theater_id = request.data.get('theater_id', showtime.theater_id)
        showtime.showtime = request.data.get('showtime', showtime.showtime)
        showtime.from_date = request.data.get('from_date', showtime.from_date)
        showtime.to_date = request.data.get('to_date', showtime.to_date)
        showtime.discount = request.data.get('discount', showtime.discount)

        # Save the updated showtime
        showtime.save()

        # Return a success response
        return Response({"message": "Showtime updated successfully"}, status=status.HTTP_200_OK)

class DeleteShowtime(APIView):
    def delete(self, request, showtime_id, user_id=None):
        # Retrieve the showtime object based on the given showtime_id
        showtime = get_object_or_404(Showtime, schedule_id=showtime_id)

        # Delete the showtime
        showtime.delete()

        # Return a success response
        return Response({"message": "Showtime deleted successfully"}, status=status.HTTP_204_NO_CONTENT)

class DeleteTheaterAndRelatedEntries(APIView):
    def delete(self, request, theater_id, user_id=None):  # user_id is not used in this method
        with transaction.atomic():
            theater = Theater.objects.filter(theater_id=theater_id).first()
            if not theater:
                return Response({"message": "Theater not found"}, status=status.HTTP_404_NOT_FOUND)

            Showtime.objects.filter(theater_id=theater.theater_id).delete()

            theater_owners = TheaterOwner.objects.all()
            for owner in theater_owners:
                theater_list = owner.theater_list.split(',')
                if str(theater_id) in theater_list:
                    theater_list.remove(str(theater_id))
                    owner.theater_list = ','.join(theater_list)
                    owner.save()

            theater.delete()

            return Response({"message": "Theater and related entries deleted successfully"}, status=status.HTTP_204_NO_CONTENT)

class DeleteMovieAndRelatedEntries(APIView):
    def delete(self, request, movie_id, user_id=None):
        with transaction.atomic():
            movie = Movie.objects.filter(movie_id=movie_id).first()
            if not movie:
                return Response({"message": "Movie not found"}, status=status.HTTP_404_NOT_FOUND)

            # Delete Schedule related to the movie
            Showtime.objects.filter(movie_id=movie.movie_id).delete()

            # Finally, delete the movie
            movie.delete()

        return Response({"message": "Movie and related Showtimes deleted successfully"}, status=status.HTTP_204_NO_CONTENT)

class MoviesByLocation(APIView):
    def get(self, request):
        # Retrieve the location_name from query parameters
        location_name = request.query_params.get('location_name')
        if not location_name:
            return Response({"error": "Location name is required."}, status=status.HTTP_400_BAD_REQUEST)

        # Rest of the code remains the same
        location = get_object_or_404(Location, name=location_name)
        theaters = Theater.objects.filter(location_id=location.location_id)
        movie_ids = Showtime.objects.filter(
            theater_id__in=theaters.values_list('theater_id', flat=True)
        ).values_list('movie_id', flat=True).distinct()
        movies = Movie.objects.filter(movie_id__in=movie_ids).values(*Movie_attribute_list)
        
        return Response(list(movies))

class TheatersByLocation(APIView):
    def get(self, request):
        # Retrieve the location_name from query parameters
        location_name = request.query_params.get('location_name')
        if not location_name:
            return Response({"error": "Location name is required."}, status=status.HTTP_400_BAD_REQUEST)

        # Fetch the location
        location = get_object_or_404(Location, name=location_name)

        # Fetch theaters in the specified location
        theaters = Theater.objects.filter(location_id=location.location_id).values()

        # Return the list of theaters
        return Response(list(theaters))

class MoviesByTheaterId(APIView):
    def get(self, request, pk):
        # Retrieve the Theater object based on the given primary key (pk)
        theater = get_object_or_404(Theater, pk=pk)

        # Get all distinct movie_ids for this theater from the showtimes
        movie_ids = Showtime.objects.filter(
            theater_id=theater.theater_id
        ).values_list('movie_id', flat=True).distinct()

        movies_data = []
        for movie_id in movie_ids:
            # Fetch movie details
            movie = Movie.objects.filter(movie_id=movie_id).values(*Movie_attribute_list).first()

            # Fetch showtimes for the specific movie and theater
            showtimes = Showtime.objects.filter(
                movie_id=movie_id,
                theater_id=theater.theater_id
            ).values('showtime', 'from_date', 'to_date', 'discount')

            # Add showtime details to the movie
            if movie:
                movie['showtimes'] = list(showtimes)
                movies_data.append(movie)

        return Response(movies_data)

@api_view(['GET'])
def movies_watched(request, user_id):
    # Get the current date and time 
    now = timezone.now()

    # Calculate the date 30 days ago
    past_30_days = now - timedelta(days=30)

    # Find all bookings for the user in the past 30 days
    bookings = Booking.objects.filter(
        user_id=user_id,
        booking_timestamp__gte=past_30_days
    )
    # Get all unique schedule_ids from these bookings
    schedule_ids = bookings.values_list('schedule', flat=True).distinct()

    # Get all schedules for these ids
    schedules = Showtime.objects.filter(schedule_id__in=schedule_ids)

    # Get all unique movie_ids from these schedules
    movie_ids = schedules.values_list('movie', flat=True).distinct()

    # Get all movies for these ids
    movies = Movie.objects.filter(movie_id__in=movie_ids).values(*Movie_attribute_list)

    return Response(list(movies))

@api_view(['GET'])
def user_booking_summary(request, user_id):
    bookings = Booking.objects.filter(user_id=user_id)
    total_seats = bookings.aggregate(Sum('number_of_seats'))['number_of_seats__sum'] or 0
    total_points = bookings.aggregate(Sum('points_earned'))['points_earned__sum'] or 0

    return Response({
        'user_id': user_id,
        'total_seats_booked': total_seats,
        'total_points_earned': total_points
    })

class MovieList(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request):
        movies = Movie.objects.all()
        serializer = MovieSerializer(movies, many=True)
        return Response(serializer.data)    

class MovieDetail(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, pk):
        movie = get_object_or_404(Movie, pk=pk)
        serializer = MovieSerializer(movie)
        return Response(serializer.data)    

# Theater views
class GetTheaters(APIView):
    def get(self, request):
        theaters = Theater.objects.all().select_related('location')
        result = []

        for theater in theaters:
            theater_data = {
                'theater_id': theater.theater_id,
                'name': theater.name,
                'seating_capacity': theater.seating_capacity,
                'distance': theater.distance,
                'coordinates': theater.coordinates,
                'address': theater.address,
                'location_id': theater.location.location_id,
                'location_name': theater.location.name
            }
            result.append(theater_data)

        return Response(result)



class TheaterDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Theater.objects.all()
    serializer_class = TheaterSerializer


# Showtime Views
class ShowtimeList(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request):
        showtimes = Showtime.objects.all()
        serialized_data = ShowtimeSerializer(showtimes, many=True).data

        for showtime_data in serialized_data:
            movie_id = showtime_data.get('movie')
            theater_id = showtime_data.get('theater')

            # Fetch the movie title
            movie = Movie.objects.filter(movie_id=movie_id).first()
            showtime_data['movie_title'] = movie.title if movie else "Unknown"

            # Fetch the theater name
            theater = Theater.objects.filter(theater_id=theater_id).first()
            showtime_data['theater_name'] = theater.name if theater else "Unknown"

        return Response(serialized_data)
  

class ShowtimeDetail(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, pk):
        showtimes = get_object_or_404(Showtime, pk=pk)
        serializer = ShowtimeSerializer(showtimes)
        return Response(serializer.data)    

# Booking Views

class BookingList(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request):
        bookings = Booking.objects.all()
        serializer = BookingSerializer(bookings, many=True)
        return Response(serializer.data)  

class BookingDetail(APIView):
    renderer_classes = [JSONRenderer]

    def get(self, request, pk):

        bookings = Booking.objects.filter(user_id=pk).values()

        booking_ids = bookings.values_list('booking_id', flat=True).distinct()

        myBookings = Booking.objects.filter(booking_id__in=booking_ids)

        # Get all unique schedule_ids from these bookings
        schedule_ids = bookings.values_list('schedule', flat=True).distinct()

        # Get all schedules for these ids
        schedules = Showtime.objects.filter(schedule_id__in=schedule_ids)

        # Get all unique movie_ids from these schedules
        movie_ids = schedules.values_list('movie', flat=True).distinct()

        booking_movies = []

        for booking in myBookings:

             # Get all schedules for these ids
            movies = Movie.objects.filter(movie_id__in = movie_ids)

            movie_data = [{
                'title': mv.title,
                'poster_url': mv.poster_url
            } for mv in movies]

            # Adding theater details and its showtimes to the list
            booking_movies.append({
                'booking_id': booking.booking_id,
                'total_amount_paid': booking.total_amount_paid,
                'booking_timestamp': booking.booking_timestamp,
                'show_timestamp': booking.showtimestamp,
                'is_refund_requested': booking.is_refund_requested,
                'points_earned': booking.points_earned,
                'seat_list': booking.seat_list,
                'movies': movie_data
            })
    
        return Response(booking_movies, status=status.HTTP_200_OK)

# Functionality Views

class AccumulatePoints(generics.UpdateAPIView):
    queryset = UserProfile.objects.all()
    serializer_class = UserProfileSerializer

    def perform_update(self, serializer):
        user_profile = self.request.user.userprofile
        dollars_spent = self.request.data.get('dollars_spent', 0)
        accumulated_points = dollars_spent // 1  # 1 point per dollar
        user_profile.rewards_points += accumulated_points
        user_profile.save()

class MultiSeatBooking(generics.CreateAPIView):
    queryset = Booking.objects.all()
    serializer_class = BookingSerializer

    def perform_create(self, serializer):
        num_seats = self.request.data.get('num_seats')
        if num_seats > 8:
            raise ValidationError("You can book up to 8 seats only.")
        # Here you can also add logic to check seat availability, calculate total price etc.
        serializer.save()        

# CRUD Views

class MovieCRUD(generics.RetrieveUpdateDestroyAPIView):
    queryset = Movie.objects.all()
    serializer_class = MovieSerializer
    permission_classes = [permissions.IsAdminUser]

class ShowtimeCRUD(generics.RetrieveUpdateDestroyAPIView):
    queryset = Showtime.objects.all()
    serializer_class = ShowtimeSerializer
    # permission_classes = [permissions.IsAdminUser]     

    def post(self, request, *args, **kwargs):
        theater_id = kwargs.get('theater_id')
        if 'update-discount' in request.path:
            return self.update_discount(request, theater_id)
        else:
            return Response({'error': 'POST method not allowed'}, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def update_discount(self, request, theater_id):
        """
        Update discount for all showtimes of a specific theater with showtime before 6 PM.
        """
        discount_value = request.data.get('discount')
        if discount_value is None:
            return Response({'error': 'Discount value is required.'}, status=status.HTTP_400_BAD_REQUEST)

        # Parse discount_value to a decimal to ensure it's valid
        try:
            discount_value = Decimal(discount_value)
        except InvalidOperation:
            return Response({'error': 'Invalid discount value.'}, status=status.HTTP_400_BAD_REQUEST)

        # Filter showtimes for the given theater_id and showtime before 6 PM
        showtimes_before_6pm = Showtime.objects.filter(
            theater_id=theater_id,
            showtime__lt=timezone.make_aware(time(18, 0))  # Correct usage
        )

        # Update the discount for these showtimes
        updated_count = showtimes_before_6pm.update(discount=discount_value)

        return Response({'message': f'Discount updated successfully for {updated_count} showtimes.'}, status=status.HTTP_200_OK)
 
    
class TheaterCRUD(generics.RetrieveUpdateDestroyAPIView):
    queryset = Theater.objects.all()
    serializer_class = TheaterSerializer
    # permission_classes = [IsAuthenticated, IsAdminUser]  # Assuming admin-only access

    def post(self, request, *args, **kwargs):
        theater_id = kwargs.get('theater_id')
        print("ABC",theater_id)
        if 'change-seating' in request.path:
            return self.change_seating(request, theater_id)
        else:
            return Response({'error': 'POST method not allowed'}, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def change_seating(self, request, theater_id):
        theater = self.queryset.get(theater_id = theater_id)

        new_capacity = request.data.get('seating_capacity')
        if new_capacity and new_capacity.isdigit():
            new_capacity = int(new_capacity)
            theater.seating_capacity = new_capacity
            theater.save()
            return Response({'message': 'Seating capacity updated successfully.'}, status=status.HTTP_200_OK)
        return Response({'error': 'Invalid seating capacity.'}, status=status.HTTP_400_BAD_REQUEST)

class RegisterView(generics.CreateAPIView):
    queryset = UserProfile.objects.all()
    serializer_class = UserProfileSerializer

    @api_view(['POST'])
    def register(request):
        # Extract the necessary information from the POST request
        username = request.data.get('username')
        password = request.data.get('password')
        email_id = request.data.get('email_id')

        # Check if a user with the same username already exists
        if UserProfile.objects.filter(username=username).exists():
            return Response({'error': 'Username already exists'}, status=status.HTTP_400_BAD_REQUEST)        

        # Validate the email using Django's validators
        try:
            RegexValidator(regex='^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$')(email_id)
        except ValidationError as e:
            return Response({'error': str(e)}, status=status.HTTP_400_BAD_REQUEST)

        # Check if a user with the same email already exists
        if UserProfile.objects.filter(email_id=email_id).exists():
            return Response({'error': 'Email already exists'}, status=status.HTTP_400_BAD_REQUEST)

        # Create the User
        user = UserProfile.objects.create(
            username=username,
            password=password, 
            email_id=email_id
        )

        return Response({'message': 'User registered successfully'}, status=status.HTTP_201_CREATED)


# View for user login , should be added to a LoginViewClass
class LoginView(APIView):
    def post(self, request):
        username = request.data.get('username')
        password = request.data.get('password')
        user = authenticate( request, username=username, password=password)
        if user is None:
            return Response({'error': 'Invalid login credentials'},status=status.HTTP_400_BAD_REQUEST)
        # token, _ = Token.objects.get_or_create(user=user)
        # return Response({'token': token.key})
        # return Response({'message': 'User login successfull'})

        return Response({
            'user_id': user.user_id,
            'user_membership': user.membership_type,
            'total_points_earned': user.reward_points
        }, status=status.HTTP_200_OK)

# View for fetching membership options
class MembershipOptionsView(generics.ListAPIView):
    serializer_class = MembershipOptionsSerializer

    def get_queryset(self):
        return UserProfile.MEMBERSHIP_CHOICES

class CreateBookingView(APIView):
    def post(self, request, *args, **kwargs):

        # Retrieve the location_name from query parameters
        username = request.query_params.get('username')
        if not username:
            return Response({"error": "username is required."}, status=status.HTTP_400_BAD_REQUEST)
        schedule_id = request.data.get('schedule_id')
        date = request.data.get('date')
        total_amount = request.data.get('total_amount')
        seat_mapping = request.data.get('seat_mapping')
        number_of_seats = len(seat_mapping.split(','))
        payment_type = request.data.get('payment_type')

        user_profile = UserProfile.objects.get(username=username)
        schedule = Showtime.objects.get(schedule_id=schedule_id)

        if number_of_seats > 8:
            return Response({'error': 'You can book a maximum of 8 seats per transaction.'}, status=400)

        points = 0
        if payment_type=='reward_points':
            user_profile.reward_points-= total_amount*10
        else:
            points = total_amount//1
            user_profile.reward_points+= points
        
        user_profile.save()

        booking_time = timezone.now()
        showtime = datetime.combine(datetime.strptime(date, '%Y-%m-%d').date(), schedule.showtime)

        new_booking = Booking.objects.create(
            user_id=user_profile.user_id,
            schedule_id=schedule_id,
            number_of_seats=number_of_seats,
            total_amount_paid=total_amount,
            booking_timestamp=booking_time,
            showtimestamp=showtime,
            is_refund_requested=False,
            points_earned = points,
            seat_list = seat_mapping,
            payment_type = payment_type
        )

        seat_map = SeatMap.objects.get_or_create(schedule_id=schedule_id,date=date)[0]
        if seat_map.current_seatmap:    
            seat_list = seat_map.current_seatmap.split(',')
            seat_list.append(seat_mapping)
            seat_list.sort()
            seat_map.current_seatmap = ','.join(seat_list)
        else:
            seat_map.current_seatmap = seat_mapping
        seat_map.date = date
        seat_map.save()

        return Response({'message': 'Booking successful', 'booking_id': new_booking.booking_id})

class CancelBookingView(APIView):
    def get(self, request, booking_id):
        # Retrieve the booking based on booking_id
        booking = get_object_or_404(Booking, booking_id=booking_id)
        current_time = timezone.now()

        # #Check if the showtime has not passed (need to factor in date)
        # if current_time >= booking.showtimestamp:
        #     return Response({"error": "Cannot cancel past bookings"}, status=status.HTTP_400_BAD_REQUEST)

        # Check if the booking was made using reward points
        if booking.payment_type =='reward_points':
            return Response({"error": "Bookings made with reward points cannot be refunded"}, status=status.HTTP_400_BAD_REQUEST)

        # Retrieve the user profile
        user_profile = UserProfile.objects.get(user_id=booking.user.user_id)

        # Deduct points earned from this booking
        user_profile.reward_points -= booking.points_earned
        user_profile.save()

        # Mark the booking as refunded
        booking.is_refund_requested = True
        booking.save()

        # Need to add logic for cancelling SEATMAP
        seat_map = SeatMap.objects.get(schedule_id=booking.schedule_id, date=booking.showtimestamp.date())
        original_list = seat_map.current_seatmap.split(',')
        remove_list = booking.seat_list.split(',')
        seat_map.current_seatmap = ','.join(sorted(list(set(original_list).difference(remove_list))))
        seat_map.save()

        return Response({"message": "Booking cancelled successfully"}, status=status.HTTP_200_OK)