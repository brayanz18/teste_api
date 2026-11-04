
//url base da api
const API_URL = "http://localhost:8080";

//evento executado automaticamente assim que toda estrutura html for carregada
document.addEventListener("DOMContentLoaded", () => {
    getTask();
});

//função assincrona responsavel por buscar e exibir as tarefas
async function getTask() {

    const token = localStorage.getItem("Authorization");

    if (!token) {
        alert("Acesso não autorizado! Faça login primeiro");
        window.location.href = "login.html";
        return;
    }

    //Exibe o spinner de carregamento
    document.getElementById("Loading").style.display = "block";

    try {
        //envia requisição GET
        const response = await fetch(`${API_URL}/task/user`, {
            method: "GET",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json"
            }
        });

        //Se o token for aceito pela API
        if (response.ok) {

            const tasks = await response.json();

            renderTasks(tasks);

        } else if (response.status === 403 || response.status === 401) {

            alert("Sessão expirada. Por favor, faça login novamente");
            logout();

        } else {

            alert("Erro ao buscar tarefas.");

        }

    } catch (error) {

        console.error("Erro na busca por tarefas:", error);
        alert("Erro de conexão com o servidor");

    } finally {

        document.getElementById("Loading").style.display = "none";

    }
}

//Função responsavel por montar o HTML da lista de tarefas recebidas da API
function renderTasks(tasks) {
    const taskList = document.getElementById("task-List");//Seleciona o elemento <ul> no DOM
    taskList.innerHTML = ""; //limpa a lista antes de inserir novos itens 

    //Se a lista retorna estiver vazia
    if(tasks.length === 0){
        taskList.innerHTML = '<li class="list-group-item text-center text-muted"> Nenhuma tarefa encontrada.</li>';
    return;
    }

    //Percorre cada objeto do tipo TaskProjection(possuindo apenas o id e description)
    tasks.forEach(task => {
        //cria um elemento da lista <li> do bootstrap
        const li = document.createElement("li");
        li.className="list-group-item d-flex justify-content-between align-items-center";

        //define o texto da descrição na tarefa na lista
        li.innerHTML = '<span>${task.description{</span>';

        //adiciona o item a lista principal html
        taskList.appendChild(li);
    });


}

//função responsavel por encerrar a sessão do usuario por navegador
function logout(){

    //remove a chave "authorization" contendo o token de localStorage
    localStorage.removeItem("Authorization");

    //redireciona o usuario para pagina de login
    window.location.href ="login.html";
}
