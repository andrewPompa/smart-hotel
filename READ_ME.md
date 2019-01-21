# Smart Hotel by Andrzey Dynamics©®
Aplikacja **klient** (GUI) - **serwer** (obliczenia, przechowywanie informacji)

## Uruchomienie aplikacji
1) Pobranie z repozytorium kodu, adres: https://github.com/andrzej-dynamics/smart-hotel.git
2) Uruchomienie serwer z głowengo katalogu komendą: `java -jar hotel-1.0-SNAPSHOT.jar`
3) Uruchomienie przez przeglądarkę: http://localhost:9888/ (zalecany Chrome)

## Strony w aplikacji
- Logowanie klienta: 
http://localhost:9888/login/client
- Rejestracja klienta:
http://localhost:9888/register/client
- Informacje o klientacja
http://localhost:9888/client/info
- Dane o klientach:
http://localhost:9888/client/data
- Wyszukiwanie pokoi - klient:
http://localhost:9888/client/search-room
- Logowanie Administracja (login: user@user.pl, p: user ):
http://localhost:9888/login/admin
- Administracja:
http://localhost:9888/admin
- Administracja wyszukiwanie pokoi:
http://localhost:9888/admin/room-search
- Administracja klienci:
http://localhost:9888/admin/clients
- Administracja pokoje:
http://localhost:9888/admin/room
- Administracja rezerwacja:
http://localhost:9888/admin/reservation

## Schemat bazy danych znajduje się w głównym katalogu schema.ddl


#### Serwer
REST API w Spring'u. Endpoint'y do obsługi modyfikacji na wierzchołkach i krawędziach
***
#### Klient
`JQuery`

