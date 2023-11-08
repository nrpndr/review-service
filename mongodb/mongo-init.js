conn = new Mongo();
db = conn.getDB("cineevent");

db.MovieReviews.insert({"movieId":"1","userId":1,"starRating":4,"reviewDescription":"Amazing Movie","reviewDate":1699470968420});
db.MovieReviews.insert({"movieId":"1","userId":2,"starRating":4,"reviewDescription":"Excellent, must watch","reviewDate":1699470968421});
db.MovieReviews.insert({"movieId":"2","userId":1,"starRating":2,"reviewDescription":"Boring, rubbish story","reviewDate":1699470968422});
db.MovieReviews.insert({"movieId":"2","userId":2,"starRating":2,"reviewDescription":"Second half is boring","reviewDate":1699470968423});

db.EventReviews.insert({"eventId":"1","userId":1,"starRating":4,"reviewDescription":"Amazing Event","reviewDate":1699470968420});
db.EventReviews.insert({"eventId":"1","userId":2,"starRating":4,"reviewDescription":"Excellent","reviewDate":1699470968421});
db.EventReviews.insert({"eventId":"2","userId":1,"starRating":2,"reviewDescription":"Boring","reviewDate":1699470968422});
db.EventReviews.insert({"eventId":"2","userId":2,"starRating":2,"reviewDescription":"Bored to Death","reviewDate":1699470968423});
