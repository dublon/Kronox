# Kronox

This is a fork for our existing system at SLU. As of Jan 1st 2020 we are retiring KronoX at SLU but here over the next few days I will upload the small changes we made to k3larra's excellent Kronox digital signage program. Notably this version has support for additional characters and I hard coded room names as opposed to simply displaying room codes. I hope this is of use to other Kronox users. Best wishes, Ian

At Sveriges lantbruksuniversitet (Alnarp, Alnarpsgården), it looks like this, and ran for nearly two years on a Raspberry Pi 3:

![](kronoxSLU.jpg)

K3larra's original text:

This code is used to view Kronox data for a specific building on a Full HD screen in portrait mode.
In my case, it runs on a remotely controlled Raspberry Pi that downloads a jarfiles and runs them or shows webpages (another story). 
(You can run the code from any computer with a screen in portrait mode.)

Change in the file Constants.java and some of the images in the assets directory to adapt it.

The jarfile should run directly if you have java jre or jdk istalled on the computer.
/L

At Malmö University, it looks like this:

![](KronoxScreen/Kronox.jpg)
