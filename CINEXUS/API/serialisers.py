from rest_framework import serializers
from .models import Movie, Theater, Showtime, Booking, UserProfile 
from django.contrib.auth.models import User
from rest_framework.authtoken.models import Token

class MovieSerializer(serializers.ModelSerializer):
    class Meta:
        model = Movie
        fields = '__all__'

class ShowtimeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Showtime
        fields = '__all__'

class BookingSerializer(serializers.ModelSerializer):
    class Meta:
        model = Booking
        fields = '__all__'

class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserProfile
        fields = '__all__'

class TheaterSerializer(serializers.ModelSerializer):
    class Meta:
        model = Theater
        fields = '__all__'

# Serializer for user registration

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'password']
        extra_kwargs = {'password': {'write_only': True, 'required': True}}

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        Token.objects.create(user=user)  # Create an auth token for the user
        return user

# Serializer for membership options

class MembershipOptionsSerializer(serializers.Serializer):
    membership_type = serializers.ChoiceField(choices=UserProfile.MEMBERSHIP_CHOICES)
