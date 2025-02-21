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
		String logMsg = String.format("%1$td/%1$tm/%1$tY %1$tH:%1$tM (%2$s) - %3$s", new java.util.Date(), apelido, mensagem);
	    logger.log(Level.INFO, logMsg);
	    
		synchronized (servidor.getParticipantes()) {
			for (Participante participante : servidor.getParticipantes()) {
				participante.enviarMensagem(String.format("%s: %s", apelido, mensagem));
			}
		}
	}
	
	public String getApelido() { return apelido; }
	public String getMensagem() { return mensagem; }
}
