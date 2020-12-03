# BetterRTP
It was an advanced plugin to rtp.

## API

Api is really easy to use. 
You can do what you want with this interface

```java
/// RTP api instance, don't create a new Instance of it
RTPApi rtpApi = RTPApi.getInstance();

Set<RTP> rtpList = rtpApi.getRTPs(); // Get all rtp instance
RTP rtp = rtpApi.getRTPbyTag("tag"); // Return the rtp instance of a specific tag
rtpApi.add(new RTP(tag, ...)); // Add an rtp instance
```

## Event

You can do a lot of thing with this cancellable event
> You can define the random location and set actionbar, title, sub and message string
> Moreover, you could set if the player use song and potion effect

```java
@EventHandler
public void onRTP(PlayerRandomTeleportEvent event){
	event.//do
}
```

## Other Tools

You can find my free and open-source library on my github.
You have :
 - **BetterSQL** : an easy and optimised way to save your stuff with sqlite. [Click here](https://www.github.com/betterstudio/betterSql)
 - **BetterTool**  : the most powerful spigot developement tool.  [Click here](https://www.github.com/betterstudio/betterTool)
