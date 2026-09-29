//urlbase da API Spring boot para buscar as tarefas do usuario id 1
const url = "http://localhost:8080/task/user/1";

//função responsavel por ocultar o icone de carregamento
function hideloader() {
    //busca o elemento html com o id loading e altera o seu estilo de exibição para oculta-lo
    document.getElementById("loading").style.display = "none";

}//função responsavel por construir o html da tabela e preenhe-lo com as tarefas
function show(tasks){
    //Cria uma string contendo o cabeçãlho da tabela utilizando Template literals
    let tab =`
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
            <th scope="col">Usuario</th>
            <th scope="col">User</th>
        </tr>
    </thead>
    `;
    //interador 'for...of'
    for(let task of tasks){
            //concatena uma nova linha HTML(<tr>) com as colunas (<td>) preenchida com dados da tarefa 
        tab +=`
        <tr>
        <td scope="row">${task.id}</td>
        <td>${task.description}</td>
        <td>${task.user.username}</td>
        <td>${task.user.id}</td>


        </tr>
        `;
    }
    //injeta string acumulada diretamente na tabela atraves do id "tasks"
    document.getElementById("tasks").innerHTML =tab;

    //função assincrona encarregada de realizar a requisição HTTP GET para a API
    async function getAPI(url){

        const response = await fetch(url,{method:"GET"});
        
        //executa a requisição HTTP e formato json e guarda na variavel
        var data = await response.json();

        if (response){
            hideLoader(); 
        }
    }
}    
