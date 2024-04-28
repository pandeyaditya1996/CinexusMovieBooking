# custom_auth_backend.py

from django.contrib.auth.backends import BaseBackend
from .models import UserProfile

class CustomBackend(BaseBackend):
    def authenticate(self, request, username=None, password=None, **kwargs):
        try:
            user = UserProfile.objects.get(username=username)
            if user.password == password:
                return user
        except UserProfile.DoesNotExist:
            return None
