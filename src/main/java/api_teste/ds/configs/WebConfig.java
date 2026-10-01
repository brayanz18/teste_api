package api_teste.ds.configs;

import org.springframework.context.annotation.Configuration; // Importa a anotação de configuração do Spring Container
import org.springframework.web.servlet.config.annotation.CorsRegistry; // importa a classe responsável por registrar as regras do CORS
import org.springframework.web.servlet.config.annotation.EnableWebMvc; // import a anotação que habilita os recursos do spring Web MVC
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // importa a interface de customização do Spring MVC

@Configuration // indica que essa classe possui configuraçõe de Beans que devem ser
               // inicializados com o Spring Ioc
@EnableWebMvc // importa e ativa o suporte básico as requisições e controladores Web Mvc do
              // Spring
public class WebConfig implements WebMvcConfigurer { // Classe de cinfiguração que implementa o contrato de customização
                                                     // do Spring

    @Override // Sobreescreve o método de mapeamento CORS padrão da interface WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry) { // Método indicado pelo Spring para registrar as regras do CORS
        registry.addMapping("/**"); // Libera qualquer rota da API(coringa "/**") para aceitar chamadas externas
    }
}