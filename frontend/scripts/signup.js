
// URL base da API Spring Boot
const API_URL = "http://localhost:8080";

// Função responsável pelo cadastro
async function signup(event) {

    event.preventDefault();

    // Captura os dados informados
    const usernameInput = document.getElementById("username").value.trim();
    const passwordInput = document.getElementById("password").value;

    try {

        // Envia os dados para a API
        const response = await fetch(`${API_URL}/user`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },

            body: JSON.stringify({
                username: usernameInput,
                password: passwordInput
            })
        });

        // Verifica se o cadastro foi realizado
        if (response.ok) {

            alert("Usuário cadastrado com sucesso! Faça login para continuar.");

            window.location.href = "login.html";

        } else {

            // Recupera a mensagem enviada pela API
            const responseText = await response.text();

            let errorMessage =
                `Não foi possível cadastrar. HTTP ${response.status}`;

            if (responseText) {

                try {

                    const errorData = JSON.parse(responseText);

                    if (errorData.message) {
                        errorMessage = errorData.message;
                    }

                    if (Array.isArray(errorData.errors)) {

                        errorMessage += "\n" + errorData.errors
                            .map(error =>
                                `${error.field}: ${error.message}`)
                            .join("\n");
                    }

                } catch (parseError) {

                    errorMessage = responseText.slice(0, 500);
                }
            }

            console.error("Falha no cadastro:", response.status, responseText);

            alert(errorMessage);
        }

    } catch (error) {

        console.error("Erro na requisição de cadastro:", error);

        alert("Erro ao conectar com o servidor.");
    }
}
