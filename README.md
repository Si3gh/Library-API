# DB1
## Hibernate Envers
# Glossário
Hibernate é um framework para o mapeamento objeto-relacional. 
Envers é um módulo do Hibernate que permite realizar auditoria das tabelas mapeadas de um banco de dados.
O Hibernate envers é um framework que facilita o mapeamento dos atributos entre uma base tradicional de dados relacionais e o modelo objeto de uma aplicação, mediante o uso de arquivos (XML) ou anotações Java (veja Annotation (java)). 

O Hibernate Envers oferece a organização do histórico das versões dos dados gerenciados pela aplicação, através das entidades mapeadas para a persistência JPA para auditar as modificações ocorridas em um dado registro. Dessa forma, com sua utilização, uma aplicação é capaz de gerir todas as modificações realizadas no seu banco de dados de forma fácil e não intrusiva.

Documentação:
* https://hibernate.org/orm/envers/
* https://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html/ch15.html
* https://docs.spring.io/spring-data/envers/docs/2.1.6.RELEASE/reference/html/

Leitura auxiliar:
* https://thoughts-on-java.org/hibernate-envers-getting-started/
* https://thoughts-on-java.org/hibernate-envers-query-data-audit-log/

### Exemplos de ferramentas
- [Kiwi Syslog Server](https://www.kiwisyslog.com/)
- [Graylog](https://www.graylog.org/)
- [ELK stack](https://www.elastic.co/elk-stack)

## Projeto
Neste projeto utilizaremos o sistema de ELK(Elasticsearch, Logstash e Kibana) para centralizar, quebrar e analisar os dados.

Ele pode ser um pouco complexo de configuração pelo fato de seus serviços serem separados, porém é por este motivo que este
conjunto de ferramentas é tão poderoso, é possivel realizar integração com qualquer outro produto da elastic que atenda a seu
objetivo com a ferramenta.

Iremos utilizar o ELK junto com o [docker](https://www.docker.com/) para criarmos containers e subi-los separadamente, e também
subiremos um app em Spring boot para simular um micro-serviço e alimentar nosso ELK com logs.

### Requisitos minimos
É necessário instalar as seguintes ferramentas para acompanhar o projeto.
- [Docker Toolbox](https://docs.docker.com/toolbox/toolbox_install_windows/)
- [Github](https://gitforwindows.org/) (Opcional)
- Possuir 4GB RAM para o docker

O Elasticsearch puxa muito da memória virtual e pode ser necessário aumenta-la para rodar corretamente as ferramentas elk.

Para aumentar a quantidade de memória virtual do docker é bem simples.
- **Abra o Docker Quickstart Terminal**

O docker toolbox roda em cima de uma maquina virtual de linux, vamos entrar dentro dela e trocar uma configuração.

- **[Digite](imagem1)** `docker-machine ssh`.
- **[Digite](imagem2)** `sudo sysctl vm.max_map_count=262144`.
- **Digite** `exit` para sair da maquina virtual.


## Subindo o ELK
Como a configuração de cada ferramenta separada pode ser complexa iremos utilizar uma maneira mais simplificada para subirmos todas 
as ferramentas em sua devida ordem.

Utilizaremos o repositório [docker-elk](https://github.com/deviantony/docker-elk) onde já está configurado toda a ordem para subir as 
ferramentas utilizando o docker-compose.

- Baixe o repositório (Clone ou download).
- ![Com o docker toolbox entre dentro da pasta do repositório baixado.](imagem3).

É possivel navegar pelas pastas utilizando `cd nomeDaPasta`, e para poder ver os nomes das pastas digite `ls`.

Caso queira voltar para a pasta anterior digite `cd ..` e para saber em qual diretório você está digite `pwd`.

- [Digite](imagem4) `docker-compose build` para [buildar o elk](imagem5).
- [Digite](imagem6) `docker-compose up` para subir as ferramentas para os containers.

Espere um pouco pois essa parte pode demorar um pouco para configurar.

Com esses passos é possivel rodar todas as ferramentas de uma vez, e já é possivel mexer na interface gráfica da kibana.

## Portas e configurações
No arquivo docker-compose é configurado onde serão as portas de entrada de cada container:
- Elasticsearch :9200
- Logstash :5000
- Kibana : 5601

Para acessar sua interface kibana apenas digite o ip da sua maquina virtual e a porta da kibana(5601) na url de qualquer navegador.

**Exemplo**: `192.168.99.100:5601`

O ip da sua maquina virtual pode ser encontrado ao abrir o docker ou ao digitar `docker-machine ip` no terminal do docker toolbox.
![](imagem7)

# Adicionando dados
Se você entrar na área de [Discover](imagem8) percebemos que não possuimos nenhum dado, então vamos adicionar nossos próprios dados.

Para podermos enviar os logs de nosso sistema que esta aqui neste github iremos subi-lo em um container e liga-lo a porta 5000
onde está nosso Logstash que será o responsável por receber, quebrar e enviar para o Elasticsearch. Para podermos enviar os dados
de forma direta vamos precisar fazer uma configuração na nossa maquina virtual.

## Configurando maquina virtual

Para o envio dos dados iremos utilizar o [driver do docker de logging](https://docs.docker.com/config/containers/logging/configure/
deste modo podemos configurar para que todo os dados serão jogados diretamente no Logstash.

- Abra um novo terminal do docker toolbox
- [Entre na maquina docker digitando](imagem1) `docker-machine ssh`
- [Vá para a pasta do docker digitando](imagem9) `cd /etc/docker` 
- [Crie um arquivo de configuração do driver digitando](imagem10) `sudo touch daemon.json`
- [Edite este arquivo pelo terminal digitando](imagem11) `sudo vi daemon.json`
- Pressione `i` para poder inserir dados
- [Escreva o seguinte json de configuração:](imagem12)

```
{ 
"log-driver":"syslog"
"log-opts": {
    "max-size": "10m",
    "max-file": "3",
    "labels": "production_status",
    "env": "os,customer"
  }
}

```

- Pressione a tecla `esc` para sair do modo de edição
- Escreva `:wq` e pressione enter para salvar as configurações e sair do editor.
- Escreva `exit` para sair da maquina virtual

## Rodando o micro-serviço

Após configurarmos o driver podemos enviar as mensagens de forma direta para o logstash, então vamos levantar nossa aplicação.

- [Vá para a pasta deste projeto com o docker toolbox.](imagem13)
- [Digite o comando](imagem14) `docker build -t dockerfile .  ` para construir a imagem do docker(O ponto final também é importante).
- [Digite o comando](imagem15) `docker run -p 8080:8080 --log-driver=syslog --log-opt syslog-address=tcp://:5000 --log-opt tag={{.Name}} --log-opt syslog-facility=daemon dockerfile`

Vamos entender o que está acontecendo neste comando:
- Com `docker run` a gente roda a imagem dockerfile que foi construida a partir do arquvio docker que está dentro da pasta deste projeto.
- Com -p a gente mapeia as portas do container 8080:8080 para poder acessar o container.
- Com `--log-driver=syslog` a gente define a utilização do driver de envio de logs do syslog.
- Com `--log-opt syslog-address=tcp://:5000` nós definimos que enviaremos as mensagens para a porta 5000 que é nosso logstash
- Com `--log-opt tag={{.Name}}` iremos mandar o nome do container como informação adicional, deste modo podendo saber qual container esta sendo utilizado.
- Com `--log-opt syslog-facility=daemon` iremos utilizar o arquivo de configuração que criamos dentro da maquina virtual.

[Agora por este terminal podemos ver todas as mensagens que são geradas pelo sistema](imagem16), e eles vão ser todos enviados para o logstash.

## Visualizando os dados

Depois de subir o container podemos acessa-lo digitando na url de qualquer navegador `ip-da-nossa-maquina-virtual:8080`
que é a porta mapeada quando foi levantado o container.

Exemplo :`192.168.99.100:8080`

Ao acessar as urls são geradas algumas mensagens simples:
- ip:8080/Info = Gera uma log de informação.
- ip:8080/Error = Gera uma log de erro.
- ip:8080/ = Gera uma log de saudação.
- ip:8080/json = Recebe mensagens post e exibe elas no logger.

Como configuramos para que todas as mensagens do console sejam mandadas para o logstash, mesmo que a mensagem não seja de um logger
ela é enviada.

Vamos acessar agora nosso kibana e ir em Discover [visualizar nossos dados](imagem18), [basta criar um index escolhendo nosso logstash](imagem17).

## Filtrando dados

As vezes queremos filtrar nossos dados de logs para podermos ter informaçoes mais relevantes e podermos separa-los em campos diferentes
quando formos visualizar nossos logs. Então vamos criar nosso próprio filtro para o logstash quebrar nossos dados.

- [Entre na pasta docker-elk que está nossas ferramentas ELK.](imagem19)
- Entre na pasta do logstash.
- Entre na pasta de pipeline.
- Abra o arquivo logstash.conf para editar com algum editor de textos.
- Adicione entre o input e o output um filtro.

Exemplo : 

```
input {
	tcp {
		port => 5000
	}
}
filter {

}
output {
	elasticsearch {
		hosts => "elasticsearch:9200"
	}
	
}
```
Existem vários modelos de [filtros](https://www.elastic.co/guide/en/logstash/current/filter-plugins.html) disponiveis mas vamos criar nosso próprio utilizando o dissect.

Para configurar esse filtro vamos pegar uma mensagem de exemplo:
`<30>Jan 31 14:15:14 cranky_elgamal[2592]: 2019-01-31 14:15:14.423  INFO 1 --- [           main] com.example.demo.DemoApplication         : Started DemoApplication in 15.683 seconds (JVM running for 17.484)`

A partir dela vamos quebra-la em partes para transformar estas partes em campos no nosso kibana.
` "%{+data/1} %{+data/2} %{+data/3} %{containerName}[%{id}]: %{data/4} %{hora} %{tipo} 1 --- %{exec} %{arquivo}   : %{funcao} : %{mensagem}" `

Para entender mais sobre criação filtro acesse a [documentação](https://www.elastic.co/guide/en/logstash/current/plugins-filters-dissect.html) .

O arquivo de configuração final do logstash deve ficar assim:

```
input {
	tcp {
		port => 5000
	}
}
filter {
    dissect {
      mapping => {
        "message" => "%{+data/1} %{+data/2} %{+data/3} %{containerName}[%{id}]: %{data/4} %{hora} %{tipo} 1 --- %{exec} %{arquivo}   : %{funcao} : %{mensagem}"
 	    }
    }
}
output {
	elasticsearch {
		hosts => "elasticsearch:9200"
	}
	
}
```

Agora vamos rebuildar o elk para que ele configure seus filtros.

- [No terminal que está rodando os logs do kibana/logstash/elasticsearch aperte `CTRL + C` para parar o processo.](imagem20)
- Digite `docker-compose down` para remover os container.
- Digite `docker-compose build` para rebuildar e adicionar os filtros.
- [Digite `docker-compose up` para subir novamente o elk.](imagem21)

Agora ao enviar as mensagens de log é possivel ver que ele [cria novos campos e os separa](imagem22) de acordo com nosso filtro!!!

Para mais informações visite o site da ferramenta: https://www.elastic.co/
