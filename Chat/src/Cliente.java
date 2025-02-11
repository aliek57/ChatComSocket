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
	
	private static final String ENDERECO = "127.0.0.1";
	private static final int PORTA = 50123;
	
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	String apelido, mensagem;
	
	public static void main(String[] args) {
		new Cliente().iniciar();
	}
	
	public void iniciar() {
		Scanner scanner = new Scanner(System.in);
		
		try {
			socket = new Socket(ENDERECO, PORTA);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.print("Digite seu apelido: ");
            apelido = scanner.nextLine();
            out.println(apelido);
            
            new Thread(new Receptor()).start();
            
            while (true) {
                System.out.print("Mensagem: ");
                mensagem = scanner.nextLine();
                
                if (mensagem.equalsIgnoreCase("##sair##")) {
                    out.println("##sair##");
                    break;
                }
                
                out.println(mensagem);
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
                    System.out.println(mensagem);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Erro ao receber mensagem.", e);
            }
        }
    }
}
