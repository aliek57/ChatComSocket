import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
	private static final int PORTA = 50123;
	private static Logger logger = Logger.getLogger(Class.class.getSimpleName());
	
	final List<Participante> participantes = new ArrayList<>();
	final ExecutorService fofoqueiro = Executors.newFixedThreadPool(10);
	
	public static void main(String[] args) {
		new Servidor().iniciar();
	}
	
	public void iniciar() {
		try(ServerSocket serverSocket = new ServerSocket(PORTA)) {
			logger.log(Level.INFO, "Servidor iniciado na porta {0}", PORTA);
			
			while(true) {
				Socket socket = serverSocket.accept();
				logger.log(Level.INFO, "Novo cliente conectado: {0}", socket.getInetAddress());
				
				Participante participante = new Participante(socket, this);
				
				synchronized (participantes) {
					participantes.add(participante);
				}
				
				new Thread(participante).start();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Erro no servidor: ", e);
		}
	}
	
	public void fofocar(ServicoMensagem sm) {
		synchronized (participantes) {
			for (Participante participante : participantes) {
				if (!participante.getApelido().equals(sm.getApelido())) {
					participante.enviarMensagem(sm.getMensagem());
				}
			}
		}
	}
	
	public void removerParticipante(Participante participante) {
		synchronized (participantes) {
			participantes.remove(participante);
		}
		logger.log(Level.INFO, "Participante removido: {0}", participante);
	}
	
	public List<Participante> getParticipantes() { return participantes; };
}
