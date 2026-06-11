const { io } = require("socket.io-client");

const URL = "http://localhost:3000";
const resultados = [];

function verificar(nombre, condicion) {
    resultados.push({ nombre, ok: condicion });
    console.log(`${condicion ? "PASS" : "FALL"}  ${nombre}`);
}

function esperarEvento(socket, evento, tiempo = 3000) {
    return new Promise((resolve, reject) => {
        const temporizador = setTimeout(
            () => reject(new Error(`Timeout esperando '${evento}'`)),
            tiempo
        );
        socket.once(evento, (datos) => {
            clearTimeout(temporizador);
            resolve(datos);
        });
    });
}

function conectar() {
    const socket = io(URL, { transports: ["websocket"], forceNew: true });
    return new Promise((resolve) => socket.on("connect", () => resolve(socket)));
}

async function main() {
    const jugadorUno = await conectar();
    const jugadorDos = await conectar();
    verificar("Ambos jugadores se conectan", jugadorUno.connected && jugadorDos.connected);

    jugadorUno.emit("create_room");
    const salaCreada = await esperarEvento(jugadorUno, "room_created");
    const codigo = salaCreada.roomId;
    verificar("create_room devuelve room_created con roomId", typeof codigo === "string" && codigo.length > 0);

    const inicioUno = esperarEvento(jugadorUno, "game_start");
    const inicioDos = esperarEvento(jugadorDos, "game_start");
    jugadorDos.emit("join_room", { roomId: codigo });
    await Promise.all([inicioUno, inicioDos]);
    verificar("join_room dispara game_start en ambos", true);

    const ataque = esperarEvento(jugadorDos, "receive_attack");
    jugadorUno.emit("send_attack", { roomId: codigo, garbageLines: 2 });
    const datosAtaque = await ataque;
    verificar("send_attack llega como receive_attack con garbageLines", datosAtaque.garbageLines === 2);

    const victoria = esperarEvento(jugadorUno, "victory");
    jugadorDos.emit("game_over", { roomId: codigo });
    await victoria;
    verificar("game_over notifica victory al rival", true);

    const jugadorTres = await conectar();
    const errorSala = esperarEvento(jugadorTres, "error_message");
    jugadorTres.emit("join_room", { roomId: "NOEXISTE" });
    const datosError = await errorSala;
    verificar("join_room a sala inexistente devuelve error_message", typeof datosError.message === "string");
    jugadorTres.close();

    const desconexion = esperarEvento(jugadorUno, "opponent_disconnected");
    jugadorDos.close();
    await desconexion;
    verificar("disconnect notifica opponent_disconnected", true);

    jugadorUno.close();

    const fallidas = resultados.filter((r) => !r.ok).length;
    console.log(`\n${resultados.length - fallidas}/${resultados.length} verificaciones correctas`);
    process.exit(fallidas === 0 ? 0 : 1);
}

main().catch((error) => {
    console.error("ERROR:", error.message);
    process.exit(1);
});
