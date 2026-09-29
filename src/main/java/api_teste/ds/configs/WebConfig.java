package api_teste.ds.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //indica que essa classe possui configurações de Beans e deve ser inicializado com o Spring  
@EnableWebMvc  //importa e ativa o suporte basico as requisições e controladores Web Mvc do Spring
public class WebConfig implements WebMvcConfigurer { //classe de configuração que implementa o contrato de customização do Spring
    
    @Override //sobrescreve metodo de mapeamento cors padrão da interface WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry){ //metodo indicado pelo spring para registrar as regras do cors
        registry.addMapping("/**");//Libere qualquer rota da api (coringa "/**") para aceitar chamadas externas
    
    }
}
