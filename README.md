# Congestion Tax Calculator

This is my take on the Congestion Tax calculation.

I have made it simple and trying to use the code provided but with changes that I felt necessary.

My logic is that for every toll booth pass a record of the vehicle is created and aggregated.  
When 60 minutes (changed to 3 minutes for the sake of not waiting in execution) has passed the aggregation is 
flushed and the calculation of taxes is started.

The aggregation is based on the vehicle registration number or ID, a type and timestamp. I have used the default 
_Car_ as an example but that can be tweaked.


All values are hardcoded but can easilty be transferred to an external configuration of choice, properties file or 
database.

I would suggest to use a JMS queue instead using transactions so that no records is lost in case of a breakdown.

Since I opted out of any unit tests for the sake of time you can test it by running it and curl/httpie to   
http://localhost:8080/camel/tax/<regNo>; http://localhost:8080/camel/tax/abc123

Each call will be aggregated and then on threshold of time a statement for each Vehicle will be printed on the console.

