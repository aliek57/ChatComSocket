import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
	private static Logger logger = Logger.getLogger(Class.class.getSimpleName());
	
	private static final int PORTA = 50123;
	
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	String apelido, mensagem;
	
	public static void main(String[] args) {
		if (args.length < 2) {
            logger.log(Level.SEVERE, "EX: java Cliente <IP_Servidor> <Apelido>");
            return;
        }
        String endereco = args[0];
        String apelido = args[1];
        new Cliente().iniciar(endereco, apelido);
	}
	
	public void iniciar(String endereco, String apelido) {
		Scanner scanner = new Scanner(System.in);
		
		try {
			socket = new Socket(endereco, PORTA);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            this.apelido = apelido;
            out.println(apelido);
            
            logger.log(Level.INFO, "Conectado ao servidor {0} como {1}", new Object[]{endereco, apelido});
            
            new Thread(new Receptor()).start();
            
            while (true) {
            	logger.log(Level.FINE, "Aguardando usuário escrever");
                mensagem = scanner.nextLine();
                
                if (mensagem.equalsIgnoreCase("##sair##")) {
                    out.println("##sair##");
                    break;
                }
                
                out.println(mensagem);
                logger.log(Level.INFO, "{0}: {1}", new Object[]{apelido, mensagem});
            }
		} catch (IOException e){
			logger.log(Level.SEVERE, "Erro na comunicação com o servidor: ", e);
		} finally {
			encerrar();
			scanner.close();
		}
	}
	
	private void encerrar() {
		try {
			if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.log(Level.INFO, "Cliente desconectado.");
            }
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro ao fechar conexão do cliente.", e);
		}
	}
	
	private class Receptor implements Runnable {
        @Override
        public void run() {
            try {
                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                	logger.log(Level.INFO, mensagem);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Erro ao receber mensagem.", e);
            }
        }
    }
}
