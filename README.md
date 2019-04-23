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


### Ambiente/Programas necessário(s):
* Maven 3.x.x
* Java 8 ou superior
* [Docker] (https://www.docker.com/products/docker-desktop/)
* [Docker Toolbox](https://docs.docker.com/toolbox/toolbox_install_windows/)
* [Git Bash](https://git-scm.com/)
* IDE de desenvolvimento [InteliJ]

## Projeto
O objetivo da API é fazer uso das funcionalidades do Envers, que permite uma organização do versionamento dos dados gerenciados pela aplicação.

### Requisitos minimos
É necessário instalar as seguintes ferramentas para acompanhar o projeto.
- [Docker Toolbox](https://docs.docker.com/toolbox/toolbox_install_windows/)
- [Github](https://gitforwindows.org/) (Opcional)
- Possuir 4GB RAM para o docker

### Executando:

1. Clonar o projeto
2. Subir uma instância do banco de dados PostgresSQL[Doocker ToolBox] por meio do comando:  
    `docker run --name postgres-db -p 5432:5432 \`  
    `-e POSTGRES_PASSWORD=1234 \`  
    `-e POSTGRES_USER=postgres-user \`  
    `-e POSTGRES_DB=postgres_db \`  
    `-d postgres:latest`  
3. Executar o comando `mvn clean install`
4. Executar o comando `mvn spring-boot:run` ou executar a classe `Application`.

### Testando a Demo:

### Passo a passo de desenvolvimento:
#### **1-Vamos criar um novo projeto**

Utilizando o site: [Spring Initializr](https://start.spring.io/) com as dependências: **JPA,WEB,e o banco de dados(PostgreSQL)**.

1. Com o propjeto criado. Adicione a dependência do Hibernate Envers:

       <dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-envers</artifactId>
			<version>${hibernate.version}</version>
		</dependency>
		
 -  Versão utilizada <version>5.4.2.Final</version>

2. Configurar em `/src/main/resources/application.yml` as propriedades do banco de dados.
```yml

spring:
  datasource:
    url: jdbc:postgresql://IP_MAQUINA_:5432/postgres_db
    username: postgres-user
    password: 1234
  jpa:
    hibernate.ddl-auto: update
    properties.hibernate.temp.use_jdbc_metadata_defaults: false
    database-platform: org.hibernate.dialect.PostgreSQL9Dialect
	
```
Para que os Envers criem as tabelas de auditoria históricas necessárias e armazenem as alterações feitas nas entidades auditadas e suas associações, o que se segue é tudo o que é necessário para começar:

    Adicione a dependência de hibernate-envers ao seu classpath.
    "Anote" sua entidade e/ou suas propriedades @Audited.
    Certifique-se de que suas entidades usem identificadores exclusivos imutáveis (chaves primárias).
    
    
 Exemplo
 Considerando que temos uma entidade Person, ao marcá-la com a anotação Audited, o Hibernate irá gerar as tabelas no banco
 Person; Person_AUD; REVINFO
```    

@Entity
@Audited
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String surname;

    private String comments;
}
```
Revision

Muito parecido com o controle de versão do código-fonte, o Envers usa um conceito de revisões.

Uma revisão identifica uma coleção de alterações em entidades e suas associações para todos os atributos auditados que ocorreram dentro do limite de uma transação. Essas revisões são globais e numéricas.

A API AuditReader fornece várias maneiras de consultar entidades em revisões específicas e recuperar uma visão parcial da aparência dessa entidade nessa revisão específica. Ele também permite que você tenha acesso a listas de revisões associadas a um tipo de entidade ou restritas por um período. A API também fornece uma maneira de obter os metadados de revisão para que você possa saber quando uma alteração ocorreu, além de quaisquer atributos personalizados adicionais que você possa ter armazenado na entidade de revisão com base em suas necessidades de implementação.


 Auditing the entity INSERT operation
```
Person person = new Person();
person.setId( 1L );
person.setFirstName( "John" );
person.setLastName( "Doe" );

entityManager.persist( person );

insert
into
    Person
    (firstName, lastName, id)
values
    (?, ?, ?, ?)

-- binding parameter [1] as [TIMESTAMP] - [Mon Jul 24 17:21:32 EEST 2017]
-- binding parameter [2] as [VARCHAR]   - [John]
-- binding parameter [3] as [VARCHAR]   - [Doe]
-- binding parameter [4] as [BIGINT]    - [1]

insert
into
    REVINFO
    (REV, REVTSTMP)
values
    (?, ?)

-- binding parameter [1] as [BIGINT]    - [1]
-- binding parameter [2] as [BIGINT]    - [1500906092803]

insert
into
    Person_AUD
    (REVTYPE, created_on, firstName, lastName, id, REV)
values
    (?, ?, ?, ?, ?, ?)

-- binding parameter [1] as [INTEGER]   - [0]
-- binding parameter [2] as [TIMESTAMP] - [Mon Jul 24 17:21:32 EEST 2017]
-- binding parameter [3] as [VARCHAR]   - [John]
-- binding parameter [4] as [VARCHAR]   - [Doe]
-- binding parameter [5] as [BIGINT]    - [1]
-- binding parameter [6] as [INTEGER]   - [1]
```
Auditing the entity UPDATE operation
```
Person person = entityManager.find( Person.class, 1L );
person.setLastName( "Doe Jr." );

update
    Person
set
    created_on=?,
    firstName=?,
    lastName=?
where
    id=?

-- binding parameter [1] as [TIMESTAMP] - [2017-07-24 17:21:32.757]
-- binding parameter [2] as [VARCHAR]   - [John]
-- binding parameter [3] as [VARCHAR]   - [Doe Jr.]
-- binding parameter [4] as [BIGINT]    - [1]

insert
into
    REVINFO
    (REV, REVTSTMP)
values
    (?, ?)

-- binding parameter [1] as [BIGINT]    - [2]
-- binding parameter [2] as [BIGINT]    - [1500906092853]

insert
into
    Person_AUD
    (REVTYPE, firstName, lastName, id, REV)
values
    (?, ?, ?, ?, ?, ?)

-- binding parameter [1] as [INTEGER]   - [1]
-- binding parameter [2] as [TIMESTAMP] - [2017-07-24 17:21:32.757]
-- binding parameter [3] as [VARCHAR]   - [John]
-- binding parameter [4] as [VARCHAR]   - [Doe Jr.]
-- binding parameter [5] as [BIGINT]    - [1]
-- binding parameter [6] as [INTEGER]   - [2]




Person person = entityManager.find( Person.class, 1L );
person.setLastName( "Doe Jr." );

update
    Person
set
    firstName=?,
    lastName=?
where
    id=?

-- binding parameter [1] as [TIMESTAMP] - [2017-07-24 17:21:32.757]
-- binding parameter [2] as [VARCHAR]   - [John]
-- binding parameter [3] as [VARCHAR]   - [Doe Jr.]
-- binding parameter [4] as [BIGINT]    - [1]

insert
into
    REVINFO
    (REV, REVTSTMP)
values
    (?, ?)

-- binding parameter [1] as [BIGINT]    - [2]
-- binding parameter [2] as [BIGINT]    - [1500906092853]

insert
into
    Person_AUD
    (REVTYPE, firstName, lastName, id, REV)
values
    (?, ?, ?, ?, ?, ?)

-- binding parameter [1] as [INTEGER]   - [1]
-- binding parameter [2] as [TIMESTAMP] - [2017-07-24 17:21:32.757]
-- binding parameter [3] as [VARCHAR]   - [John]
-- binding parameter [4] as [VARCHAR]   - [Doe Jr.]
-- binding parameter [5] as [BIGINT]    - [1]
-- binding parameter [6] as [INTEGER]   - [2]
```

Auditing the entity DELETE operation
```
Person person = entityManager.getReference( Person.class, 1L );
entityManager.remove( person );

delete
from
    Person
where
    id = ?

-- binding parameter [1] as [BIGINT]    - [1]

insert
into
    REVINFO
    (REV, REVTSTMP)
values
    (?, ?)

-- binding parameter [1] as [BIGINT]    - [3]
-- binding parameter [2] as [BIGINT]    - [1500906092876]

insert
into
    Person_AUD
    (REVTYPE, firstName, lastName, id, REV)
values
    (?, ?, ?, ?, ?, ?)

-- binding parameter [1] as [INTEGER]   - [2]
-- binding parameter [2] as [TIMESTAMP] - [null]
-- binding parameter [3] as [VARCHAR]   - [null]
-- binding parameter [4] as [VARCHAR]   - [null]
-- binding parameter [5] as [BIGINT]    - [1]
-- binding parameter [6] as [INTEGER]   - [3]
```
