# PhotoLoader

Example app using MVVM pattern with DataBinding for loading free photos from [Unsplash](https://unsplash.com/). The following libraries are used in the app:
- Room as ORM to cache photos and favourites data,
- RxJava2 for async requests/background tasks,
- Retrofit2 for HTTP-requests,
- Picasso for pictures preview loading,
- Yandex MapKit as an example to build route.

All you need to build the app is create ***api.xml*** in ***app/src/main/res/values*** folder:
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="access_key" translatable="false">%YOUR UNSPLASH ACCESS KEY%</string>
    <string name="secret_key" translatable="false">%YOUR UNSPLASH SECRET KEY%</string>
    <string name="map_key" translatable="false">%YOUR YANDEX MAP KEY%</string>
</resources>
```
## Photos screen:
![Photos screen](Photos.gif)

## Favourites screen:
![Favourites screen](Favourites.gif)
