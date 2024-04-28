from django.db import models
from django.core.validators import RegexValidator


class Theater(models.Model):
    theater_id = models.AutoField(primary_key=True)
    location = models.ForeignKey('Location', on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    seating_capacity = models.IntegerField()
    distance = models.CharField(max_length=255, null=True, blank=True)    
    coordinates = models.CharField(max_length=255, null=True, blank=True)  # Store coordinates as a string
    address = models.CharField(max_length=255, null=True, blank=True)

    class Meta:
        db_table = 'Theaters'


class TheaterOwner(models.Model):
    theater_owner_id = models.AutoField(primary_key=True)
    user = models.ForeignKey('UserProfile', on_delete=models.CASCADE)
    owner_username = models.CharField(max_length=255)
    permissions = models.CharField(max_length=255)  # Assuming permissions are stored as text
    theater_list = models.CharField(max_length=255)  # Assuming list of theater IDs as comma-separated string

    class Meta:
        db_table = 'Theater_Owner'


class Movie(models.Model):
    movie_id = models.AutoField(primary_key=True)
    title = models.CharField(max_length=255)
    description = models.TextField(null=True, blank=True)
    release_date = models.DateField()
    duration = models.IntegerField(help_text='Duration in minutes')
    genre = models.CharField(max_length=255,blank=True)
    rating = models.DecimalField(max_digits=3, decimal_places=1,blank=True)
    poster_url = models.CharField(max_length=255,blank=True)
    banner_url = models.CharField(max_length=255,blank=True)
    censor_rating = models.BooleanField(blank=True)
    language = models.CharField(max_length=255,blank=True)
    cast_and_crew = models.CharField(max_length=255,blank=True)
    director = models.CharField(max_length=255,blank=True)
    currently_running = models.IntegerField(blank=True)

    class Meta:
        db_table = 'Movies'

class Showtime(models.Model):
    schedule_id = models.AutoField(primary_key=True)
    movie = models.ForeignKey('Movie', on_delete=models.CASCADE)
    theater = models.ForeignKey('Theater', on_delete=models.CASCADE)
    showtime = models.TimeField()
    from_date = models.DateField()
    to_date = models.DateField()
    discount = models.DecimalField(max_digits=5, decimal_places=2,blank=True)

    class Meta:
        db_table = 'Schedules'    

class Booking(models.Model):
    booking_id = models.AutoField(primary_key=True)
    user = models.ForeignKey('UserProfile', on_delete=models.CASCADE)
    schedule = models.ForeignKey('Showtime', on_delete=models.CASCADE)
    number_of_seats = models.IntegerField()
    total_amount_paid = models.DecimalField(max_digits=10, decimal_places=2)
    booking_timestamp = models.DateTimeField()
    showtimestamp = models.DateTimeField()
    is_refund_requested = models.BooleanField()
    points_earned = models.IntegerField()
    seat_list = models.CharField(max_length=255)
    payment_type = models.CharField(max_length=255)

    class Meta:
        db_table = 'Bookings'    

class UserProfile(models.Model):
    # user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_id = models.AutoField(primary_key=True)
    username = models.CharField(max_length=255)
    email_id = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,
        validators=[
            RegexValidator(
                regex='^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$',
                message='Enter a valid email address',
                code='invalid_email'
            ),
        ]
    )
    password = models.CharField(max_length=255)  # Consider using a hash function for storing passwords
    role = models.CharField(max_length=255)
    reward_points = models.PositiveIntegerField(default=0)
    MEMBERSHIP_CHOICES = [
        ('Regular', 'Regular'),
        ('Premium', 'Premium'),
    ]
    membership_type = models.CharField(max_length=7, choices=MEMBERSHIP_CHOICES, default='Regular')    

    class Meta:
        db_table = 'Users'    


class Location(models.Model):
    location_id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=255)

    class Meta:
        db_table = 'Locations'

class SeatMap(models.Model):
    seatmap_id = models.AutoField(primary_key=True)
    schedule = models.ForeignKey('Showtime', on_delete=models.CASCADE)
    date = models.DateField()
    current_seatmap = models.CharField(max_length=255)

    class Meta:
        db_table = 'seatMap'

