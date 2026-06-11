# Backend — Tetris Duel Online

Servidor de coordinacion en Node.js + Express + Socket.IO. Crea salas, une
jugadores, transmite ataques y notifica victorias y desconexiones. La logica
del juego corre en la app Android.

## Requisitos

- Node.js 18 o superior.

## Instalacion

```bash
npm install
```

## Ejecucion

```bash
npm start
```

Salida esperada:

```text
Server running on port 3000
```

## Validacion del contrato

Con el servidor encendido en otra terminal:

```bash
npm test
```

Simula dos jugadores y verifica los eventos: create_room, join_room,
game_start, send_attack/receive_attack, game_over/victory, error_message
y opponent_disconnected.

## Conexion desde la app Android

La laptop ejecuta el servidor y provee la red. Cada cliente usa una URL distinta:

| Cliente | URL del servidor |
| --- | --- |
| Emulador Android | `http://10.0.2.2:3000` |
| Celular fisico (misma red WiFi) | `http://<IP-LAN>:3000` |

Para obtener la IP de la laptop en macOS:

```bash
ipconfig getifaddr en0
```

La app permite editar esta URL en la pantalla inicial.

## Eventos

Cliente a servidor: `create_room`, `join_room`, `send_attack`, `game_over`.
Servidor a cliente: `room_created`, `game_start`, `receive_attack`, `victory`,
`opponent_disconnected`, `error_message`.
