let tratamientos = [];
let citas = [];
let rutaActualCitas = "/api/citas";

async function solicitar(ruta, opciones = {}) {
    const respuesta = await fetch(ruta, opciones);

    if (!respuesta.ok) {
        const error = await respuesta.json().catch(() => ({}));
        throw new Error(error.mensaje || `Error HTTP ${respuesta.status}`);
    }

    if (respuesta.status === 204) return null;
    return respuesta.json();
}

function escapar(texto) {
    return String(texto ?? "").replace(/[&<>"']/g, caracter => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    }[caracter]));
}

function mostrarMensaje(texto, error = false) {
    const mensaje = document.getElementById("mensaje");
    mensaje.textContent = texto;
    mensaje.hidden = false;
    mensaje.style.backgroundColor = error ? "#ffe8e8" : "#e6f2ee";
    mensaje.style.borderColor = error ? "#c62828" : "#247a65";
}

function datosJson(datos) {
    return {
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos)
    };
}

async function cargarTratamientos() {
    tratamientos = await solicitar("/api/tratamientos");
    const tabla = document.getElementById("tabla-tratamientos");

    tabla.innerHTML = tratamientos.map(t => `
        <tr>
            <td>${t.id}</td>
            <td>${escapar(t.nombre)}</td>
            <td>${escapar(t.descripcion)}</td>
            <td>Q${Number(t.precio).toFixed(2)}</td>
            <td>
                <button type="button" data-accion="editar" data-id="${t.id}">Editar</button>
                <button type="button" data-accion="eliminar" data-id="${t.id}">Eliminar</button>
            </td>
        </tr>
    `).join("") || '<tr><td colspan="5">No hay tratamientos registrados.</td></tr>';

    const opciones = tratamientos.map(t =>
        `<option value="${t.id}">${escapar(t.nombre)}</option>`
    ).join("");

    document.getElementById("cita-tratamiento").innerHTML =
        '<option value="">Seleccione un tratamiento</option>' + opciones;

    document.getElementById("filtro-tratamiento").innerHTML =
        '<option value="">Todos los tratamientos</option>' + opciones;
}

async function cargarCitas(ruta = rutaActualCitas) {
    rutaActualCitas = ruta;
    citas = await solicitar(ruta);
    const tabla = document.getElementById("tabla-citas");

    tabla.innerHTML = citas.map(c => {
        const tratamiento = tratamientos.find(t => t.id === c.tratamientoId);

        const acciones = c.estado === "PROGRAMADA" ? `
            <button type="button" data-accion="editar" data-id="${c.id}">Editar</button>
            <button type="button" data-accion="ATENDIDA" data-id="${c.id}">Atendida</button>
            <button type="button" data-accion="CANCELADA" data-id="${c.id}">Cancelar</button>
        ` : "Sin acciones";

        return `
            <tr>
                <td>${c.id}</td>
                <td>${escapar(c.paciente)}</td>
                <td>${escapar(c.fecha)}</td>
                <td>${escapar(c.hora?.slice(0, 5))}</td>
                <td>${escapar(tratamiento?.nombre || "No disponible")}</td>
                <td>${escapar(c.estado)}</td>
                <td>${escapar(c.observaciones)}</td>
                <td>${acciones}</td>
            </tr>
        `;
    }).join("") || '<tr><td colspan="8">No hay citas para mostrar.</td></tr>';
}

async function cargarTotal() {
    const total = await solicitar("/api/citas/total-estimado");
    document.getElementById("total-estimado").textContent =
        `Q${Number(total).toFixed(2)}`;
}

async function actualizarTodo() {
    await cargarTratamientos();
    await cargarCitas();
    await cargarTotal();
}

function limpiarTratamiento() {
    document.getElementById("tratamiento-form").reset();
    document.getElementById("tratamiento-id").value = "";
    document.getElementById("cancelar-tratamiento").hidden = true;
    document.getElementById("guardar-tratamiento").textContent = "Guardar tratamiento";
}

function limpiarCita() {
    document.getElementById("cita-form").reset();
    document.getElementById("cita-id").value = "";
    document.getElementById("cancelar-cita").hidden = true;
    document.getElementById("guardar-cita").textContent = "Guardar cita";
}

document.getElementById("tratamiento-form").addEventListener("submit", async evento => {
    evento.preventDefault();

    const id = document.getElementById("tratamiento-id").value;

    const datos = {
        nombre: document.getElementById("tratamiento-nombre").value.trim(),
        descripcion: document.getElementById("tratamiento-descripcion").value.trim(),
        precio: Number(document.getElementById("tratamiento-precio").value)
    };

    try {
        await solicitar(id ? `/api/tratamientos/${id}` : "/api/tratamientos", {
            method: id ? "PUT" : "POST",
            ...datosJson(datos)
        });

        limpiarTratamiento();
        await actualizarTodo();
        mostrarMensaje("Tratamiento guardado correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
});

document.getElementById("tabla-tratamientos").addEventListener("click", async evento => {
    const boton = evento.target.closest("button");
    if (!boton) return;

    const tratamiento = tratamientos.find(t => t.id === Number(boton.dataset.id));
    if (!tratamiento) return;

    if (boton.dataset.accion === "editar") {
        document.getElementById("tratamiento-id").value = tratamiento.id;
        document.getElementById("tratamiento-nombre").value = tratamiento.nombre;
        document.getElementById("tratamiento-descripcion").value = tratamiento.descripcion || "";
        document.getElementById("tratamiento-precio").value = tratamiento.precio;
        document.getElementById("guardar-tratamiento").textContent = "Actualizar tratamiento";
        document.getElementById("cancelar-tratamiento").hidden = false;
        return;
    }

    if (!confirm("¿Eliminar este tratamiento?")) return;

    try {
        await solicitar(`/api/tratamientos/${tratamiento.id}`, {
            method: "DELETE"
        });

        await actualizarTodo();
        mostrarMensaje("Tratamiento eliminado correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
});

document.getElementById("cita-form").addEventListener("submit", async evento => {
    evento.preventDefault();

    const id = document.getElementById("cita-id").value;

    const datos = {
        paciente: document.getElementById("cita-paciente").value.trim(),
        fecha: document.getElementById("cita-fecha").value,
        hora: document.getElementById("cita-hora").value,
        tratamientoId: Number(document.getElementById("cita-tratamiento").value),
        observaciones: document.getElementById("cita-observaciones").value.trim(),
        estado: "PROGRAMADA"
    };

    try {
        await solicitar(id ? `/api/citas/${id}` : "/api/citas", {
            method: id ? "PUT" : "POST",
            ...datosJson(datos)
        });

        limpiarCita();
        await actualizarTodo();
        mostrarMensaje("Cita guardada correctamente.");
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
});

document.getElementById("tabla-citas").addEventListener("click", async evento => {
    const boton = evento.target.closest("button");
    if (!boton) return;

    const cita = citas.find(c => c.id === Number(boton.dataset.id));
    if (!cita) return;

    const accion = boton.dataset.accion;

    if (accion === "editar") {
        document.getElementById("cita-id").value = cita.id;
        document.getElementById("cita-paciente").value = cita.paciente;
        document.getElementById("cita-fecha").value = cita.fecha;
        document.getElementById("cita-hora").value = cita.hora.slice(0, 5);
        document.getElementById("cita-tratamiento").value = cita.tratamientoId;
        document.getElementById("cita-observaciones").value = cita.observaciones || "";
        document.getElementById("guardar-cita").textContent = "Actualizar cita";
        document.getElementById("cancelar-cita").hidden = false;
        return;
    }

    if (!confirm(`¿Cambiar la cita a ${accion}?`)) return;

    try {
        await solicitar(`/api/citas/${cita.id}/estado`, {
            method: "PATCH",
            ...datosJson({ estado: accion })
        });

        await cargarCitas();
        await cargarTotal();
        mostrarMensaje(`Cita marcada como ${accion}.`);
    } catch (error) {
        mostrarMensaje(error.message, true);
    }
});

document.getElementById("cancelar-tratamiento").addEventListener("click", limpiarTratamiento);
document.getElementById("cancelar-cita").addEventListener("click", limpiarCita);

document.getElementById("btn-todas").addEventListener("click", () =>
    cargarCitas("/api/citas").catch(error => mostrarMensaje(error.message, true))
);

document.getElementById("btn-pendientes").addEventListener("click", () =>
    cargarCitas("/api/citas/pendientes").catch(error => mostrarMensaje(error.message, true))
);

document.getElementById("btn-fecha").addEventListener("click", () => {
    const fecha = document.getElementById("filtro-fecha").value;

    if (!fecha) {
        return mostrarMensaje("Selecciona una fecha.", true);
    }

    cargarCitas(`/api/citas/por-fecha?fecha=${fecha}`)
        .catch(error => mostrarMensaje(error.message, true));
});

document.getElementById("btn-tratamiento").addEventListener("click", () => {
    const id = document.getElementById("filtro-tratamiento").value;

    if (!id) {
        return mostrarMensaje("Selecciona un tratamiento.", true);
    }

    cargarCitas(`/api/citas/por-tratamiento/${id}`)
        .catch(error => mostrarMensaje(error.message, true));
});

actualizarTodo().catch(error => mostrarMensaje(error.message, true));