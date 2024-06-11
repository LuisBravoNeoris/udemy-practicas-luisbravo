
# Practica Spring Boot

Practica con PokeAPI en Spring Boot de Luis Bravo


## API Consumida
 - [Pokemon API V2](https://pokeapi.co/api/v2/pokemon)
   
## Colección Postman
 - [Colección](https://documenter.getpostman.com/view/24257361/2sA3XMjP7m)


## Rutas de OAuth2

#### OAuth2 Authorization 

```http
  GET http://127.0.0.1:8080/oauth2/authorization/client-app
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `basic_auth` | `string` | **Autenticación: Básica**  (usuario: 'client-app', contraseña: 'secret') |

#### Login

```http
  POST http://127.0.0.1:9000/login
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `basic_auth`      | `string` | **Autenticación: Básica**  (usuario: 'client-app', contraseña: 'secret') |
| `username`      | `string` | **Body** (valor: 'admin') |
| `password`      | `string` | **Body**. (valor: 'admin') |

#### OAuth2 Token

```http
  POST http://127.0.0.1:9000/oauth2/token
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `basic_auth`      | `string` | **Autenticación: Básica**  (usuario: 'client-app', contraseña: 'secret') |
| `code`      | `string` | **Body** (valor: 'your_authorization_code') |
| `grant_type`      | `string` | **Body**. Body (valor: 'authorization_code') |
| `redirect_uri`      | `string` | **Body**. Body (valor: 'http://127.0.0.1:8080/authorized') |


## Rutas de Pokémon

#### Obtener Pokémon Cifrado por Nombre en URL

```http
  GET http://localhost:8080/pokemon/pikachu
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `none` | `none	` | Remplazar 'pikachu' por el pokemon deseado |

#### Obtener Pokémon Descifrado por Nombre en URL

```http
  GET http://localhost:8080/pokemon/decrypted/charizard
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `none` | `none	` | Remplazar 'charizard' por el pokemon deseado |

#### Obtener Pokémon Cifrado por JSON

```http
  POST http://localhost:8080/pokemon/search
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string	` | **Body** (valor: 'charizard'). Solo se puede buscar pokemones por nombre si no estan guardados en memoria|
| `weight` | `number` | **Body** (valor: 60). Se puede filtrar por otros atributos si el pokemon esta guarado en memoria |

#### Obtener Pokémon Descifrado por JSON

```http
  POST http://localhost:8080/pokemon/decrypted/search
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string	` | **Body** (valor: 'charizard'). Solo se puede buscar pokemones por nombre si no estan guardados en memoria|
| `weight` | `number` | **Body** (valor: 60). Se puede filtrar por otros atributos si el pokemon esta guarado en memoria |
