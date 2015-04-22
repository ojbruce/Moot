Moot
====

This is a joyfull application using scala and play framework and powered by Redis.

This small application is a twitter-like but with moot inside.

Powered by bootstrap, angularjs, css3, html5 ...


<p align="center">
  <img src="https://github.com/ojbruce/Moot/blob/master/public/images/moot_firstpage.png?raw=true"/>
</p>


Requirements:
------------
Have activator


Run the application :
---------------------
Run a redis instance on port : 6379

Run : `` activator run ``



Good readings before beginning :
--------------------------------

Play framework:
https://www.playframework.com/documentation/

Scala :
http://docs.scala-lang.org/overviews/

Scala futures :
https://www.playframework.com/documentation/2.3.x/ScalaAsync
http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html

Twitter :

http://highscalability.com/blog/2014/9/8/how-twitter-uses-redis-to-scale-105tb-ram-39mm-qps-10000-ins.html


Some tips about the project:
----------------------------

This was my first scala, play, redis project.
Therefore it is only the shape of it but I clearly learned some tricks.
Have a non blocking application is not quite easy. You need to learn some scala tricks : Future, for-comprehension structure, Either.
Not sure I myself have totally understand it.
But it was Fun trying!