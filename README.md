# Vaccine Finder
Service to notify when vaccination slots are available

1. Finds vaccination centers by pincode
2. Sends a notification mail with details of avaliable slots in vaccination centers

# Steps
Its a Spring Boot app.
1. Update the scheduler interval and mail configs in application.properties
2. Update the search criteria in application.yml
3. Package in a jar and deploy it on any free cloud service.

# Notice
1. POC project only. Use at your own risk.
2. This service uses only public APIs, so availability data is cached and may be upto 30 minutes old. Not much reliable.
