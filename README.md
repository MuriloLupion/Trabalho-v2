# Catálogo de jogos

API em Java e Spring Boot para cadastrar, consultar, atualizar e excluir jogos. No Trabalho II, os dados passaram a ser salvos no PostgreSQL.

## Como executar

É necessário ter Java 17 ou superior (pode usar o OpenJDK 25), PostgreSQL e Maven, ou abrir o projeto como Maven no IntelliJ.

1. No pgAdmin, crie um banco chamado `catalogo_jogos`. Também pode executar `sql/criar-banco.sql` conectado ao banco `postgres`.
2. Abra `src/main/resources/application.properties` e confira a URL, o usuário e a senha. Troque `sua_senha` pela senha do seu PostgreSQL, ou configure a variável de ambiente `DB_PASSWORD` no IntelliJ. Antes de colocar no GitHub, retire qualquer senha pessoal do arquivo.
3. No IntelliJ, abra o `pom.xml` como projeto Maven e recarregue as dependências.
4. Execute `CatalogoJogosApplication`. Pelo terminal, na pasta do `pom.xml`, pode usar `mvn spring-boot:run`.

A API fica em `http://localhost:8080/jogos`. A tabela `jogos` é criada automaticamente na primeira execução. A opção `ddl-auto=update` mantém os dados nas próximas execuções. O banco precisa estar criado e o PostgreSQL precisa estar rodando.

## Rotas

| Método | Rota | Uso |
| --- | --- | --- |
| GET | /jogos | Listar jogos e aplicar filtros |
| GET | /jogos/{id} | Buscar pelo ID |
| POST | /jogos | Cadastrar |
| PUT | /jogos/{id} | Atualizar todos os campos do jogo |
| DELETE | /jogos/{id} | Excluir |

Exemplo de JSON para POST e PUT:

```json
{
  "nome": "Stardew Valley",
  "genero": "Simulação",
  "plataforma": "PC",
  "concluido": false
}
```

O ID é gerado pelo banco e não precisa ser enviado no cadastro. `concluido` aceita `true` ou `false`, sem aspas. Nome, gênero e plataforma não podem ficar vazios.

Os filtros são opcionais e podem ser combinados:

- `/jogos?nome=Stardew%20Valley`
- `/jogos?genero=Simula%C3%A7%C3%A3o`
- `/jogos?plataforma=PC`
- `/jogos?concluido=false`
- `/jogos?genero=Simula%C3%A7%C3%A3o&plataforma=PC&concluido=false`

Os filtros de texto usam o valor completo e ignoram maiúsculas e minúsculas. Ao combinar filtros, o jogo deve atender a todos eles.

## Teste no Postman e no banco

Importe `postman/Catalogo de Jogos.postman_collection.json`. Se usar o Postman no navegador, utilize o Desktop Agent para acessar localhost.

1. Execute “1 - Cadastrar jogo”. A resposta deve ser 201, com o ID gerado. A coleção guarda esse ID na variável `jogoId`.
2. Execute “2 - Listar todos” e “3 - Buscar pelo ID”.
3. Pare a aplicação no IntelliJ e execute novamente. Não faça outro POST.
4. Execute “4 - Conferir após reiniciar a API”. O mesmo jogo deve aparecer com o mesmo ID.
5. No pgAdmin, abra o Query Tool do banco `catalogo_jogos` e execute `sql/consultar-jogos.sql`. Mostre o registro antes de excluí-lo.
6. Teste os filtros, o PUT e o DELETE nas próximas requisições. O PUT retorna 200, o DELETE retorna 204 e a busca de um ID excluído retorna 404.

Faça essa sequência manualmente, pois a etapa de reiniciar a aplicação não é feita pelo Postman.

## Código

- `Jogo.java`: entidade JPA. `@Entity` identifica a entidade, `@Id` indica a chave primária e `@GeneratedValue` faz o banco gerar o ID.
- `JogoRepository.java`: interface que estende `JpaRepository`. Ela fornece `save`, `findAll`, `findById` e `delete`. A consulta `@Query` aplica os filtros diretamente no banco. Cada parâmetro nulo é ignorado, e `AND` combina os filtros informados.
- `JogoController.java`: recebe as requisições, valida os campos e usa o repository para acessar os jogos. O repository é recebido pelo construtor.

Referências: [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html) e [configuração de banco no Spring Boot](https://docs.spring.io/spring-boot/reference/data/sql.html).
