
import os
import pymysql
from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'API.settings')
pymysql.install_as_MySQLdb()
application = get_wsgi_application()
