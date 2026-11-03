const API_URL = "http://localhost:8080"

async function login(event) {
    event.preventDefault();

    const usernameInput = document.getElementById("username").Value;
    const passwordInput = document.getElementById("password").Value;

    try {
        const response = await fetch('${API_URL}/login', {
            method: "POST", headers: {
                "Content_Type": "application/json", "Aceept": "application/json"
            },
            body: JSON.stringify({
                username: usernameInput,
                password: passwordInput
            })
        });

        if (response.ok) {
            const token = response.headers.get("Authentication");

            if (token) {
                localStorage.setItem("Authorization", token);

                alert("Login realizado com sucesso");

                window.location.href = "index.html";

            } else {
                alert("Usuario ou senha invalidos");

            }
        }
    }
    catch (error) {
        console.error("Erro na requisição de login", error);
        alert("Erro ao conectar com o servidor");
    }
}
