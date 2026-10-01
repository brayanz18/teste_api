
//Url base da API Spring boot para buscar as tarefas do usuario de ID 1
const url = "http://localhost:8080/task/user/5";

//função responsável por ocultar o icone de carregamento
function hideLoader(){

    //Busca o elemento HTML com o id 'loading' e altera seu estilo de exibição para oculta-lo
    document.getElementById("loading").style.display = "none";
}

//Função responsável por construir o html da tabela e preenche-lo com as tarefas
function show(tasks){
    //Cria uma string contendo o cabeçalho da tabela utilizando Template Literals
    let tab =`
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
            <th scope="col">Usuario</th>
            <th scope="col">User ID</th>
        </tr>
    </thead>
    `;
    //Iterador 'for...of' 
    for(let Task of tasks){
        //Concatena uma nova linha html(<tr>) com as colunas (<td>) preenchida com dados da tarefa
        tab += `
        <tr>
            <td scope="row">${Task.id}</td>
            <td>${Task.description}</td>
            <td>${Task.user.username}</td>
            <td>${Task.user.id}</td>        
        </tr>
        `;
        }
        // Injeta a string acumulada diretamente na tabela através do ID 'tasks'
        document.getElementById("tasks").innerHTML = tab;
    }
        //Função assincrona encarregada de realizar a requisição  HTTP GET para a API
        async function getAPI(url) {

            //Executa a requisição HTTP usando a API nativa fetch() e aguarda(wait) a resposta da rede
            const response = await fetch(url, { method:"GET" });

            //Converte o corpo da resposta HTTP e formato JSON e gurada na variavel
            var data = await response.json();
            
            // Se a resposta for obtida com sucesso, executa a função de ocultar o carregamento
            if(response){
                hideLoader();
            }
            show(data);
        }
        
getAPI(url);