import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoMensagem implements Runnable {
	private static Logger logger = Logger.getLogger(Class.class.getSimpleName());
	
	final String apelido, mensagem;
	Servidor servidor;
	
	public ServicoMensagem(String apelido, String mensagem, Servidor servidor) {
		super();
		this.apelido = apelido;
		this.mensagem = mensagem;
		this.servidor = servidor;
	}


	@Override
	public void run() {
		String logMsg = String.format("%1$tF %1$tT FINE (%2$s) - %3$s", System.currentTimeMillis(), apelido, mensagem);
		
		logger.log(Level.FINE, logMsg);
		
		synchronized (servidor.getParticipantes()) {
			for (Participante participante : servidor.getParticipantes()) {
				participante.enviarMensagem(String.format("%s: %s", apelido, mensagem));
			}
		}
	}
	
	public String getApelido() { return apelido; }
	public String getMensagem() { return mensagem; }
}
