# Chat com Java Sockets

## Descrição da Atividade
A atividade consiste em implementar uma aplicação de chat (bate-papo) em Java utilizando Sockets. A aplicação é composta de um **Cliente** que os usuários devem utilizar para trocar mensagens e de um **Servidor** que deverá centralizar e gerenciar todas as conexões das aplicações cliente.

## Especificação

### Servidor
#### Classe `Servidor`
- Implementa o método `main()` da aplicação Servidor.
- O servidor da aplicação deve aguardar conexões dos clientes na porta **50123**.
- Múltiplas requisições devem ser atendidas simultaneamente pelo servidor. Crie uma **thread** para cada participante.
- Armazene cada um dos clientes conectados (participantes) em uma lista que deve ser percorrida para enviar a mensagem para todos os participantes. Atente-se para a possibilidade da lista ser modificada enquanto está sendo percorrida.
- Crie um **worker thread (FixedThreadPool)** que será responsável por enviar cada mensagem à lista de participantes. Vamos chamar este worker thread de **"fofoqueiro"**.

#### Classe `Participante` – Runnable
- Implementa as **threads** responsáveis por manter a comunicação com as aplicações cliente.
- Ao receber uma mensagem do respectivo cliente, o participante deve criar uma tarefa (`ServicoMensagem`) e solicitar para o worker thread **"fofoqueiro"** executá-la.
- A thread do participante encerra quando receber a mensagem **"##sair##"** da aplicação cliente.

#### Classe `ServicoMensagem` – Runnable
- **Atributos:**
  - `apelido` (emissor)
  - `texto`
- Representa a tarefa que deve ser executada pelo worker thread **"fofoqueiro"**.
- A tarefa basicamente é percorrer a lista de clientes conectados no servidor e enviar a mensagem para cada um deles (inclusive para o emissor da mensagem).
- A mensagem enviada deve ser apresentada no console do servidor como um **log**, no seguinte formato:
  ```
  27/04/2021 22:50 FINE (apelido do remetente) - Mensagem
  ```

### Cliente
#### Classe `Cliente`
- Implementa o método `main()` da aplicação Cliente.
- A aplicação cliente deve receber o **endereço IP** do servidor e o **apelido** do participante via linha de comando ao ser executada.
- O cliente deve ler a mensagem digitada pelo usuário e enviá-la para o servidor. O servidor se encarrega de replicar a mensagem para todos os clientes, inclusive para o emissor da mensagem.
- Ao fechar a janela, a aplicação cliente deve enviar a mensagem **"##sair##"** para o servidor.

---
### Como Executar o Projeto
1. **Compilar o código:**
   ```sh
   javac *.java
   ```
2. **Executar o Servidor:**
   ```sh
   java Servidor
   ```
3. **Executar um Cliente:**
   ```sh
   java Cliente <IP_do_Servidor> <Apelido>
   ```
   Exemplo:
   ```sh
   java Cliente 127.0.0.1 Aliek
   ```

---
### Exemplo de Funcionamento
- O **Servidor** exibe mensagens de log indicando conexões e mensagens enviadas.
- Os **Clientes** enviam mensagens para o servidor, que as distribui para todos os participantes.
- Ao digitar **"##sair##"**, o cliente encerra sua conexão.

---

Desenvolvido para a disciplina de **Programação Concorrente**.
