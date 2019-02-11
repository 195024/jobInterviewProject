# jobInterviewProject

I tried to implement the idea of putting nulls at the end and displaying them as progress bars as in here:
http://knowmywork.com/load-more-in-recyclerview-with-progressbar/?fbclid=IwAR1JxNB8GyDDr_fTkdI-gzb4Wx9dJDLL5eflDGN82ytG8ytxFLNdx7HVALU
but the flags were checked before the API responses came back and I wasn't able to fix this so I scrapped this idea.

To avoid reaching the API calls limit of themoviedb I wanted to load 30 movies with details at first
and then i.e. when the 20th movie would be reached on RecycleView I would fetch another 30 and so on, but I failed to create
a proper condition to trigger fetching details for the movies after the 59th position. So another idea got scrapped...

I also tried to somehow respond to the 429 HTTP reponse of too much calls, but to no avail.

In the end I just left the list with miserable 30 movies :(

P.S. I hope the massive amount of git commits won't be taken into account :D

Aleksander
