<script>
    document.getElementById("formDesaparecido").addEventListener("submit", function(event) {
        event.preventDefault();

        const data = {
            nombre: document.getElementById("nombre").value,
            apellido: document.getElementById("apellido").value,
            edad: parseInt(document.getElementById("edad").value),
            descripcion: document.getElementById("descripcion").value
        };

        fetch("http://localhost:8080/desaparecidos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(async response => {
            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }
            return response.json();
        })
        .then(result => {
            document.getElementById("resultado").innerText =
                `✅ Persona registrada con ID: ${result.id}`;
        })
        .catch(error => {
            document.getElementById("resultado").innerText =
                `❌ Error al registrar: ${error.message}`;
        });
    });
</script>